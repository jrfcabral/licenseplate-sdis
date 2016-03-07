package server;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
//T4G01
public class ServerThread extends Thread {
	private static final String NOT_FOUND = "NOT_FOUND";
	private HashMap<String, String> register;
	private Socket tcpsocket;
	
	public ServerThread(Socket socket){
		this.tcpsocket = socket;
		this.register = new HashMap<String, String>();

	}	
	
	private boolean validatePlate(String plate){
		return plate.matches("[a-zA-Z0-9]{2}-[a-zA-Z0-9]{2}-[a-zA-Z0-9]{2}") && this.register.get(plate) == null;		
	}
	
	private String parseMessage(String message){		
		
		String args[] = message.split(" ");

		if (args.length < 2)			
			return null;
		else if(args[0].equals("register") && args.length == 3){
			if (validatePlate(args[1])){				
				System.out.println("registering " + args[1]);
				Server.db.put(args[1].trim(), args[2]);
				for(String entry: this.register.keySet()){
					System.out.println(entry);
				}
				return Integer.toString(Server.db.size());
			}
			else return Integer.toString(-1);
			
			
		}
		else if (args[0].equals("lookup") && args.length == 2){
			for(String entry: this.register.keySet()){
				System.out.println(entry);
			}
			System.out.println("looking up " + args[1]);
			String owner = Server.db.get(args[1].trim());
			if (owner == null)
				return NOT_FOUND;
			else
				return owner;
			
		}
		else 
			return null;
	}
	
	
	
	public void run(){
		
			try{
				
				PrintWriter out = new PrintWriter(this.tcpsocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(this.tcpsocket.getInputStream()));
				
				String input = in.readLine();
				System.out.println(input);
				String output = this.parseMessage(input);
				out.println(output);
				this.tcpsocket.shutdownOutput();
				this.tcpsocket.close();
			}
			
			catch(IOException e){
				return;
			}
		
	}
}
