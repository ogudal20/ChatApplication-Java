package chatapplication;
import javax.swing.*;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class ChatClient {

	//First thing we will need a JFrame container to hold
	//the all the other containers.
	//All the swing components will left as static as all the instances will be left
	//constant.
	static JFrame chatWindow = new JFrame("Chat Application");
	//Now we add text area for the chat messages to be displayed on.
	//JtextArea contains two parameters one for the row and the other columns
	static JTextArea chatArea = new JTextArea(22, 40);
	//We will need a textfield for the user to enter in the messages.
	//One parameter for columns.
	static JTextField textField = new JTextField(40);
	//Next will have a line to separate the chat area and  textfield.
	static JLabel blankLabel = new JLabel("		");
	//Now we need a send button. To send out message.
	static JButton sendButton = new JButton("Send");
	static BufferedReader in;	//Take input from socket stream.
	static PrintWriter out;	//display output to socket stream.
	static JLabel nameLabel = new JLabel("		");
	
	//Adding components to chat window.
	ChatClient(){
		
		//How are components we be arranged on the JFrame.
		chatWindow.add(nameLabel);
		chatWindow.setLayout(new FlowLayout());
		//Add the scroll bar to the chat area.
		chatWindow.add(new JScrollPane(chatArea));
		chatWindow.add(blankLabel);
		chatWindow.add(textField);
		chatWindow.add(sendButton);
		
		//Close button on GUI.Stops the application
		chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatWindow.setSize(475, 500);	//Width and height
		chatWindow.setVisible(true); //Display visible to true.
		
		//We want only the user to enter text if they have established a connection with the server.
		textField.setEditable(false);
		//the chat area only is there to display messages and not for entering messages.
		chatArea.setEditable(false);
		//Now we bind the send button to the listener.
		//Now when the user clicks send the message will be sent
		//to the server.
		sendButton.addActionListener(new Listener());
		//Now when the user clicks enter the user should still be able to send messages to the server.
		textField.addActionListener(new Listener());
		
		
		
		
		
	}
	
	public void startChat() throws Exception{
		
	 	//Ask the user to enter ip address of the server.
		//The showinputDialog method displays a dialog box.
		//@parameter chatWindow where will display the dialog box, in this case on the chatWindow.
		//@paramter Enter IP Address message to display in the dialog box.
		//@parameter IP Address Required is used for title of the dialog box.
		//@parameter JOptionPane.PLAIN_MESSAGE type message for dialog box.
		String ipAddress = JOptionPane.showInputDialog(
				chatWindow,
				"Enter IP Address: ",
				"IP Address Required: ",
				JOptionPane.PLAIN_MESSAGE);
		
		//Socket object with ipaddress above and port number.
		//connecting to the server.
		Socket soc = new Socket(ipAddress, 6969);
		in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
		out = new PrintWriter(soc.getOutputStream(), true);
		
		//Now create a while loop to wait for incoming messages from the server.
		
		while(true){
			
			//Get the name of the user.
			//First we get capture message the server sends us.
			String str = in.readLine();
			
			//Now we create a dialog box for the user to enter a name.
			if(str.equals("NAMEREQUIRED")){
				String name = JOptionPane.showInputDialog(
						chatWindow,
						"Enter a unique name: ",
						"Name Required!!",
						JOptionPane.PLAIN_MESSAGE);
				
				//Get the name and send it to the server.
				out.println(name);
				
				
				//Now we check when the name already exists.
			}else if(str.equals("NAMEALREADYEXISTS")){
				String name = JOptionPane.showInputDialog(
						chatWindow, 
						"Enter another name: ", 
						"Name Already Exists!!", 
						JOptionPane.WARNING_MESSAGE);
				
				//Now we check for when the name is accepted.
				//startsWith. = This is because the server will send both the name and NAMEACCPEPTED
			}else if(str.startsWith("NAMEACCEPTED")){
				
				//After the name is accepted the user can enter in text into the 
				//message box.
				textField.setEditable(true);
				//Now we will have to separate the name and NAMEACCEPTED
				nameLabel.setText("You are logged in as: " +str.substring(12));
				
			}else{
				
				//Now if string does not equal any of the other values.
				//Then we will assume that the message was from a user.
				chatArea.append(str + "\n");
				
			}
			
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ChatClient client = new ChatClient();
		client.startChat();
		
		
		
	}
}

/* New class for listener when user sends thier message.
*/
class Listener implements ActionListener{
	
	
	/*
	 * @method actionPerformed : when event occurs in this click send action is performed.
	 * @ActionEvent e : event that occurs.
	 */
	@Override
	public void actionPerformed(ActionEvent e){
		//Whatever the user types will be sent to the socket output stream.
		ChatClient.out.println(ChatClient.textField.getText());
		//Then we clear the textfield.
		ChatClient.textField.setText("");
	}
	

