package RIP;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFileChooser;




public class Input {

	public static void main(String[] args){
		JFileChooser openfile = new JFileChooser();
		int reval =openfile.showOpenDialog(null);
			if (reval== openfile.APPROVE_OPTION){
				 File input =openfile.getSelectedFile();
				 	if (input.isDirectory()){
				 		System.out.println("the specified file is a directory");
				 		}
				 	try {
						Scanner reader = new Scanner(input);
						while (reader.hasNextLine()){
							String line= reader.nextLine();
							String[] lines=line.split(" ");
							System.out.println(lines[1]);
							System.out.println(line);
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						System.out.println("file doesnt exist ");
					}
				 	
			}
	}
}
