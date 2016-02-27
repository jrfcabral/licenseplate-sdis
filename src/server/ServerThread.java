package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Optional;

public class ServerThread extends Thread {
	private static final String NOT_FOUND = "NOT_FOUND";
	private DatagramSocket socket;
	private HashMap<String, String> register;
	
	public ServerThread(int port){
		try {
			this.socket = new DatagramSocket(port);
			this.register = new HashMap<String, String>();
		} catch (SocketException e) {		
			e.printStackTrace();
		}
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
				this.register.put(args[1], args[2]);
				System.out.println(this.register.get(args[1]));
				boolean value = args[1].equals("25-22-26");
				return Integer.toString(register.size());
			}
			else return Integer.toString(-1);
			
			
		}
		else if (args[0].equals("lookup") && args.length == 2){
			Optional<String> owner = Optional.ofNullable(this.register.get(args[1]));
			return owner.orElse(NOT_FOUND);
			
		}
		else 
			return null;
	}
	
	public void run(){
		
		while(true){
			try{
				byte[] buf = new byte[512];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				this.socket.receive(packet);
				int length = packet.getLength();
				
				String request = new String(java.util.Arrays.copyOfRange(packet.getData(), 0, length), "UTF-8");		
				System.out.println(request);
				
				String reply = this.parseMessage(request);
				byte[] bufOut = reply.getBytes();
				DatagramPacket replyPacket = new DatagramPacket(bufOut, bufOut.length, packet.getAddress(), packet.getPort());
				this.socket.send(replyPacket);
			}
			catch(IOException e){
				break;
			}
		}
	}
}
