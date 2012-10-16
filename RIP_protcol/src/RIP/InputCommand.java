package RIP;

import java.util.ArrayList;



public class InputCommand {

	public static void sendProcess(String sender) {
	ArrayList<String> receivers = Input.lookupTable.get(sender).links;	
	ArrayList<Thread> ConcurrentE = new ArrayList<Thread>();
	InputNode copyOfsender = new InputNode(sender,Input.lookupTable.get(sender).getAddresses());
	for (String receiver:receivers){
	Input.lookupTable.get(receiver).receiveMsgFrom(copyOfsender);
	System.out.println("send "+sender +" " + receiver);
	}
	for (String receiver:receivers){
		ConcurrentE.add(new Thread(Input.lookupTable.get(receiver)));
	}
	 for (Thread update: ConcurrentE){
		 update.start();
		 
	 }
	 
	 try {
		Thread.sleep(3000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	public  static void sendProcess(String sender,String receiver){
		
	}
}
