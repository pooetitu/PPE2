package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CommandSender {
	private Socket s;
	private ObjectOutputStream oos;

	public Socket getS() {
		return s;
	}

	public CommandSender() {
		try {
			s = new Socket("localhost", 5657);
			oos = new ObjectOutputStream(s.getOutputStream());
			System.out.println("connected" + s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sending(String toSend) {
		if (toSend != null && !toSend.equals("")) {
			try {
				System.out.println("sending" + toSend);
				oos.writeObject(toSend);
				oos.flush();
				toSend = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}