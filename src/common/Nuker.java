package common;

import java.util.concurrent.ThreadLocalRandom;

import server.Arene;

public class Nuker extends Personnage {
	private static final long serialVersionUID = 6276744828014753338L;

	public void reset() {
		super.reset();
	}

	public Nuker() {
		this.nuker = true;
		this.lvl = 0;
		this.pvmax = 250;
		this.force = 40;
		this.xp = 0;
		this.pc = 0;
		this.lvlComp1 = 1;
		this.lvlComp2 = 1;
		this.lvlComp3 = 1;
		this.ps = new PersonnageStats(this);
	}

	public void attaque(Personnage p) {
		int atk = ThreadLocalRandom.current().nextInt((int) (this.force - (this.force * 0.15f)),
				(int) (this.force + (this.force * 0.15f) + 1));
		p.getPs().setPv(p.getPs().getPv() - atk * p.getPs().getEvite());
	}

	public void attaqueUlt(Personnage p) {
		int atk = ThreadLocalRandom.current().nextInt((int) ((this.force + 50) - ((this.force + 50) * 0.1f)), (int)(this.force + 50
				+ ((this.force + 50) * 0.1f)) + 1);
		p.getPs().setPv(p.getPs().getPv() - atk * p.getPs().getEvite());
	}

	public void control(Personnage p, Arene a) {
		p.getPs().setTaunt(a.getTour() + 3);
	}

	public void eviter() {
		this.getPs().setEvite(0);
	}

	public void round(Personnage p1, Personnage p2, Personnage p3, Personnage p4, Arene a) {
		this.coolDown();
		if (this.getPs().getCdUlti() > 0) {
			this.getPs().setCdUlti(getPs().getCdUlti() - 1);
		}
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
						this.getPs().setCd1(4 / this.lvlComp1);
					}
					break;
				}
				case ("CONTROLE"): {
					if (this.getPs().getCd2() <= 0) {
						if (this == p1 || this == p2) {
							switch (cmd2) {
							case ("1"): {
								this.control(p3, a);
								break;
							}
							case ("2"): {
								this.control(p4, a);
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
						this.getPs().setCd2(5 / this.lvlComp2);
					}
					break;
				}
				case ("EVITE"): {
					if (this.getPs().getCd3() <= 0) {
						this.eviter();
						this.getPs().setCd3(3 / this.lvlComp3);
					}
					break;
				}
				case ("ATTAQUEULT"): {
					if (this.getPs().getCdUlti() <= 0) {
						if (this == p1 || this == p2) {
							switch (cmd2) {
							case ("1"): {
								this.attaqueUlt(p3);
								break;
							}
							case ("2"): {
								this.attaqueUlt(p4);
								break;
							}
							}
						} else if (this == p3 || this == p4) {
							switch (cmd2) {
							case ("1"): {
								this.attaqueUlt(p1);
								break;
							}
							case ("2"): {
								this.attaqueUlt(p2);
								break;
							}
							}
						}
						this.getPs().setCdUlti(8);
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