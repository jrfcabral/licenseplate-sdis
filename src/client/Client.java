package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

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
		System.out.println(request);
		
		
		try{
			Socket socket = new Socket(ip,port);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out.println(request);
			socket.shutdownOutput();
			System.out.println(in.readLine());
			socket.close();
			
		}
		catch(IOException e){
			System.out.println("Couldn't open or write to socket");
			return;
		}
			
	}

}
