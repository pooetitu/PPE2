package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import client.state.Game;
import client.state.Lobby;
import client.state.Menu;
import common.Compte;
import common.Group;
import common.Personnage;
import common.PersonnageStats;

public class CommandHandler implements Runnable {
	private ObjectInputStream ois;
	private Socket s;

	public CommandHandler() {
		try {
			s = Launch.getCs().getS();
			if (s != null)
				ois = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			while (!Launch.getCs().getS().isClosed()) {
				String received = "";
				Object o = ois.readObject();
				if (o instanceof String) {
					received = (String) o;
				}
				List<String> cmd = Arrays.asList(received.trim().split("\\s+"));
				if (!cmd.get(0).equals("ERROR") && !cmd.get(0).equals("CONNECTED") && !received.equals("BATTLE END")) {

					o = ois.readObject();
				}
				if (!cmd.get(0).equals("GROUP") && !cmd.get(0).equals("USERS") && !cmd.get(0).equals("COMPTE")) {
					System.out.println(cmd.get(0));
					System.out.println(received);
				}
				switch (cmd.get(0)) {
				case ("ERROR"): {
					switch (cmd.get(1)) {
					case ("CONNECTION"): {
						Menu.setErrString("Utilisateur deja connecte ou inexistant");
						break;
					}
					case ("INSCRIPTION"): {
						Menu.setErrString("Utilisateur deja inscrit");
						break;
					}
					}
					break;
				}
				case ("CONNECTED"): {
					Menu.getGame().enterState(1);
					break;
				}
				case ("COMPTE"): {
					if (o instanceof Compte) {
						Launch.setC((Compte) o);
					}
					break;
				}
				case ("USERS"): {
					if (o instanceof ArrayList) {
						ArrayList<String> a = (ArrayList<String>) o;
						if (Launch.getC() != null) {
							a.remove(Launch.getC().getPseudo());
						}
						Launch.setUserKeys(a);
					}
					break;
				}
				case ("GROUP"): {
					if (!Launch.isPlaying()) {
						if (o instanceof Group) {
							Launch.setG((Group) o);
							Launch.setInGroup(true);
						} else {
							Launch.setG(null);
							Launch.setInGroup(false);
						}
					}
					break;
				}
				case ("MESSAGE"): {
					if (o instanceof String)
						Launch.getMsg().add((String) o);
					break;
				}
				case ("INVITE"): {
					if (o instanceof String) {
						Lobby.setUserKey((String) o);
						Lobby.setInvited(true);
					}
					break;
				}
				case ("SEARCH"): {
					switch (cmd.get(1)) {
					case ("START"): {
						if (o instanceof Boolean) {
							Launch.setSearching((Boolean) o);
						}
						System.out.println(Launch.isSearching());
						break;
					}
					case ("STOP"): {
						if (o instanceof Boolean) {
							Launch.setSearching((Boolean) o);
						}
						System.out.println(Launch.isSearching());
						break;
					}
					}
					break;
				}
				case ("BATTLE"): {
					switch (cmd.get(1)) {
					case ("STATS"): {
						Object o1;
						if (o instanceof String) {
							o1 = ois.readObject();
							search((String) o).setPs((PersonnageStats) o1);
							System.out.println((PersonnageStats) o1);
						}
						o = ois.readObject();
						o1 = ois.readObject();
						if (o instanceof String) {
							search((String) o).setPs((PersonnageStats) o1);
							System.out.println((PersonnageStats) o1);
						}
						o = ois.readObject();
						o1 = ois.readObject();
						if (o instanceof String) {
							search((String) o).setPs((PersonnageStats) o1);
							System.out.println((PersonnageStats) o1);
						}
						o = ois.readObject();
						o1 = ois.readObject();
						if (o instanceof String) {
							search((String) o).setPs((PersonnageStats) o1);
							System.out.println((PersonnageStats) o1);
						}
						System.out.println("Stats received");
						break;
					}
					case ("GROUP"): {
						if (o instanceof Group) {
							Launch.setG2((Group) o);
							if (Launch.getG2() != null) {
								System.out.println(Launch.getG2());
							}
							Launch.setPlaying(true);
							Menu.getGame().enterState(2);
							System.out.println("Game Starting");
						}
						break;
					}
					case ("MAP"): {
						if (o instanceof Integer) {
							Game.setNumMap((Integer) o);
							System.out.println("map number" + Game.getNumMap() + "   " + (Integer) o);
						}
						Game.setStarted(false);
						break;
					}
					case ("TURN"): {
						if (o instanceof Boolean) {
							Game.setWaitingTurn((Boolean) o);
							Game.setTimer(Game.getTime() + 20000);
						}
						break;
					}
					case ("WAIT"): {
						if (o instanceof Boolean) {
							Game.setWaitingTurn((Boolean) o);
							Game.setCompChoice(null);
							Game.setUserSelection(false);
						}
						break;
					}
					case ("END"): {
						Menu.getGame().enterState(1);
						Launch.setPlaying(false);
						break;
					}
					}
					break;
				}
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	private Personnage search(String c) {
		Personnage p = null;
		if (Launch.getG() != null && Launch.getG2() != null) {
			if (c.equals(Launch.getG().getC1())) {
				p = Launch.getG().getP(0);
			} else if (c.equals(Launch.getG().getC2())) {
				p = Launch.getG().getP(1);
			} else if (c.equals(Launch.getG2().getC1())) {
				p = Launch.getG2().getP(0);
			} else if (c.equals(Launch.getG2().getC2())) {
				p = Launch.getG2().getP(1);
			}
			System.out.println(c);
			System.out.println(p);
		}
		return p;
	}

}