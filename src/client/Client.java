package client;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class Client {
	
	
	public static void main(String[] args) {
		InetAddress ip;
		int port;
		try{
			ip = InetAddress.getByName(args[0]);
			port = Integer.parseInt(args[1]);
		}
		catch(Exception e){
			System.out.println("Invalid IP address or port format");
			return;
		}
		
		String request = String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length));		
		byte[] message = request.getBytes();
		System.out.println(request);
		
		try{			
			DatagramSocket socket = new DatagramSocket();
			DatagramPacket packetMessage = new DatagramPacket(message, message.length, ip, port);
			DatagramPacket packetReply = new DatagramPacket(new byte[256], 256);
			socket.send(packetMessage);
			socket.receive(packetReply);
			System.out.println(new String(packetReply.getData(), "UTF-8"));
			socket.close();
			
		}
		catch(Exception e){
			System.out.println("Couldn't open or write to socket");
			return;
		}
			
	}

}
