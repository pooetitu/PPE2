package common;

import java.io.Serializable;

import server.Arene;
import server.commands.GameServer;

public class Group implements Serializable {
	private static final long serialVersionUID = -7416516550111949005L;
	private String c1, c2;
	private Personnage p[] = new Personnage[2];
	private boolean inSearch = false, inGame = false, disconnected = false;

	public Group(Personnage p, Personnage p1, String c1, String c2) {
		this.p[0] = p;
		this.p[1] = p1;
		this.c1 = c1;
		this.c2 = c2;
	}

	@Override
	public String toString() {
		return "Group [p=" + p[0] + " p2 = " + p[1] + "c1=" + c1 + "c2=" + c2 + "]";
	}

	public Personnage getP(int x) {
		return p[x];
	}

	public void setP(int x, Personnage p) {
		this.p[x] = p;
	}

	public boolean isInSearch() {
		return inSearch;
	}

	public void setInSearch(boolean inSearch) {
		this.inSearch = inSearch;
	}

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public String getC1() {
		return c1;
	}

	public void setC1(String c1) {
		this.c1 = c1;
	}

	public String getC2() {
		return c2;
	}

	public void setC2(String c2) {
		this.c2 = c2;
	}

	public void startSearch() throws InterruptedException {
		this.setInSearch(true);
		System.out.println("Starting group queue");
		boolean found = false;
		do {
			Thread.sleep(20);
			for (Group g : GameServer.listGroup) {
				if (!g.isInGame() && g.isInSearch() && g != this && g != null && p[0] != null && p[1] != null) {
					g.setInSearch(false);
					this.setInSearch(false);
					System.out.println("Game found");
					GameServer.listClient.get(getC2()).sendGroup();
					GameServer.listClient.get(getC1()).sendGroup();
					GameServer.listClient.get(g.getC2()).sendGroup();
					GameServer.listClient.get(g.getC1()).sendGroup();
					g.setInGame(true);
					this.setInGame(true);
					if (GameServer.listClient.get(getC2()) != null && GameServer.listClient.get(getC1()) != null) {
						GameServer.listClient.get(getC2()).sendObject("BATTLE GROUP",g);
						GameServer.listClient.get(getC1()).sendObject("BATTLE GROUP",g);
						GameServer.listClient.get(g.getC2()).sendObject("BATTLE GROUP",this);
						GameServer.listClient.get(g.getC1()).sendObject("BATTLE GROUP",this);
						Arene a = new Arene(this, g);
						System.out.println("Game started");
						a.startGame(this, g);
					}
					this.setInGame(false);
					g.setInGame(false);
					break;
				}
				if (p[0] == null || p[1] == null) {
					this.setInSearch(false);
				}

			}
			Thread.sleep(20);
		} while (found == false && this.isInSearch());
	}

	public boolean isDisconnected() {
		return disconnected;
	}

	public void setDisconnected(boolean disconnected) {
		this.disconnected = disconnected;
	}

}