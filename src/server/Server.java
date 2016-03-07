package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
	public static ConcurrentHashMap<String, String> db; 

	public static void main(String[] args) {
		Server.db = new ConcurrentHashMap<String,String>();
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
