package server;

import java.util.concurrent.ThreadLocalRandom;

import common.Group;
import common.Personnage;
import server.commands.ClientConnection;
import server.commands.GameServer;

public class Arene {
	private int tour;
	private int rndMap;
	private Group g1, g2;
	private Personnage p1, p2, p3, p4;
	private ClientConnection c1, c2, c3, c4;

	public Arene(Group g1, Group g2) {
		this.g1 = g1;
		this.g2 = g2;
		p1 = g1.getP(0);
		p2 = g1.getP(1);
		p3 = g2.getP(0);
		p4 = g2.getP(1);
		c1 = GameServer.listClient.get(g1.getC1());
		c2 = GameServer.listClient.get(g1.getC2());
		c3 = GameServer.listClient.get(g2.getC1());
		c4 = GameServer.listClient.get(g2.getC2());
		tour = 0;
		this.rndMap = ThreadLocalRandom.current().nextInt(0, 4);
		c2.sendObject("BATTLE MAP", rndMap);
		c1.sendObject("BATTLE MAP", rndMap);
		c4.sendObject("BATTLE MAP", rndMap);
		c3.sendObject("BATTLE MAP", rndMap);
	}

	public int getTour() {
		return tour;
	}

	public void setTour(int tour) {
		this.tour = tour;
	}

	public Group getG1() {
		return g1;
	}

	public void setG1(Group g1) {
		this.g1 = g1;
	}

	public Group getG2() {
		return g2;
	}

	public void setG2(Group g2) {
		this.g2 = g2;
	}

	@Override
	public String toString() {
		return "Arene [rndMap=" + rndMap + ", g1=" + g1 + ", g2=" + g2 + "]";
	}

	public void startGame(Group g1, Group g2) {
		boolean end = false;
		while (!end && (p1 != null && p2 != null) && (p3 != null && p4 != null)) {
			System.out.println("Tour grp1");

			// joueur1
			if (!end) {
				tour(p1, p2, p3, p4, g1.getC1(), g1.getC2(), g2.getC1(), g2.getC2());
			}
			// joueur2
			if ((p3.getPs().isDead() && p4.getPs().isDead()) || (p1.getPs().isDead() && p2.getPs().isDead())
					|| (p1.isDisconnected() && p2.isDisconnected()) || (p3.isDisconnected() && p4.isDisconnected())) {
				end = true;
			}
			if (!end) {
				tour(p2, p1, p3, p4, g1.getC2(), g1.getC1(), g2.getC1(), g2.getC2());
			} // joueur3
			if ((p3.getPs().isDead() && p4.getPs().isDead()) || (p1.getPs().isDead() && p2.getPs().isDead())
					|| (p1.isDisconnected() && p2.isDisconnected()) || (p3.isDisconnected() && p4.isDisconnected())) {
				end = true;
			}
			if (!end) {
				tour(p3, p4, p1, p2, g2.getC1(), g1.getC2(), g1.getC1(), g2.getC2());
			}
			// joueur4
			if ((p3.getPs().isDead() && p4.getPs().isDead()) || (p1.getPs().isDead() && p2.getPs().isDead())
					|| (p1.isDisconnected() && p2.isDisconnected()) || (p3.isDisconnected() && p4.isDisconnected())) {
				end = true;
			}
			if (!end) {
				tour(p4, p3, p1, p2, g2.getC2(), g1.getC2(), g2.getC1(), g1.getC1());
			}
			if ((p3.getPs().isDead() && p4.getPs().isDead()) || (p1.getPs().isDead() && p2.getPs().isDead())
					|| (p1.isDisconnected() && p2.isDisconnected()) || (p3.isDisconnected() && p4.isDisconnected())) {
				end = true;
			}

			tour++;
		}
		if (p3.getPs().isDead() && p4.getPs().isDead()) {
			win(p1, p2);
			lose(p3, p4);
		} else if (p1.getPs().isDead() && p2.getPs().isDead()) {
			lose(p1, p2);
			win(p3, p4);
		}
		updateData(p1, p2, p3, p4);
		if (p1.isDisconnected() || p2.isDisconnected()) {
			GameServer.listGroup.remove(g1);
			System.out.println("group left");
		}
		if (p3.isDisconnected() || p4.isDisconnected()) {
			GameServer.listGroup.remove(g2);
			System.out.println("group left");
		}
		end();
		System.out.println("partie fini");
	}

	private void tour(Personnage p1, Personnage p2, Personnage p3, Personnage p4, String c1, String c2, String c3,
			String c4) {
		p1.getPs().setEvite(1);
		p1.getPs().die();
		p2.getPs().die();
		p3.getPs().die();
		p4.getPs().die();
		disconnect();
		sendPS();
		if (p1.getPs().getTaunt() <= tour && !p1.getPs().isDead() && !p1.isDisconnected()) {
			System.out.println("tour " + c1);
			playerTurn(c1, c2, c3, c4);
			p1.round(p1, p2, p3, p4, this);
		}
	}

