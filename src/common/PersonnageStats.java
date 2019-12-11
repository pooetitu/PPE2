package common;

import java.io.Serializable;

public class PersonnageStats implements Serializable {
	private static final long serialVersionUID = 4457644728566684327L;
	private int pv, cd1 = 0, cd2 = 0, cd3 = 0, evite = 1, taunt = 0;
	private int cdUlti = 0;
	private boolean reviveUsed, dead;

	@Override
	public String toString() {
		return "PersonnageStats [pv=" + pv + ", cd1=" + cd1 + ", cd2=" + cd2 + ", cd3=" + cd3 + ", evite=" + evite
				+ ", taunt=" + taunt + ", cdUlti=" + cdUlti + ", reviveUsed=" + reviveUsed + ", dead=" + dead + "]";
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public boolean isReviveUsed() {
		return reviveUsed;
	}

	public void setReviveUsed(boolean used) {
		this.reviveUsed = used;
	}

	public int getCdUlti() {
		return cdUlti;
	}

	public void setCdUlti(int cdUlti) {
		this.cdUlti = cdUlti;
	}

	public PersonnageStats(Personnage p) {
		pv = p.getPvmax();
	}

	public void die() {
		if (pv <= 0) {
			dead = true;
			pv = 0;
		}
	}

	public void coolDown(Personnage p) {
		if (cdUlti >= 1) {
			cdUlti--;
		}
		if (this.cd1 >= 1) {
			cd1--;
		}
		if (this.cd2 >= 1) {
			cd2--;
		}
		if (this.cd3 >= 1) {
			cd3--;
		}
	}

	public void reset(Personnage p) {
		cd1 = 0;
		cd2 = 0;
		cd3 = 0;
		cdUlti = 6;
		taunt = 0;
		evite = 1;
		dead = false;
		reviveUsed = false;
		pv = p.getPvmax();
	}

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public int getCd1() {
		return cd1;
	}

	public void setCd1(int cd1) {
		this.cd1 = cd1;
	}

	public int getCd2() {
		return cd2;
	}

	public void setCd2(int cd2) {
		this.cd2 = cd2;
	}

	public int getCd3() {
		return cd3;
	}

	public void setCd3(int cd3) {
		this.cd3 = cd3;
	}

	public int getEvite() {
		return evite;
	}

	public void setEvite(int evite) {
		this.evite = evite;
	}

	public int getTaunt() {
		return taunt;
	}

	public void setTaunt(int taunt) {
		this.taunt = taunt;
	}

}
