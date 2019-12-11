package common;

import java.io.Serializable;
import java.util.Map;

import server.Arene;
import server.commands.ClientConnection;
import server.commands.GameServer;

public abstract class Personnage implements Serializable {
	private static final long serialVersionUID = -5577798614706854110L;
	protected boolean nuker, soigneur, searching = false, playing = false, disconnected = false;
	protected int xp, pvmax, lvl, pc, pcUsed, lvlComp1, lvlComp2, lvlComp3, force;
	protected transient String cmd1 = null, cmd2 = null;
	protected PersonnageStats ps;

	public boolean isDisconnected() {
		return disconnected;
	}

	public void setDisconnected(boolean disconnected) {
		this.disconnected = disconnected;
	}

	public boolean isSearching() {
		return searching;
	}

	public void setSearching(boolean searching) {
		this.searching = searching;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	@Override
	public String toString() {
		return "Personnage [lvl=" + lvl + "nuker=" + nuker + "healer=" + soigneur + "]";
	}

	public boolean isNuker() {
		return nuker;
	}

	public boolean isSoigneur() {
		return soigneur;
	}

	public int getPvmax() {
		return pvmax;
	}

	public void setPvmax(int pvmax) {
		this.pvmax = pvmax;
	}

	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}

	public int getLvl() {
		return lvl;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	public int getPc() {
		return pc;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}

	public int getLvlComp1() {
		return lvlComp1;
	}

	public void setLvlComp1(int lvlComp1) {
		this.lvlComp1 = lvlComp1;
	}

	public int getLvlComp2() {
		return lvlComp2;
	}

	public void setLvlComp2(int lvlComp2) {
		this.lvlComp2 = lvlComp2;
	}

	public int getLvlComp3() {
		return lvlComp3;
	}

	public void setLvlComp3(int lvlComp3) {
		this.lvlComp3 = lvlComp3;
	}

	public int getforce() {
		return force;
	}

	public void setforce(int force) {
		this.force = force;
	}

	public void updateLvl() {
		this.setLvl(this.getXp() / 100);
		this.setPc(this.getXp() / 100 - pcUsed);
	}

	public void lvlUpComp(int comp) {
		if (pc - pcUsed > 0 && comp == 1 && lvlComp1 < 5) {
			System.out.println("comp1");
			this.lvlComp1++;
			this.pcUsed++;
		} else if (pc - pcUsed > 0 && comp == 2 && lvlComp2 < 5) {
			System.out.println("comp2");
			this.lvlComp2++;
			this.pcUsed++;
		} else if (pc - pcUsed > 0 && comp == 3 && lvlComp3 < 5) {
			System.out.println("comp3");
			this.lvlComp3++;
			this.pcUsed++;
		}
	}

	public void lvlUp() {
		lvl = xp / 100;
		pc = lvl - pcUsed;
	}

	public void coolDown() {
		ps.coolDown(this);
	}

	public void startGame(ClientConnection c) throws InterruptedException {
		boolean found = false;
		this.setSearching(true);
		System.out.println("Starting solo queue");
		do {
			Thread.sleep(50);
			for (Map.Entry<String, ?> lc : GameServer.listClient.entrySet()) {
				ClientConnection cc = (ClientConnection) lc.getValue();
				for (int j = 0; j < cc.getC().getNbperso() && this.isSearching(); j++) {
					if (cc.getC().getP(j).isSearching() && cc.getC().getP(j) != this) {
						found = true;
						this.setSearching(false);
						cc.getC().getP(j).setSearching(false);
						GameServer.listGroup.add(new Group(c.getC().getP(c.getC().getSelection()), cc.getC().getP(j),
								c.getC().getPseudo(), cc.getC().getPseudo()));
						for (Group g : GameServer.listGroup) {
							System.out.println(g);
							if (g.getC1().equals(c.getC().getPseudo())) {
								g.startSearch();
								break;
							}
						}
						break;
					} else {
						found = false;
					}
				}
				if(found) {
					break;
				}
			}
		} while (found == false && this.isSearching());
	}

	public void reset() {
		ps.reset(this);
		searching = false;
	}

	public int getPcUsed() {
		return pcUsed;
	}

	public void setPcUsed(int pcUsed) {
		this.pcUsed = pcUsed;
	}

	public int getForce() {
		return force;
	}

	public void setForce(int force) {
		this.force = force;
	}

	public String getCmd1() {
		return cmd1;
	}

	public void setCmd2(String cmd2) {
		this.cmd2 = cmd2;
	}

	public String getCmd2() {
		return cmd2;
	}

	public void setCmd1(String cmd1) {
		this.cmd1 = cmd1;
	}

	public PersonnageStats getPs() {
		return ps;
	}

	public void setPs(PersonnageStats ps) {
		this.ps = ps;
	}

	public abstract void round(Personnage p1, Personnage p2, Personnage p3, Personnage p4, Arene a);

}
