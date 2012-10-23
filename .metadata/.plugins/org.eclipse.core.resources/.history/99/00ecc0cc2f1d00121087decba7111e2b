package RIP;

import java.util.ArrayList;



public class InputCommand {
	 static int  level=0;

	@SuppressWarnings("deprecation")
	public static void sendProcess(String sender) {
	ArrayList<String> receivers = Input.lookupTable.get(sender).links;	
	ArrayList<Thread> ConcurrentE = new ArrayList<Thread>();
	
	for (String receiver:receivers){
	Input.lookupTable.get(receiver).receiveMsgFrom(sender, Input.lookupTable.get(sender).copyRoutingTable());
		//Input.lookupTable.get(receiver).receiveMsgFrom(sender,copies.get(receivers.indexOf(receiver)).getRoutingTable());
		
		

	}
	for (String receiver:receivers){
		ConcurrentE.add(new Thread(Input.lookupTable.get(receiver)));
		
	}
	level=level+1;
	System.out.println ("");
	System.out.println ("The LEVEL IS "+ level);
	System.out.println("");
	
	 for (Thread update: ConcurrentE){
		 
		 update.start();
		 
		 
	 }
	 
	 
		Thread.currentThread().stop();
	
	}
	public  static void sendProcess(String sender,String receiver){
		
	}
}
