package client;

public class LookupMessage implements ClientMessage {
	private String plate;
	public LookupMessage(String plate){
		if (!plate.matches("[a-zA-Z0-9]{2}-[a-zA-Z0-9]{2}-[a-zA-Z0-9]{2}]"))
			throw new IllegalArgumentException();
		
		this.plate = plate;
	}
	
	@Override
	public byte[] getByteArray() {
		return ("lookup " + plate).getBytes(); 
	}

}
