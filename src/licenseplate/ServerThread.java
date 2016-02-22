package licenseplate;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Optional;

public class ServerThread extends Thread {
	private DatagramSocket socket;
	private HashMap<String, String> register;
	
	public ServerThread(int port){
		try {
			this.socket = new DatagramSocket(port);
		} catch (SocketException e) {		
			e.printStackTrace();
		}
	}
	
	private boolean validatePlate(String plate){
		return plate.matches("[a-zA-Z0-9]{2}-[a-zA-Z0-9]{2}-[a-zA-Z0-9]{2}]");
	}
	
	private String parseMessage(String message){
		String args[] = message.split(" ");
		if (args.length < 2)			
			return null;
		else if(args[0] == "register" && args.length == 3){
			if (validatePlate(args[1])){
				this.register.put(args[1], args[2]);
				return null;
			}			
			return null;
			
		}
		else if (args[0] == "lookup" && args.length == 2){
			String owner = this.register.get(args[1]);
			if (owner == null)
				return "NOT_FOUND";
			else
				return this.register.size() + "\n" + args[1].toString() + owner;
		}
		else 
			return null;
	}
	
	public void run(){
		
		while(true){
			try{
				byte[] buf = new byte[256];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				this.socket.receive(packet);	
				System.out.println(new String(buf, "UTF-8"));
				Optional<String> reply = Optional.ofNullable(this.parseMessage(buf.toString()));
				String message = reply.orElse("NOT_FOUND");
				DatagramPacket replyPacket = new DatagramPacket(message.getBytes(), message.length(), packet.getAddress(), packet.getPort());
				this.socket.send(replyPacket);
			}
			catch(IOException e){
				break;
			}
		}
	}
}
