package chatapplication;
import javax.swing.*;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class ChatClient {


	static JFrame chatWindow = new JFrame("Chat Application");
	static JTextArea chatArea = new JTextArea(22, 40);
	static JTextField textField = new JTextField(40);
	static JLabel blankLabel = new JLabel("		");
	static JButton sendButton = new JButton("Send");
	static BufferedReader in;	
	static PrintWriter out;	
	static JLabel nameLabel = new JLabel("		");
	
	//Adding components to chat window.
	ChatClient(){
		
		
		chatWindow.add(nameLabel);
		chatWindow.setLayout(new FlowLayout());
		chatWindow.add(new JScrollPane(chatArea));
		chatWindow.add(blankLabel);
		chatWindow.add(textField);
		chatWindow.add(sendButton);
		
		chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatWindow.setSize(475, 500);	
		chatWindow.setVisible(true); 
		
		
		textField.setEditable(false);
		chatArea.setEditable(false);
		sendButton.addActionListener(new Listener());
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
		
		
		Socket soc = new Socket(ipAddress, 6969);
		in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
		out = new PrintWriter(soc.getOutputStream(), true);
		
		//Now create a while loop to wait for incoming messages from the server.
		
		while(true){
			
			
			String str = in.readLine();
			
			
			if(str.equals("NAMEREQUIRED")){
				String name = JOptionPane.showInputDialog(
						chatWindow,
						"Enter a unique name: ",
						"Name Required!!",
						JOptionPane.PLAIN_MESSAGE);
				
				
				out.println(name);
				
				
				
			}else if(str.equals("NAMEALREADYEXISTS")){
				String name = JOptionPane.showInputDialog(
						chatWindow, 
						"Enter another name: ", 
						"Name Already Exists!!", 
						JOptionPane.WARNING_MESSAGE);
				
				
				//startsWith. = This is because the server will send both the name and NAMEACCPEPTED
			}else if(str.startsWith("NAMEACCEPTED")){
				
				//After the name is accepted the user can enter in text into the 
				//message box.
				textField.setEditable(true);
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
	

