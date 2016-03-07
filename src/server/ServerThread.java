package server;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Optional;

public class ServerThread extends Thread {
	private static final String NOT_FOUND = "NOT_FOUND";
	@SuppressWarnings("unused")
	private DatagramSocket socket;
	private MulticastSocket mcSocket;
	private InetAddress mcgroup;
	private HashMap<String, String> register;
	private int mcport;
	private int port;
	private Socket tcpsocket;
	
	public ServerThread(Socket socket){
		this.tcpsocket = socket;
	}
		
	public ServerThread(int servicePort, InetAddress multicastAddress, int multicastPort){
		try {
			this.port = servicePort;
			this.socket = new DatagramSocket(servicePort);
			this.mcSocket = new MulticastSocket(multicastPort);
			this.mcSocket.setTimeToLive(1);
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
	
	@SuppressWarnings("unused")
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
				
				PrintWriter out = new PrintWriter(this.tcpsocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(this.tcpsocket.getInputStream()));
				
				String input = in.readLine();
				String output = this.parseMessage(input);
				out.println(output);
			}
			
			catch(IOException e){
				break;
			}
		}
	}
}
