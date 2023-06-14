package packets;

import java.io.Serializable;

public class ChatMessage implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String message;
	private String sender;
	private long timeRecieved;

	public ChatMessage(String message, String sender){
		this.message = message;
		this.sender = sender;
	}
	
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
	public String getSender() { return sender; }
	public void setSender(String sender) { this.sender = sender; }
	public long getTimeRecieved() { return timeRecieved; }
	public void setTimeRecieved(long timeRecieved) { this.timeRecieved = timeRecieved; }
}
