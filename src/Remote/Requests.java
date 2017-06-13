package Remote;

import java.awt.HeadlessException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;

import MailSystemUtilities.Email;
import MailSystemUtilities.MailAccount;
import MailSystemUtilities.MailAccountDatabase;
import Server.Server;

public class Requests extends UnicastRemoteObject implements RequestsInterface {

	private static final long serialVersionUID = 1L;
	public String name;
	public static final ArrayList<String> clientList = new ArrayList<String>();
	public static MailAccountDatabase db = MailAccountDatabase.getInstance();

	public Requests(String n) throws RemoteException {
		this.name = n;
	}

	public void setClient(Client c) throws RemoteException {
		clientList.add(c.getClientName());
		refreshServerList();
	}

	void refreshServerList() {
		// Server.connectedClients = getClients();
		Server.refreshTable(getClients(), Calendar.getInstance().getTime());
	}

	public synchronized void delete(Client c, int index) throws RemoteException {

		String sender = c.getClientName();

		MailAccount senderAccount = null;

		for (MailAccount ml : db.getAccountList()) {
			if (ml.getMailAccount().equals(sender)) {
				senderAccount = ml;
			}

		}

		senderAccount.getMessageList().remove(index);
		Server.logArea.append(sender + " deleted a message\n");

	}

	public synchronized void send(ArrayList<Email> m,ClientImpl c) throws RemoteException {
		sendMail(m,c);
	}

	void sendMail(ArrayList<Email> list,ClientImpl c) {
		for (Email m : list) {
			String sender = m.getSender();
			String receiver = m.getReceiver();

			MailAccount senderAccount = null;
			MailAccount receiverAccount = null;

			for (MailAccount ml : db.getAccountList()) {
				if (ml.getMailAccount().equals(sender)) {
					senderAccount = ml;
				}
				if (ml.getMailAccount().equals(receiver)) {
					receiverAccount = ml;
				}
				else
				{
					//checking integrity
					c.trythis();
					c.showErrorMessage("Nessun utente con questo nome");
					return;
				}
			}
			Server.logArea.append(sender + " sent a message to " + receiver + "\n");

			senderAccount.getMessageList().add(m);
			// If you forward a mail to yourself it may occur twice in your
			// inbox
			if (sender.equals(receiver)) {
				return;
			}
			receiverAccount.getMessageList().add(m);
		}

	}

	public ArrayList<String> getClients() {
		return clientList;
	}

	public void clientConnectionWelcome(Client c) throws RemoteException {
		System.out.println(name + " ha accettato il client di " + c.getClientName());
	}

	public ArrayList<Email> requestUserMailList(Client c) throws HeadlessException, RemoteException {
		MailAccount currentAccount = null;
		for (MailAccount ml : db.getAccountList()) {
			if (ml.getMailAccount().equals(c.getClientName())) {
				currentAccount = ml;
			}
		}
		return currentAccount.getMessageList();
	}

	public void destroyClient(Client c) throws RemoteException {
		System.out.println("Oggetto passato " + c.getClientName() + "la lista ha :" + clientList.size() + "oggetti");

		clientList.remove(c.getClientName());

		refreshServerList();
	}







}
