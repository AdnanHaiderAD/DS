package RIP;

import java.util.ArrayList;



public class InputCommand {

	public static void sendProcess(String sender){
	ArrayList<String> receivers = Input.lookupTable.get(sender).links;	
	ArrayList<Thread> ConcurrentE = new ArrayList<Thread>();
	for (String receiver:receivers){
	Input.lookupTable.get(receiver).receiveMsgFrom(Input.lookupTable.get(sender));
	System.out.println("send "+sender +" " + receiver);
	}
	for (String receiver:receivers){
		ConcurrentE.add(new Thread(Input.lookupTable.get(receiver)));
	}
	 for (Thread update: ConcurrentE){
		 update.start();
	 }
	 for (Thread update: ConcurrentE){
		 try {
			update.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("there is a problem with the thread corresponding to "+ receivers.get(ConcurrentE.indexOf(update)));
		}
	 }
	 System.out.println("finished!");
	
	}
	public  static void sendProcess(String sender,String receiver){
		
	}
}
