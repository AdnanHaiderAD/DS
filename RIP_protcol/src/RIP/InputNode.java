package RIP;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class InputNode implements Runnable{

	private String name;
	private int[] addresses;
	private boolean tableUpdated=false;
	LinkedList<String> Senders= new LinkedList<String>();
	
	LinkedList<Hashtable<Integer,LinkCost>> senderQue=new LinkedList<Hashtable<Integer,LinkCost>>();
	/*the routing table of the corresponding node */
	private  Hashtable<Integer,LinkCost> routingTable = null;
	/* list of the nodes that are linked to the current node*/
	ArrayList<String> links= null;
	
	public InputNode(String name ,int[] addresses){
		this.name=name;
		this.addresses=addresses;
		routingTable= new Hashtable<Integer,LinkCost>();
		 for (int i =0; i<addresses.length;i++){
			 routingTable.put(addresses[i], new LinkCost("local",0));
		 }
		 links=new ArrayList<String>();
	}
	
	public void addEntries(InputNode node){
		int[] destinations = node.getAddresses();
		if (!links.contains(node.getName()) && !node.getName().equals(getName())){
		links.add(node.getName());
		}
			for (int i=0;i<destinations.length;i++){
				routingTable.put(destinations[i], new LinkCost(node.getName(),1));
			}
		
		}
	public void settableUpdate(boolean state){
		tableUpdated = state;
	}
	public boolean gettableUpdate(){
		return tableUpdated;
	}
	public Hashtable<Integer, LinkCost> getRoutingTable(){
		return routingTable;
	}
	public Hashtable<Integer,LinkCost> copyRoutingTable(){
		Hashtable<Integer,LinkCost> sendingTable = new Hashtable<Integer,LinkCost>(routingTable.size());
		for (Enumeration<Integer> e = routingTable.keys(); e.hasMoreElements();){
			 Integer key = e.nextElement();
			 sendingTable.put(key, new LinkCost(routingTable.get(key)));
			}
		return sendingTable;
	}
	public void receiveMsgFrom(String sender, Hashtable<Integer,LinkCost>senderTable){
		Senders.add(sender);
		this.senderQue.add(senderTable);
	}
	public  synchronized void receiveTable(){
		settableUpdate(false);
		if (senderQue.size()==0){
			return;
		}
		
		
		Hashtable<Integer,LinkCost>senderTable = senderQue.remove();
		String sender= Senders.remove();
		
		System.out.println("send "+sender +" " + getName());
		String info="";
		Enumeration<Integer> e= senderTable.keys();
		/* the node acknowledges receive of data */
		while (e.hasMoreElements()){
			Integer address= (Integer) e.nextElement();
		    LinkCost linkcost = senderTable.get(address);
		    if (linkcost.link.equals("no-link")){
		    	info = info.concat(" ( "+address+ "|" + linkcost.link +"|"+   " i) ");
		    }else{
		    	info = info.concat(" ( "+address+ "|" + linkcost.link +"|"+ linkcost.cost + ") ");	
		    }
		}
		System.out.println(" receive "+ sender + " "+ getName() + info);
		
		
		/*RIP protocol */
		for (Enumeration<Integer> list= senderTable.keys(); list.hasMoreElements();){
		    Integer address= list.nextElement();
			LinkCost row = senderTable.get(address);
			 if (!row.link.equals(sender)){
				
				 if (row.cost !=Integer.MAX_VALUE){
				 row.cost= row.cost+1;
				 }
				/// String linkName = row.link;
				 row.link= sender;
				 
				  if (!routingTable.containsKey(address)){
					  routingTable.put(address, row);
					  settableUpdate(true);
					  
				  }else{
					  		for (Enumeration<Integer> local_list = routingTable.keys(); local_list.hasMoreElements();){
						         Integer destination = local_list.nextElement();
						    	 LinkCost destEntry= routingTable.get(destination);
						    	 if (destination==address ){
						    		// if (this.name.equals(linkName) && destEntry.link.equals("no-link")|| ((destEntry.link.equals(linkName) && destEntry.cost==Integer.MAX_VALUE))){
						    		//	 continue;
						    		 //}
						    		  if (row.cost< destEntry.cost|| (row.link.equals(destEntry.link) && row.cost!=destEntry.cost)){
						    			 routingTable.remove(destination);
						    			 routingTable.put(destination , new LinkCost(row));
						    			 
						    			  settableUpdate(true);
						    		 }
						    	 }
					  			}
					  }
				
			 }
		}
		
			if (gettableUpdate()){
			
		/* print the table */
				Enumeration<Integer> e2= routingTable.keys();
				String inform= this.getName();
				System.out.println(getName()+ "has been updated:"+ gettableUpdate());
				
				while (e2.hasMoreElements()){
						Integer address= (Integer) e2.nextElement();
						inform = inform.concat(" ( "+address+ "|" + routingTable.get(address).link +"|"+ routingTable.get(address).cost + ") ");
				}
				System.out.println(inform);
			}else{
				System.out.println(getName()+ "has been updated:"+ gettableUpdate());
			}
		
	}
	
	public synchronized void  linkFail(String process){
		System.out.println("link-fail"+ this.name +" "+ process);
		for (Enumeration<Integer> e =this.routingTable.keys(); e.hasMoreElements();){
			Integer address = e.nextElement();
			if (this.routingTable.get(address).link.equals(process)){
				this.routingTable.get(address).link = "no-link";
				this.routingTable.get(address).cost= Integer.MAX_VALUE;
			}
		}
		this.links.remove(process);
		if (this.Senders.contains(process)){
			int index = this.Senders.indexOf(process);
			Senders.remove(index);
			senderQue.remove(index);
		}
		if (!links.isEmpty()){
			
		InputCommand.sendProcess(this.name);
		}
	}
	
	public String getName(){
		return name;
		
	}
	public int[] getAddresses(){
		return addresses;
	}
	
	public  class LinkCost{
		String link;
		int cost;
		public LinkCost(String link,int cost){
		   this.link=link;
		   this.cost=cost;
		}
		
		public LinkCost(LinkCost lc)
		{
			this.link=lc.link;
			this.cost= lc.cost;
		}
	}

	@Override
	public void run() {
		
			
		//Rip protocol
	
		System.out.println(this.name+"s size of the list is " +senderQue.size());
		receiveTable();
		
		
		if (gettableUpdate()){
		settableUpdate(false);	
		InputCommand.sendProcess(this.name);
		Input.thread_fin+=1;
		}else{
		Input.thread_fin+=1;
		}
		}

	
		// TODO Auto-generated method stub
		
	}
	
	
	


