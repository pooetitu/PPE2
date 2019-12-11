package server.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import common.Compte;
import common.Group;
import server.Arene;

public class ClientConnection implements Runnable {
	private Compte c;
	private Arene a;
	private Socket sock;
	private ObjectOutputStream oos, oos1;
	private ObjectInputStream ois, ois1;
	private boolean sending = false;

	public boolean isSending() {
		return sending;
	}

	public void setSending(boolean sending) {
		this.sending = sending;
	}

	public ClientConnection(Socket pSock) throws IOException {
		sock = pSock;
		oos = new ObjectOutputStream(sock.getOutputStream());
		ois = new ObjectInputStream(sock.getInputStream());
	}

	public Arene getA() {
		return a;
	}

	public void setA(Arene a) {
		this.a = a;
	}

	public void connectionCompte(String pseudo, String mdp) throws SQLException {
		if (GameServer.listClient.get(pseudo) == null) {
			SQL sql = new SQL();
			if (sql.userExist(pseudo, mdp)) {
				int id = sql.userConnection(pseudo, mdp);
				try {
					File file = new File(String.valueOf(id));
					FileInputStream fichier = new FileInputStream(file);
					ois1 = new ObjectInputStream(fichier);
					c = (Compte) ois1.readObject();
					ois1.close();
				} catch (final IOException e) {
					e.printStackTrace();
				} catch (final ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void creationCompte(String pseudo, String mdp) throws SQLException {
		SQL sql = new SQL();
		if (!sql.userExist(pseudo, mdp)) {
			this.c = new Compte(pseudo, mdp);
			try {
				File file = new File(String.valueOf(c.getId()));
				file.createNewFile();
				FileOutputStream fichier = new FileOutputStream(file);
				oos1 = new ObjectOutputStream(fichier);
				oos1.writeObject(c);
				oos1.close();
				fichier.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void run() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					while (!sock.isClosed()) {
						Thread.sleep(2000);
						if (sending = true) {
							Thread.sleep(50);
						}
						if (!sock.isClosed()) {
							ArrayList<String> keys = new ArrayList<String>(GameServer.listClient.keySet());
							sendObject("USERS", keys);
							sendGroup();
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
		System.err.println("Lancement du traitement de la connexion cliente");
		while (!sock.isClosed()) {
			try {
				String request = null;
				request = (String) ois.readObject();
				System.out.println(sock + " " + request);
				List<String> cmd = Arrays.asList(request.trim().split("\\s+"));
				// toSend = "";
				if (cmd.size() >= 0)
					switch (cmd.get(0)) {
					case ("CONNECT"): {
						if (cmd.size() >= 1)
							switch (cmd.get(1)) {
							case ("SIGNIN"): {
								try {
									if (cmd.size() >= 3)
										creationCompte(cmd.get(2), cmd.get(3));
									if (c != null) {
										GameServer.listClient.put(this.c.getPseudo(), this);
										sendObject("CONNECTED");
										sendCompte();
									} else {
										System.out.println("id used");
										sendObject("ERROR INSCRIPTION");
									}
								} catch (SQLException e) {
									sendObject("ERROR INSCRIPTION");
									System.err.println("erreur connection signin");
								}
								break;
							}
							case ("SIGNUP"): {
								try {

									if (cmd.size() > 3)
										connectionCompte(cmd.get(2), cmd.get(3));
									if (c != null) {
										GameServer.listClient.put(String.valueOf(this.c.getPseudo()), this);
										sendObject("CONNECTED");
										sendCompte();
									} else {
										System.out.println("id eroné");
										sendObject("ERROR CONNECTION");
									}
								} catch (SQLException e) {
									sendObject("ERROR CONNECTION");
									System.err.println("erreur connection signup");
								}
								break;
							}
							}
						break;
					}
					case ("MENU"): {
						if (cmd.size() >= 2)
							switch (cmd.get(1)) {
							case ("DELETE"): {
								if (c.getNbperso() > 0) {
									c.delPerso();
									sendCompte();
									sendGroup();
								}
								break;
							}
							case ("COMP"): {
								c.getP(c.getSelection()).lvlUpComp(Integer.parseInt(cmd.get(2)));
								break;
							}
							case ("LEAVING"): {
								for (Group lg : GameServer.listGroup) {
									if (lg.getC1().equals(c.getPseudo()) || lg.getC2().equals(c.getPseudo())) {
										lg.setInSearch(false);
										GameServer.listGroup.remove(lg);
										break;
									}
								}
								break;
							}
							case ("CREATE"): {
								c.creePerso(cmd.get(2));
								sendCompte();
								sendGroup();
								break;
							}
							case ("MESSAGE"): {
								String msg = this.c.getPseudo() + " : ";
								for (int i = 2; i < cmd.size(); i++) {
									msg += cmd.get(i);
									msg += " ";
								}
								for (Map.Entry<String, ?> me : GameServer.listClient.entrySet()) {
									ClientConnection c = (ClientConnection) me.getValue();
									c.sendObject("MESSAGE", msg);
								}
								break;
							}
							case ("INVITE"): {
								GameServer.listClient.get(cmd.get(2)).sendObject("INVITE", this.c.getPseudo());
								break;
							}

							case ("SELECT"): {
								this.c.setSelection(Integer.parseInt(cmd.get(2)));
								sendCompte();
								sendGroup();
								break;
							}
							case ("TARGETED"): {
								String msg = this.c.getPseudo() + " : ";
								for (int i = 4; i < cmd.size(); i++) {
									msg += cmd.get(i);
									msg += " ";
								}
								if (GameServer.listClient.get(cmd.get(3)) != null) {
									GameServer.listClient.get(cmd.get(3)).sendObject("MESSAGE", msg);
								}
								if (cmd.get(2).equals("Y")) {
									GameServer.listGroup.add(new Group(this.c.getP(this.c.getSelection()),
											GameServer.listClient.get(cmd.get(3)).getC()
													.getP(GameServer.listClient.get(cmd.get(3)).getC().getSelection()),
											c.getPseudo(), cmd.get(3)));
									sendGroup();
								}
								break;
							}
							case ("SEARCHING"): {
								ClientConnection cc = this;
								Thread t1 = new Thread(new Runnable() {
									@Override
									public void run() {
										Group g = null;
										for (Group lg : GameServer.listGroup) {
											if (lg.getC1().equals(c.getPseudo()) || lg.getC2().equals(c.getPseudo())) {
												g = lg;
												break;
											}
										}
										if (g != null) {
											try {
												g.startSearch();
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
										} else {
											try {
												if (c.getP(c.getSelection()) != null) {
													c.getP(c.getSelection()).startGame(cc);
												} else {
													sendObject("ERROR PERSO");
												}
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
										}

									}

								});
								switch (cmd.get(2)) {
								case ("START"): {
									t1.start();
									boolean found = false;
									for (Group lg : GameServer.listGroup) {
										if (lg.getC1().equals(c.getPseudo()) || lg.getC2().equals(c.getPseudo())) {
											GameServer.listClient.get(lg.getC1()).sendObject("SEARCH START", true);
											GameServer.listClient.get(lg.getC2()).sendObject("SEARCH START", true);
											found = true;
											break;
										}
									}
									if (!found) {
										sendObject("SEARCH START", true);
									}
									break;
								}
								case ("STOP"): {
									boolean found = false;
									for (Group lg : GameServer.listGroup) {
										if (lg.getC1().equals(c.getPseudo()) || lg.getC2().equals(c.getPseudo())) {
											GameServer.listClient.get(lg.getC1()).getC()
													.getP(GameServer.listClient.get(lg.getC1()).getC().getSelection())
													.setSearching(false);
											GameServer.listClient.get(lg.getC2()).getC()
													.getP(GameServer.listClient.get(lg.getC2()).getC().getSelection())
													.setSearching(false);
											GameServer.listClient.get(lg.getC1()).sendObject("SEARCH STOP", false);
											GameServer.listClient.get(lg.getC2()).sendObject("SEARCH STOP", false);
											found = true;
											break;
										}
									}
									if (!found) {
										if (c.getP(c.getSelection()) != null) {
											c.getP(c.getSelection()).setSearching(false);
										}
										sendObject("SEARCH STOP");
										sendObject(false);
									}
									break;
								}
								}

							}
								break;
							}
					}
					case ("BATTLE"): {
						switch (cmd.get(1)) {
						case ("ACTION"): {

							c.getP(c.getSelection()).setCmd1(cmd.get(2));
							if (cmd.size() > 3)
								c.getP(c.getSelection()).setCmd2(cmd.get(3));
						}
						}
						break;
					}
					}
			} catch (ClassNotFoundException | IOException e) {
				System.err.println("Connection closed !");
				try {
					oos.close();
					ois.close();
					sock.close();
					if (c != null) {
						if (c.getP(0) != null) {
							c.getP(0).setSearching(false);
						}
						if (c.getP(c.getSelection()) != null) {
							c.getP(c.getSelection()).reset();
						}
						c.save();
						for (Group lg : GameServer.listGroup) {
							if (lg.getC1().equals(c.getPseudo()) || lg.getC2().equals(c.getPseudo())) {
								if (lg.isInSearch()) {
									lg.setInSearch(false);
									GameServer.listClient.get(lg.getC1()).getC()
											.getP(GameServer.listClient.get(lg.getC1()).getC().getSelection())
											.setSearching(false);
									GameServer.listClient.get(lg.getC2()).getC()
											.getP(GameServer.listClient.get(lg.getC2()).getC().getSelection())
											.setSearching(false);
									GameServer.listClient.get(lg.getC1()).sendObject("SEARCH STOP", false);
									GameServer.listClient.get(lg.getC2()).sendObject("SEARCH STOP", false);
								}
								if (!lg.isInGame()) {
									GameServer.listGroup.remove(lg);
									System.out.println("Group left");
								} else if (lg.isInGame()) {
									c.getP(c.getSelection()).setDisconnected(true);
								}
								break;
							}
						}
					}
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				if (c != null) {
					GameServer.listClient.remove(this.c.getPseudo());
				}
				break;
			}
		}
	}

	public Compte getC() {
		return c;
	}

	public void setC(Compte c) {
		this.c = c;
	}

	public Socket getSock() {
		return sock;
	}

	public synchronized void sendObject(Object o) {
		try {
			if (!sock.isClosed()) {
				oos.reset();
				oos.writeObject(o);
				oos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void sendObject(Object o, Object o1) {
		try {
			if (!sock.isClosed()) {
				oos.reset();
				oos.writeObject(o);
				oos.flush();
				oos.reset();
				oos.writeObject(o1);
				oos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void sendGroup() {
		boolean found = false;
		if (c != null && !sock.isClosed()) {
			for (Group lg : GameServer.listGroup) {
				if (lg.getC1().equals(c.getPseudo()) && GameServer.listClient.get(lg.getC1()) != null
						&& GameServer.listClient.get(lg.getC2()) != null) {
					if (!lg.isInGame()) {
						lg.setP(0, c.getP(c.getSelection()));
						lg.setP(1, GameServer.listClient.get(lg.getC2()).getC()
								.getP(GameServer.listClient.get(lg.getC2()).getC().getSelection()));
						sendObject("GROUP", lg);
						GameServer.listClient.get(lg.getC2()).sendObject("GROUP", lg);
					}
					found = true;
					break;
				} else if (lg.getC2().equals(c.getPseudo()) && GameServer.listClient.get(lg.getC1()) != null
						&& GameServer.listClient.get(lg.getC2()) != null) {
					if (!lg.isInGame()) {
						lg.setP(0, GameServer.listClient.get(lg.getC1()).getC()
								.getP(GameServer.listClient.get(lg.getC1()).getC().getSelection()));
						lg.setP(1, c.getP(c.getSelection()));
						sendObject("GROUP", lg);
						GameServer.listClient.get(lg.getC1()).sendObject("GROUP", lg);
					}
					found = true;
					break;
				}
			}
		}
		if (!found) {
			sendObject("GROUP", "NULL");
			sendCompte();
		}
	}

	public synchronized void sendCompte() {
		try {
			if (!sock.isClosed()) {
				oos.reset();
				oos.writeObject("COMPTE");
				oos.flush();
				oos.reset();
				oos.writeObject(c);
				oos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}