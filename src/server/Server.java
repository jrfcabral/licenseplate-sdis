package server;

public class Server {
	

	public static void main(String[] args) {
		ServerThread t = new ServerThread(Integer.parseInt(args[0]));
		t.run();		
	}

}
