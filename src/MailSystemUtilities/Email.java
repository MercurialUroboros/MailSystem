package MailSystemUtilities;
import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

public class Email implements Serializable{

	private static final AtomicInteger count = new AtomicInteger(0); 
	private final int ID;
	private String sender;
	private String receiver;
	private String subject;
	private String text;
	private java.util.Date sendingDate;
	

	/**
	 * 
	 * @param sender person which is sending
	 * @param receiver person which will receive
	 * @param messageObject messageObject
	 * @param messageText text of the mail
	 * @param sendingDate
	 */
	public Email(String sender , String receiver , String messageObject , String messageText)
	{
		
		ID = count.incrementAndGet();
		this.sender = sender;
		this.receiver = receiver;
		subject = messageObject;
		text = messageText;
		sendingDate = Calendar.getInstance().getTime();
	}
	
	public int getID()
	{
		return ID;
	}
	
	public String getSender ()
	{
		return sender;
	}
	
	public String getReceiver()
	{
		return receiver;
	}
	
	public String getEmailObject()
	{
		return subject;
	}
	
	public String getEmailText()
	{
		return text;
	}
	
	public java.util.Date getSendingDate()
	{
		return sendingDate;
	}
	public String toString()
	{
		return "Sender"+sender+"Receiver:"+receiver+"Subject:"+subject+"Text:"+text;
		
	}
	
	public Object[] toObjectArray() {
	    return new Object[] { sender, receiver,subject,text};
	}
}