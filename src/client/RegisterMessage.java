package client;

public class RegisterMessage implements ClientMessage {
	private String plate;
	private String owner;
	
	public RegisterMessage(String plate, String owner){
		if (!plate.matches("[a-zA-Z0-9]{2}-[a-zA-Z0-9]{2}-[a-zA-Z0-9]{2}]"))
			throw new IllegalArgumentException();
		this.plate = plate;
		this.owner = owner;
	}
		
	@Override
	public byte[] getByteArray() {
		return ("register "+ this.plate + this.owner).getBytes();
	}

}
