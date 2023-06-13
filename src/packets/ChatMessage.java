package packets;

public class ChatMessage {
	private String message;
	private long timeSent;
	private String recipient; //"world", "guild", "whisper"
	public ChatMessage(String message, String recipient){
		this.message = message;
		this.recipient = recipient;
	}
	
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
	public String getRecipient() { return recipient; }
	public void setRecipient(String recipient) { this.recipient = recipient; }
	public long getTimeSent() { return timeSent; }
	public void setTimeSent(long timeSent) { this.timeSent = timeSent; }
}
