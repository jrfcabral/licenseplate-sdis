package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
	

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		InetAddress mcgroup;
		try {
			 mcgroup = InetAddress.getByName(args[1]);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		}
		ServerSocket s;
		try {
			s = new ServerSocket(Integer.parseInt(args[0]));
		} catch (NumberFormatException e) {
			return;			
		} catch (IOException e) {			
			return;			
		}
		
		
		while(true){
				Socket ss;
				try {
					ss = s.accept();
					ServerThread t = new ServerThread(ss);
					t.run();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				}
				
		}
		
		
	}

}
