package server.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.Group;
import server.Arene;

public class GameServer {
	public static List<Group> listGroup = new ArrayList<Group>();
	public static HashMap<String, ClientConnection> listClient = new HashMap<String, ClientConnection>();
	public static List<Arene> listArene = new ArrayList<Arene>();
	private ServerSocket ss = null;
	private final static int port = 5657;
	private final static String host = "127.0.0.1";
	private boolean running = true;

	public GameServer() {

		try {
			ss = new ServerSocket(port, 20, InetAddress.getByName(host));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void open() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				while (running == true) {
					try {
						Socket client = ss.accept();
						System.out.println("Connexion cliente reçue.");
						ClientConnection cl = new ClientConnection(client);
						Thread t = new Thread(cl);
						t.start();
						System.out.println(listClient.size());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					ss.close();
				} catch (IOException e) {
					e.printStackTrace();
					ss = null;
				}
			}
		});
		t.start();
	}
}
