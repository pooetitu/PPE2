package client.state;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import client.Launch;

public class Game extends BasicGameState implements MouseListener {
	private static boolean userSelection, started;
	private int toWait, grpIndex;
	private static int timer, time, numMap;
	private static boolean waitingTurn;
	private static String compChoice, errString;
	private Image nuker, healer;
	private SpriteSheet map1 = null;
	private Animation map = null;

	public static boolean isUserSelection() {
		return userSelection;
	}

	public static void setUserSelection(boolean userSelection) {
		Game.userSelection = userSelection;
	}

	public static String getCompChoice() {
		return compChoice;
	}

	public static void setCompChoice(String compChoice) {
		Game.compChoice = compChoice;
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		nuker = new Image("res/characters/goku.png");
		healer = new Image("res/characters/vegeta.png");

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		if (map != null) {
			g.drawAnimation(map, 0, 0);
			g.setColor(new Color(0f, 0f, 0f, 0.92f));
			g.fillRect(0, 500, 1200, 100);
		}
		// affichage des persos
		// affichage santé groupe ennemi
		g.setColor(Color.white);
		g.drawString(Launch.getG2().getC1() + "  " + Launch.getG2().getP(0).getPs().getPv(), 990, 340);
		g.drawString(Launch.getG2().getC2() + "  " + Launch.getG2().getP(1).getPs().getPv(), 820, 340);
		if (!Launch.getG().getC1().equals(Launch.getC().getPseudo())) {
			// affichage santé groupe allié
			g.drawString(Launch.getG().getC2() + "  " + Launch.getG().getP(1).getPs().getPv(), 300, 340);
			g.drawString(Launch.getG().getC1() + "  " + Launch.getG().getP(0).getPs().getPv(), 160, 340);
			grpIndex = 1;
			// affichage perso allié
			if (Launch.getG().getP(1) != null) {
				if (Launch.getG().getP(1).isNuker()) {
					g.drawImage(nuker.getScaledCopy(0.38f), 250, 350);
				} else if (Launch.getG().getP(1).isSoigneur()) {
					g.drawImage(healer.getFlippedCopy(true, false), 250, 380);
				}

			}
			if (Launch.getG().getP(0) != null) {
				if (Launch.getG().getP(0).isNuker()) {
					g.drawImage(nuker.getScaledCopy(0.38f), 110, 350);
				} else if (Launch.getG().getP(0).isSoigneur()) {
					g.drawImage(healer.getFlippedCopy(true, false), 130, 380);
				}
			}
		} else if (!Launch.getG().getC2().equals(Launch.getC().getPseudo())) {
			g.drawString(Launch.getG().getC1() + "  " + Launch.getG().getP(0).getPs().getPv(), 300, 340);
			g.drawString(Launch.getG().getC2() + "  " + Launch.getG().getP(1).getPs().getPv(), 160, 340);
			grpIndex = 0;
			if (Launch.getG().getP(0) != null) {
				if (Launch.getG().getP(0).isNuker()) {
					g.drawImage(nuker.getScaledCopy(0.38f), 250, 350);
				} else if (Launch.getG().getP(0).isSoigneur()) {
					g.drawImage(healer.getFlippedCopy(true, false), 250, 380);
				}
			}
			if (Launch.getG().getP(1) != null) {
				if (Launch.getG().getP(1).isNuker()) {
					g.drawImage(nuker.getScaledCopy(0.38f), 110, 350);
				} else if (Launch.getG().getP(1).isSoigneur()) {
					g.drawImage(healer.getFlippedCopy(true, false), 130, 380);
				}
			}
		}
		if (Launch.getG2().getP(1) != null) {
			if (Launch.getG2().getP(1).isNuker()) {
				g.drawImage(nuker.getScaledCopy(0.38f).getFlippedCopy(true, false), 790, 350);
			} else if (Launch.getG2().getP(1).isSoigneur()) {
				g.drawImage(healer, 790, 380);
			}

		}
		if (Launch.getG2().getP(0) != null) {
			if (Launch.getG2().getP(0).isNuker()) {
				g.drawImage(nuker.getScaledCopy(0.38f).getFlippedCopy(true, false), 960, 350);
			} else if (Launch.getG2().getP(0).isSoigneur()) {
				g.drawImage(healer, 930, 380);
			}
		}

		// Affichage des attaques
		if (!waitingTurn) {
			g.setColor(Color.white);
			int t = (timer - time) / 1000;
			g.drawString("" + t, 590, 25);
			if (!userSelection) {
				if (Launch.getG() != null) {
					g.fillRect(150, 540, 130, 30);
					g.fillRect(290, 540, 130, 30);
					g.fillRect(430, 540, 130, 30);
					g.fillRect(570, 540, 130, 30);
					g.fillRect(710, 540, 130, 30);
					g.setColor(Color.black);
					if (Launch.getG().getP(grpIndex).isNuker()) {
						if (!Launch.getG().getP(grpIndex).getPs().isDead()) {
							if (Launch.getG().getP(grpIndex).getPs().getCd1() <= 0) {
								g.drawString("Attaquer", 170, 545);
							}
							if (Launch.getG().getP(grpIndex).getPs().getCd2() <= 0) {
								g.drawString("Control", 310, 545);
							}
							if (Launch.getG().getP(grpIndex).getPs().getCd3() <= 0) {
								g.drawString("Evite", 460, 545);
							}
							if (Launch.getG().getP(grpIndex).getPs().getCdUlti() <= 0) {
								g.drawString("Attaque ultime", 575, 545);
							}
						}
					} else if (Launch.getG().getP(grpIndex).isSoigneur()) {
						if (!Launch.getG().getP(grpIndex).getPs().isDead()) {
							if (Launch.getG().getP(grpIndex).getPs().getCd1() <= 0) {
								g.drawString("Attaquer", 170, 545);
							}
							if (Launch.getG().getP(grpIndex).getPs().getCd2() <= 0) {
								g.drawString("Soigner", 310, 545);
							}
							if (Launch.getG().getP(grpIndex).getPs().getCd3() <= 0) {
								g.drawString("Eviter", 460, 545);
							}
							if (!Launch.getG().getP(grpIndex).getPs().isReviveUsed()) {
								g.drawString("Revive", 575, 545);
							}
						}
					}
					g.drawString("Passer", 725, 545);
				}
			} else {
				if (compChoice.equals("SOIN") || compChoice.equals("REVIVE")) {
					g.setColor(Color.white);
					g.fillRect(590, 540, 130, 30);
					g.setColor(Color.black);
					g.drawString(Launch.getG().getC1(), 630, 545);
					g.setColor(Color.white);
					g.fillRect(450, 540, 130, 30);
					g.setColor(Color.black);
					g.drawString(Launch.getG().getC2(), 480, 545);
				} else {
					if (!Launch.getG2().getP(0).getPs().isDead()) {
						g.setColor(Color.white);
						g.fillRect(590, 540, 130, 30);
						g.setColor(Color.black);
						g.drawString(Launch.getG2().getC1(), 630, 545);
					}
					if (!Launch.getG2().getP(1).getPs().isDead()) {
						g.setColor(Color.white);
						g.fillRect(450, 540, 130, 30);
						g.setColor(Color.black);
						g.drawString(Launch.getG2().getC2(), 480, 545);
					}
				}
			}
		}
		if (!userSelection) {
			g.setColor(Color.red);
			if (errString != null) {
				if (time < toWait) {
					g.drawString(errString, 400, 525);
				} else {
					errString = null;
				}
			}
		}
		g.setColor(Color.white);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if (!started) {
			started = true;
			setMap();
		}
		time += delta;
	}

