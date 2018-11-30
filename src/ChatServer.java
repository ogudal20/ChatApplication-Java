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

	//Array lists for the usernames.
	static ArrayList<String> userNames = new ArrayList<String>();
	//When the client sends a message to the server the server has display the message to all the
	//clients. So we will need a printwriter arraylist to store printwriter objects for all the clients.
	static ArrayList<PrintWriter> printWriters = new ArrayList<PrintWriter>();
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		System.out.println("Waiting for clients...");
		ServerSocket ss = new ServerSocket(6969);
		//Now we wait for incoming client connections.
		while(true){
			
			//Now we accept client connections.
			Socket soc = ss.accept();
			System.out.println("Connection established");
			//Now we need to create a thread with the ConversationHandler class.
			ConversationHandler handler = new ConversationHandler(soc);
			//Now we will start this thread.
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
	
	Socket socket;	//Variable to hold the socket for ConvesationalHandler
	BufferedReader in;	//Use for reading in data from socket stream.
	PrintWriter out;	//Used for sending data to outputstream.
	String name;
	PrintWriter pw;	//Used to send data to file.
	static FileWriter fw;	//This FileWriter will write data character by character to a file.
	static BufferedWriter bw; //Writes text to character stream.
	
	public ConversationHandler(Socket socket) throws IOException{
		this.socket = socket;	//Get the accepted connection from the chat server class as a parameter.
		//First we specifiy where we will store all our logs.true boolean is needed so it does not overwrite the 
		//existing content.
		fw = new FileWriter("/home/sup3rn0va/Documents/ChatServer-Logs.txt", true);
		//BufferedWriter to write a entire string to file instead of character.
		bw = new BufferedWriter(fw);
		//Now a PrintWriter to write content to file.
		pw = new PrintWriter(bw, true);
	}
	
	//The code inside the run method. Is the one that gets run.
	public void run(){
		
		try{
		
		//First create a buffer reader is used for reading data from sockets input stream.
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);	//This printwriter is used for sending data to sockets output stream.
		
		//Infinite while loop until the user enters a name.
		int count = 0;
		while(true){
			
			//Check if the name already exists.
			if(count > 0){ out.println("NAMEALREADYEXISTS");}
			else{ out.println("NAMEREQUIRED");}	//Else we require the user to enter a name.
			
			//Set the buffered reader to name variable.
			name = in.readLine();
			
			//Check if the name is null.
			if(name == null){ return; }
			
			//Now add name to username arraylist is it does not exist.
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
			
			//Read a message into the socket input stream.
			String message = in.readLine();
			
			//Check if the message is null.
			if(message == null){
				return;
				
			}
			
			//Now we write the message and the name to our file.
			pw.println(name + ":  " +message);
			//If the message is not null then we will display all the
			//elements in the arraylist.
			for(PrintWriter writer : ChatServer.printWriters){
				writer.println(name + ":  " + message);
			}
			
		}
		
		}catch(Exception e){
			
		}
		
	}
	

	

}

