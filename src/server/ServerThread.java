package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Optional;

public class ServerThread extends Thread {
	private static final String NOT_FOUND = "NOT_FOUND";
	private DatagramSocket socket;
	private MulticastSocket mcSocket;
	private InetAddress mcgroup;
	private HashMap<String, String> register;
	private int mcport;
	private int port;
		
	public ServerThread(int servicePort, InetAddress multicastAddress, int multicastPort){
		try {
			this.port = servicePort;
			this.socket = new DatagramSocket(servicePort);
			this.mcSocket = new MulticastSocket(multicastPort);
			this.register = new HashMap<String, String>();
			this.mcgroup = multicastAddress;
			this.mcport = multicastPort;
		} catch (Exception e) {		
			e.printStackTrace();
		}
		
		try {
			
			mcSocket.joinGroup(this.mcgroup);
		} catch (Exception e) {
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
				args[1].equals("25-22-26");
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
	
	private void announce(){
		try {
			String ipaddress = InetAddress.getLocalHost().getHostAddress() + " " +Integer.toString(this.port);
			byte[] buf = ipaddress.getBytes();			
			DatagramPacket announcePacket = new DatagramPacket(buf, buf.length, this.mcgroup, this.mcport);
			this.mcSocket.send(announcePacket);
			System.out.println("multicast: "+ this.mcgroup.getHostAddress() + " " + Integer.toString(this.mcport) +": " + InetAddress.getLocalHost().getHostAddress() + " " + Integer.toString(this.port));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run(){
		
		while(true){
			try{
				
				byte[] buf = new byte[512];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				this.socket.setSoTimeout(1000);
				this.socket.receive(packet);
				int length = packet.getLength();
				
				String request = new String(java.util.Arrays.copyOfRange(packet.getData(), 0, length), "UTF-8");		
				System.out.println(request);
				
				String reply = this.parseMessage(request);
				byte[] bufOut = reply.getBytes();
				DatagramPacket replyPacket = new DatagramPacket(bufOut, bufOut.length, packet.getAddress(), packet.getPort());
				this.socket.send(replyPacket);
			}
			catch(SocketTimeoutException t){
				this.announce();
				continue;
			}
			catch(IOException e){
				break;
			}
		}
	}
}
