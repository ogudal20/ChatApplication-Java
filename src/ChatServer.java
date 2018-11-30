package chatapplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

	
	static ArrayList<String> userNames = new ArrayList<String>();
	
	static ArrayList<PrintWriter> printWriters = new ArrayList<PrintWriter>();
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		System.out.println("Waiting for clients...");
		ServerSocket ss = new ServerSocket(6969);
		while(true){
			Socket soc = ss.accept();
			System.out.println("Connection established");
			ConversationHandler handler = new ConversationHandler(soc);
			handler.start();
		}
		
		
		

	}

}

/*
* Now we create a separate class to handle multiple users simulataneously 
*This is called multithreading. Each time a client establishes a connection
* To the server a new thread is created
*/

class ConversationHandler extends Thread{
	
	Socket socket;	
	BufferedReader in;	
	PrintWriter out;	
	String name;
	PrintWriter pw;	
	static FileWriter fw;	
	static BufferedWriter bw; 
	
	public ConversationHandler(Socket socket) throws IOException{
		this.socket = socket;	
		fw = new FileWriter("/home/sup3rn0va/Documents/ChatServer-Logs.txt", true);
		bw = new BufferedWriter(fw);
		pw = new PrintWriter(bw, true);
	}
	
	//The code inside the run method. Is the one that gets run.
	public void run(){
		
		try{
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);	
		
		
		int count = 0;
		while(true){
			
			
			if(count > 0){ out.println("NAMEALREADYEXISTS");}
			else{ out.println("NAMEREQUIRED");}	
			
			
			name = in.readLine();
			
			
			if(name == null){ return; }
			
			if(!ChatServer.userNames.contains(name)){
				ChatServer.userNames.add(name);
				break;
			}
			
			count++;
			
			
		
			
			
		}
		
		
		//Accept the name and send name to client
		out.println("NAMEACCEPTED"+name);
		ChatServer.printWriters.add(out);
		
		//Now we create a while loop that the server will read a message from the client 
		//and send it to all the other clients.
		while(true){
			
			
			String message = in.readLine();
			
		
			if(message == null){
				return;
				
			}
			
			pw.println(name + ":  " +message);
			for(PrintWriter writer : ChatServer.printWriters){
				writer.println(name + ":  " + message);
			}
			
		}
		
		}catch(Exception e){
			
		}
		
	}
	

	

}