	@Override
	public int getID() {
		return 2;
	}

	public static int getNumMap() {
		return numMap;
	}

	public static void setNumMap(int numMap) {
		Game.numMap = numMap;
	}

	public static boolean isWaitingTurn() {
		return waitingTurn;
	}

	public static void setWaitingTurn(boolean waitingTurn) {
		Game.waitingTurn = waitingTurn;
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		if (button == 0) {
			if (!waitingTurn) {
				if (!userSelection) {
					if (Launch.getC().getP(Launch.getC().getSelection()).isNuker()) {
						if (!Launch.getC().getP(Launch.getC().getSelection()).getPs().isDead()) {
							if (x > 150 && x < 280 && y > 540 && y < 570) {
								if (Launch.getG().getP(grpIndex).getPs().getCd1() <= 0) {
									userSelection = true;
									compChoice = "ATTAQUE";
								} else {
									errString = "Veuillez attendre " + Launch.getG().getP(grpIndex).getPs().getCd1()
											+ " tour avant de reutiliser cet compétence";
									toWait = time + 2000;
								}
							}
							if (x > 290 && x < 420 && y > 540 && y < 570) {
								if (Launch.getG().getP(grpIndex).getPs().getCd2() <= 0) {
									userSelection = true;
									compChoice = "CONTROLE";
								} else {
									errString = "Veuillez attendre " + Launch.getG().getP(grpIndex).getPs().getCd2()
											+ " tour avant de reutiliser cet compétence";
									toWait = time + 2000;
								}

							}
							if (x > 430 && x < 560 && y > 540 && y < 570) {
								if (Launch.getG().getP(grpIndex).getPs().getCd3() <= 0) {
									Launch.getCs().sending("BATTLE ACTION EVITE");
								} else {
									errString = "Veuillez attendre " + Launch.getG().getP(grpIndex).getPs().getCd3()
											+ " tour avant de reutiliser cet compétence";
									toWait = time + 2000;
								}
							}
							if (x > 570 && x < 700 && y > 540 && y < 570) {
								if (Launch.getG().getP(grpIndex).getPs().getCdUlti() <= 0) {
									userSelection = true;
									compChoice = "ATTAQUEULT";
								}
							} else {
								errString = "Veuillez attendre " + Launch.getG().getP(grpIndex).getPs().getCdUlti()
										+ " tour avant de reutiliser cet compétence";
								toWait = time + 2000;
							}
						}
					} else if (Launch.getG().getP(grpIndex).isSoigneur()) {
						if (!Launch.getG().getP(grpIndex).getPs().isDead()) {
							if (x > 150 && x < 280 && y > 540 && y < 570) {
								if (Launch.getG().getP(grpIndex).getPs().getCd1() <= 0) {
									userSelection = true;
									compChoice = "ATTAQUE";
								} else {
									errString = "Veuillez attendre " + Launch.getG().getP(grpIndex).getPs().getCd1()
											+ " tour avant de reutiliser cet compétence";
									toWait = time + 2000;
								}
							}
							if (x > 290 && x < 420 && y > 540 && y < 570) {
								if (Launch.getG().getP(grpIndex).getPs().getCd2() <= 0) {
									userSelection = true;
									compChoice = "SOIN";
								} else {
									errString = "Veuillez attendre " + Launch.getG().getP(grpIndex).getPs().getCd2()
											+ " tour avant de reutiliser cet compétence";
									toWait = time + 2000;
								}
							}
							if (x > 430 && x < 560 && y > 540 && y < 570) {
								if (Launch.getG().getP(grpIndex).getPs().getCd3() <= 0) {
									Launch.getCs().sending("BATTLE ACTION  EVITE");
								} else {
									errString = "Veuillez attendre " + Launch.getG().getP(grpIndex).getPs().getCd3()
											+ " tour avant de reutiliser cet compétence";
									toWait = time + 2000;
								}
							}
							if (x > 570 && x < 700 && y > 540 && y < 570) {
								if (!Launch.getG().getP(grpIndex).getPs().isReviveUsed()) {
									userSelection = true;
									compChoice = "REVIVE";
								} else {
									errString = "Vous ne pouvez utiliser cet compétence qu'une seule fois";
									toWait = time + 2000;
								}
							}
						}
					}
					if (x > 710 && x < 840 && y > 540 && y < 570) {
						Launch.getCs().sending("BATTLE ACTION PASSE");
					}
				} else {
					if (x > 590 && x < 720 && y > 540 && y < 570) {
						userSelection = false;
						Launch.getCs().sending("BATTLE ACTION " + compChoice + " " + 1);
						compChoice = null;
					}
					if (x > 450 && x < 580 && y > 540 && y < 570) {
						userSelection = false;
						Launch.getCs().sending("BATTLE ACTION " + compChoice + " " + 2);
						compChoice = null;
					}
				}
			}
		}
	}

	public static int getTimer() {
		return timer;
	}

	public static void setTimer(int timer) {
		Game.timer = timer;
	}

	public void setMap() {
		try {
			System.out.println("NumMap:" + numMap);
			if (numMap == 0) {
				map1 = new SpriteSheet("res/back/game/fond1.png", 9600 / 8, 600);
			} else if (numMap == 1) {
				map1 = new SpriteSheet("res/back/game/fond2.png", 4800 / 4, 600);
			} else if (numMap == 2) {
				map1 = new SpriteSheet("res/back/fond_menu3.png", 14400 / 12, 2025 / 3);
			} else if (numMap == 3) {
				map1 = new SpriteSheet("res/back/game/fond4.png", 12000 / 10, 600);
			}
			map = new Animation(map1, 100);
			System.out.println("map set" + numMap);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public static int getTime() {
		return time;
	}

	public static void setTime(int time) {
		Game.time = time;
	}

	public static boolean isStarted() {
		return started;
	}

	public static void setStarted(boolean started) {
		Game.started = started;
	}

}
