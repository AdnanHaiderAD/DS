package RIP;

import java.util.ArrayList;



public class InputCommand {
	 
   
	@SuppressWarnings("deprecation")
	public static  void sendProcess(String sender) {
	InputNode sendernode =Input.lookupTable.get(sender);
	ArrayList<String> receivers = sendernode.links;	
	ArrayList<Thread> ConcurrentE = new ArrayList<Thread>();
	
	for (String receiver:receivers){
	Input.lookupTable.get(receiver).receiveMsgFrom(sender, Input.lookupTable.get(sender).copyRoutingTable());
		
		}
	
	for (String receiver:receivers){
		
		ConcurrentE.add(new Thread(Input.lookupTable.get(receiver)));
		}
	
	for (Thread update: ConcurrentE){
		/* simultaneously send copies of the updated routing table to all the neighbors*/
		update.start();
		if (!Input.linkfail){
		Input.thread_num+=1;
		}else{
			Input.linkSend+=1;
		}
		
		 
		 }	
	 
	}
	
	
	
	
}
