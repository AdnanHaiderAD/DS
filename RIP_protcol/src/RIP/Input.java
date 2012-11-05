package RIP;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JFileChooser;

import RIP.InputNode.LinkCost;




public class Input<Y> {
static Hashtable<String,InputNode>  lookupTable = new Hashtable<String,InputNode>();
static JFileChooser openfile;
static int thread_num=0;
static int thread_fin =0;
static int linkSend=0;
static int linkRec=0;
static boolean  linkfail=false;
static LinkedList <Tuple<String,String>> linkFail = new LinkedList<Tuple<String,String>>();

public static void main(String[] args){
	 openfile= new JFileChooser();
	 int reval =openfile.showOpenDialog(null);
		 
	 if (reval== JFileChooser.APPROVE_OPTION){
		File input =openfile.getSelectedFile();
				if (input.isDirectory()){
				 		System.out.println("the specified file is a directory :the file must be a text file!");
				 		}
				 try {
					Scanner reader = new Scanner(input);
					while (reader.hasNextLine()){
					String line= reader.nextLine();
					if (!line.isEmpty()){
						String[] commands=line.split(" ");
						if (commands[0].equals((String)"node")){
							 int[] addresses = new int[commands.length-2];
							 for (int i=2;i<commands.length;i++){
							 	addresses[i-2]= Integer.parseInt(  commands[i]);	
							 	}
							 lookupTable.put(commands[1], new InputNode(commands[1],addresses));
							 		    	   
						}else if (commands[0].equals("link")){
							 	if( !lookupTable.containsKey(commands[1]) || !lookupTable.containsKey(commands[2]) ){
							 		     System.out.println("Define these nodes first before specifying the link");
							 	}else{
							       	((InputNode)lookupTable.get(commands[1])).addEntries((InputNode)lookupTable.get(commands[2]));
					 		       	((InputNode)lookupTable.get(commands[2])).addEntries((InputNode)lookupTable.get(commands[1]));
							 	}
							  }else {
								 
								   if (commands[0].equals("send")){
							 	   InputCommand.sendProcess(commands[1]);
							 		}
								    else if (commands[0].equals("link-fail")){
								    	linkFail.add(new Tuple<String, String>(commands[1], commands[2]));
								    	
								     }
								    
							    }
				     }
				     }
				   } catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						System.out.println("file doesnt exist ");
					} 
			
 			while (thread_num!= thread_fin){
 				try {
 					/* This pauses the execution of the link-fail command and waits for the system to be in a stable codition*/
					Thread.currentThread().sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 			}
 			if (linkFail.size()!=0){
 			linkfail=true;	
 			performLinkFailures() ;
 			}
 			while (linkSend!= linkRec){
 				try {
 					/* The main thread waits for the network to reach a stable condition before printing the routing tables*/
					Thread.currentThread().sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 			}	
 			printTable();
			
			}
			else {
				System.exit(0);
			}
	
	 
	}
	
	public static void printTable(){
		for (Enumeration <String> e = lookupTable.keys();e.hasMoreElements();){
			String key = e.nextElement();
			Hashtable<Integer,LinkCost> routingtable = lookupTable.get(key).getRoutingTable();
			String tableRow =" table "+key;
			 for (Enumeration<Integer> ad =routingtable.keys(); ad.hasMoreElements();){
				 
				 Integer address = ad.nextElement();
				 if (routingtable.get(address).cost==Integer.MAX_VALUE){
					 tableRow= tableRow + " "+ "(" + address + " |" + routingtable.get(address).link + " |"+  "i )" ; 
				 }else{
					 tableRow= tableRow + " "+ "(" + address + " |" + routingtable.get(address).link + " |"+ routingtable.get(address).cost + " )" ; 
				 }
			 }
			 System.out.println(tableRow);
		}
	}
	
	public static void performLinkFailures(){
		for (Tuple<String,String> tuple: linkFail){
			lookupTable.get(tuple.x).linkFail(tuple.y);
	    	lookupTable.get(tuple.y).linkFail(tuple.x);
		}
	}
	public static class Tuple<X , Y >{
		X x;
		Y y;
		public Tuple(X x, Y y){
			this.x=x;
			this.y=y;
		}
	}
}
