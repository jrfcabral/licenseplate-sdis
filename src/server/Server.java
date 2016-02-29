package server;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {
	

	public static void main(String[] args) {
		InetAddress mcgroup;
		try {
			 mcgroup = InetAddress.getByName(args[1]);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		}
		ServerThread t = new ServerThread(Integer.parseInt(args[0]), mcgroup, Integer.parseInt(args[2]));
		t.run();		
	}

}
