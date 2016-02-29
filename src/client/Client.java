package client;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Client {
	
	
	public static void main(String[] args) {
		InetAddress ip;
		int port;
		try{
			
		}
		catch(Exception e){
			System.out.println("Invalid IP address or port format");
			return;
		}
		
		String request = String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length));		
		byte[] message = request.getBytes();
		System.out.println(request);
		
		
		try{

			byte[] buf = new byte[512];
			DatagramPacket mcPacket = new DatagramPacket(buf, buf.length);
			MulticastSocket mcSocket = new MulticastSocket(Integer.parseInt(args[1]));
			mcSocket.joinGroup(InetAddress.getByName(args[0]));
			mcSocket.receive(mcPacket);
			System.out.println("received");
			mcSocket.close();
			String mcMessage = new String(mcPacket.getData(), 0, mcPacket.getLength());
			String[] adPort = mcMessage.split(" ");
			ip = InetAddress.getByName(adPort[0]);
			port = Integer.parseInt(adPort[1]);
			
			
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
