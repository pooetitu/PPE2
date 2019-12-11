package common;

import java.util.concurrent.ThreadLocalRandom;

import server.Arene;

public class Soigneur extends Personnage {
	private static final long serialVersionUID = -3453287062954934820L;

	Soigneur() {
		this.soigneur = true;
		this.lvl = 0;
		this.pvmax = 150;
		this.force = 25;
		this.xp = 0;
		this.pc = 0;
		this.lvlComp1 = 1;
		this.lvlComp2 = 1;
		this.lvlComp3 = 1;
		this.ps = new PersonnageStats(this);
	}

	// comp1
	public void attaque(Personnage p) {
		int atk = ThreadLocalRandom.current().nextInt((int) (this.force - (this.force * 0.25f)),
				(int) (this.force + (this.force * 0.25f) + 1));
		if (p.getLvlComp1() > this.getLvlComp3() + 3) {
			p.getPs().setPv(p.getPs().getPv());
		} else {
			p.getPs().setPv(p.getPs().getPv() - atk * p.getPs().getEvite());
		}
	}

	// comp2
	public void soigne(Personnage p) {
		int soin = ThreadLocalRandom.current().nextInt((int) (this.force - (this.force * 0.45f)),
				(int) (this.force + (this.force * 0.45f) + 1));
		if (p.getPs().getPv() <= p.getPvmax()) {
			p.getPs().setPv(p.getPs().getPv() + soin);
		} else {
			p.getPs().setPv(p.getPvmax());
		}

	}

	// comp3
	public void eviter() {
		this.getPs().setEvite(0);
	}

	// compult
	public void revive(Personnage p) {
		p.getPs().setPv(pvmax / 2);
		p.getPs().setDead(false);
		this.getPs().setReviveUsed(true);
	}

	public void round(Personnage p1, Personnage p2, Personnage p3, Personnage p4, Arene a) {
		this.coolDown();
		for (int i = 0; i < 50 && cmd1 == null; i++) {
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (cmd1 != null)
				switch (cmd1) {
				case ("ATTAQUE"): {
					if (this.getPs().getCd1() <= 0) {
						if (this == p1 || this == p2) {
							switch (cmd2) {
							case ("1"): {
								this.attaque(p3);
								break;
							}
							case ("2"): {
								this.attaque(p4);
								break;
							}
							}
						} else if (this == p3 || this == p4) {
							switch (cmd2) {
							case ("1"): {
								this.attaque(p1);
								break;
							}
							case ("2"): {
								this.attaque(p2);
								break;
							}
							}

						}
						this.getPs().setCd1(3 / this.lvlComp1);
					}
					break;
				}
				case ("SOIN"): {
					if (this.getPs().getCd2() <= 0) {
						if (this == p1 || this == p2) {
							switch (cmd2) {
							case ("1"): {
								this.soigne(p1);
								break;
							}
							case ("2"): {
								this.soigne(p2);
								break;
							}
							}
						} else if (this == p3 || this == p4) {
							switch (cmd2) {
							case ("1"): {
								this.soigne(p3);
								break;
							}
							case ("2"): {
								this.soigne(p4);
								break;
							}
							}

						}
						this.getPs().setCd2(5 / this.lvlComp2);
					}
					break;
				}
				case ("EVITE"): {
					if (this.getPs().getCd3() <= 0) {
						this.eviter();
						this.getPs().setCd3(4 / this.lvlComp3);
					}
					break;
				}
				case ("REVIVE"): {
					if (!this.getPs().isReviveUsed()) {
						if (this == p1) {
							this.revive(p2);
						} else if (this == p2) {
							this.revive(p1);
						} else if (this == p3) {
							this.revive(p4);
						} else if (this == p4) {
							this.revive(p3);
						}
					}
					break;
				}
				case ("PASSE"): {
					System.out.println("passe son tour");
				}
				}
		}
		cmd2 = null;
		cmd1 = null;
	}

}