	private void disconnect() {
		if (p1.isDisconnected()) {
			p1.getPs().setPv(-1);
			p1.getPs().die();
		}
		if (p2.isDisconnected()) {
			p2.getPs().setPv(-1);
			p2.getPs().die();
		}
		if (p3.isDisconnected()) {
			p3.getPs().setPv(-1);
			p3.getPs().die();
		}
		if (p4.isDisconnected()) {
			p4.getPs().setPv(-1);
			p4.getPs().die();
		}
	}

	private void lose(Personnage p1, Personnage p2) {
		dropXp(p1, "l");
		dropXp(p2, "l");
	}

	private void win(Personnage p1, Personnage p2) {
		dropXp(p1, "w");
		dropXp(p2, "w");
	}

	private void dropXp(Personnage p, String statut) {
		if (statut == "w") {
			int rng = ThreadLocalRandom.current().nextInt(60, 90 + 1);
			p.setXp(p.getXp() + rng);
		} else if (statut == "l") {
			int rng = ThreadLocalRandom.current().nextInt(30, 65 + 1);
			p.setXp(p.getXp() + rng);
		}
		p.lvlUp();
	}

	private void updateData(Personnage p1, Personnage p2, Personnage p3, Personnage p4) {
		p1.reset();
		p2.reset();
		p3.reset();
		p4.reset();
	}

	private void playerTurn(String c1, String c2, String c3, String c4) {
		if (GameServer.listClient.get(c1) != null) {
			GameServer.listClient.get(c1).sendObject("BATTLE TURN", false);
		}
		if (GameServer.listClient.get(c2) != null) {
			GameServer.listClient.get(c2).sendObject("BATTLE WAIT", true);
		}
		if (GameServer.listClient.get(c3) != null) {
			GameServer.listClient.get(c3).sendObject("BATTLE WAIT", true);
		}
		if (GameServer.listClient.get(c4) != null) {
			GameServer.listClient.get(c4).sendObject("BATTLE WAIT", true);
		}
	}
//	TODO envoyer log de combat et faire les animations
//	public void sendAct(String pseudo,String action) {
//		String a="BATTLE ACT";
//		c1.sendObject(a,pseudo+" "+action);
//		c2.sendObject(o);
//		c3.sendObject(o);
//		c4.sendObject(o);
//	}

	private void sendPS() {
		if (c1 != null) {
			c1.setSending(true);
			c1.sendObject("BATTLE STATS");
			c1.sendObject(g1.getC1());
			c1.sendObject(p1.getPs());
			c1.sendObject(g1.getC2());
			c1.sendObject(p2.getPs());
			c1.sendObject(g2.getC1());
			c1.sendObject(p3.getPs());
			c1.sendObject(g2.getC2());
			c1.sendObject(p4.getPs());
			c1.setSending(false);
		}
		if (c2 != null) {
			c2.setSending(true);
			c2.sendObject("BATTLE STATS");
			c2.sendObject(g1.getC1());
			c2.sendObject(p1.getPs());
			c2.sendObject(g1.getC2());
			c2.sendObject(p2.getPs());
			c2.sendObject(g2.getC1());
			c2.sendObject(p3.getPs());
			c2.sendObject(g2.getC2());
			c2.sendObject(p4.getPs());
			c2.setSending(false);
		}
		if (c3 != null) {
			c3.setSending(true);
			c3.sendObject("BATTLE STATS");
			c3.sendObject(g1.getC1());
			c3.sendObject(p1.getPs());
			c3.sendObject(g1.getC2());
			c3.sendObject(p2.getPs());
			c3.sendObject(g2.getC1());
			c3.sendObject(p3.getPs());
			c3.sendObject(g2.getC2());
			c3.sendObject(p4.getPs());
			c3.setSending(false);
		}
		if (c4 != null) {
			c4.setSending(true);
			c4.sendObject("BATTLE STATS");
			c4.sendObject(g1.getC1());
			c4.sendObject(p1.getPs());
			c4.sendObject(g1.getC2());
			c4.sendObject(p2.getPs());
			c4.sendObject(g2.getC1());
			c4.sendObject(p3.getPs());
			c4.sendObject(g2.getC2());
			c4.sendObject(p4.getPs());
			c4.setSending(false);
		}
	}

	public void end() {
		c1.sendObject("BATTLE END");
		c1.sendObject("SEARCH STOP", false);
		c1.sendCompte();
		c2.sendObject("BATTLE END");
		c2.sendObject("SEARCH STOP", false);
		c2.sendCompte();
		c3.sendObject("BATTLE END");
		c3.sendObject("SEARCH STOP", false);
		c3.sendCompte();
		c4.sendObject("BATTLE END");
		c4.sendObject("SEARCH STOP", false);
		c4.sendCompte();
	}
}
