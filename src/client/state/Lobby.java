package client.state;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import client.Launch;

public class Lobby extends BasicGameState implements KeyListener, MouseListener {
	private Image back, play, stop, charac, goku, vegeta;
	private static String userKey;
	private boolean characMenu = false;
	private static boolean invited = false;
	private int selection, userSelection = -1;
	private TextField tf;
	static int scroll = 1;

	public static void setUserKey(String userKey) {
		Lobby.userKey = userKey;
	}

	public static void setInvited(boolean invited) {
		Lobby.invited = invited;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		tf = new TextField(container, container.getDefaultFont(), 900, 530, 200, 20);
		tf.setBorderColor(Color.yellow);
		tf.setTextColor(Color.yellow);
		tf.setBackgroundColor(Color.magenta);
		tf.setMaxLength(20);
		tf.setFocus(false);
		back = new Image("res/back/main_back.jpg");
		play = new Image("res/buttons/lobby/button_jouer.png");
		stop = new Image("res/buttons/lobby/button_stop.png");
		charac = new Image("res/buttons/lobby/button_personnage.png");
		goku = new Image("res/characters/goku.png");
		vegeta = new Image("res/characters/vegeta.png");
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.drawImage(back.getScaledCopy(0.63f), 0, -50);

		// chat
		tf.render(container, g);
		g.setColor(new Color(0.31372549f, 0.0823529412f, 0.611764706f, 0.6f));
		g.fillRect(900, 250, 300, 280);
		g.setColor(new Color(0.31372549f, 0.0823529412f, 0.611764706f));
		g.fillRect(1100, 530, 100, 20);
		g.setColor(Color.yellow);
		g.drawString("Envoyer", 1120, 532);
		g.setColor(Color.white);
		int j = 0;
		if (Launch.getC() != null) {
			if (Launch.getMsg().size() < 19) {
				for (int i = Launch.getMsg().size() - 1; i >= 0; i--) {
					j++;
					g.drawString(Launch.getMsg().get(i), 905, 525 - 15 * j);
				}
			} else {
				for (int i = Launch.getMsg().size() - scroll; i > Launch.getMsg().size() - 18 - scroll; i--) {
					j++;
					g.drawString(Launch.getMsg().get(i), 905, 525 - 15 * j);
				}
			}

			// MainBar
			g.setColor(new Color(0f, 0f, 0f, 0.92f));
			g.fillRect(0, container.getHeight() - 50, 1200, 50);
			g.drawImage(charac, 340, 555);

			if (Launch.isSearching()) {
				g.drawImage(stop, 520, 555);
			} else {
				g.drawImage(play, 520, 555);
			}
			// Affiche menu selection perso
			if (characMenu) {
				g.fillRect(300, 50, 600, 500);
				g.setColor(Color.white);
				g.drawString("Selectionner votre personnage", 465, 50);

				g.setColor(new Color(0f, 0f, 0f, 0.92f));
				g.setColor(new Color(0.31372549f, 0.0823529412f, 0.611764706f, 0.4f));
				if (Launch.getC().getP(Launch.getC().getSelection()) != null) {
					g.fillRect(530, 150, 140, 30);
					g.fillRect(530, 270, 140, 30);
					g.fillRect(530, 310, 140, 30);
					g.fillRect(530, 350, 140, 30);
				}
				if (selection == 0) {
					if (Launch.getC().getP(0) != null)
						g.fillRoundRect(355, 70, 155, 185, 20);
				} else if (selection == 1) {
					if (Launch.getC().getP(1) != null)
						g.fillRoundRect(680, 70, 155, 185, 20);
				}
				g.setColor(new Color(0.0980392157f, 0.341176471f, 0.619607843f, 0.5f));
				if (selection == 0) {
					if (Launch.getC().getP(0) != null)
						g.fillRoundRect(360, 75, 145, 175, 15);
				} else if (selection == 1) {
					if (Launch.getC().getP(1) != null)
						g.fillRoundRect(685, 75, 145, 175, 15);
				}
				// affichage des stats des persos
				if (selection == 0) {
					if (Launch.getC().getP(0) != null) {
						int pc = Launch.getC().getP(0).getPc() - Launch.getC().getP(0).getPcUsed();
						g.drawString("niveau : " + Launch.getC().getP(0).getLvl(), 350, 270);
						g.drawString("experience : " + Launch.getC().getP(0).getXp(), 350, 285);
						if (Launch.getC().getP(0).isNuker()) {
							g.drawString("Niveau attaque : " + Launch.getC().getP(0).getLvlComp1(), 350, 300);
							g.drawString("Niveau controle : " + Launch.getC().getP(0).getLvlComp2(), 350, 315);
							g.drawString("Niveau evite : " + Launch.getC().getP(0).getLvlComp3(), 350, 330);
							g.drawString("Lvlup Attaque", 540, 275);
							g.drawString("Lvlup Controle", 535, 315);
							g.drawString("Lvlup Evite", 550, 355);
						}
						if (Launch.getC().getP(0).isSoigneur()) {
							g.drawString("Niveau attaque : " + Launch.getC().getP(0).getLvlComp1(), 350, 300);
							g.drawString("Niveau soin : " + Launch.getC().getP(0).getLvlComp2(), 350, 315);
							g.drawString("Niveau evite : " + Launch.getC().getP(0).getLvlComp3(), 350, 330);
							g.drawString("Lvlup Attaque", 540, 275);
							g.drawString("Lvlup Soin", 550, 315);
							g.drawString("Lvlup Evite", 550, 355);
						}
						g.drawString("pc : " + pc, 350, 345);
						g.drawString("pv : " + Launch.getC().getP(0).getPs().getPv(), 350, 360);
						g.drawString("force : " + Launch.getC().getP(0).getforce(), 350, 375);
					}
				} else if (selection == 1) {
					if (Launch.getC().getP(1) != null) {
						int pc = Launch.getC().getP(1).getPc() - Launch.getC().getP(1).getPcUsed();
						g.drawString("niveau : " + Launch.getC().getP(1).getLvl(), 350, 270);
						g.drawString("experience : " + Launch.getC().getP(1).getXp(), 350, 285);
						if (Launch.getC().getP(1).isNuker()) {
							g.drawString("Niveau attaque : " + Launch.getC().getP(1).getLvlComp1(), 350, 300);
							g.drawString("Niveau controle : " + Launch.getC().getP(1).getLvlComp2(), 350, 315);
							g.drawString("Niveau evite : " + Launch.getC().getP(1).getLvlComp3(), 350, 330);
							g.drawString("Lvlup Attaque", 540, 275);
							g.drawString("Lvlup Controle", 535, 315);
							g.drawString("Lvlup Evite", 550, 355);
						}
						if (Launch.getC().getP(1).isSoigneur()) {
							g.drawString("Niveau attaque : " + Launch.getC().getP(1).getLvlComp1(), 350, 300);
							g.drawString("Niveau soin : " + Launch.getC().getP(1).getLvlComp2(), 350, 315);
							g.drawString("Niveau evite : " + Launch.getC().getP(1).getLvlComp3(), 350, 330);
							g.drawString("Lvlup Attaque", 540, 275);
							g.drawString("Lvlup Soin", 550, 315);
							g.drawString("Lvlup Evite", 550, 355);
						}
						g.drawString("pc : " + pc, 350, 345);
						g.drawString("pv : " + Launch.getC().getP(1).getPs().getPv(), 350, 360);
						g.drawString("force : " + Launch.getC().getP(1).getforce(), 350, 375);
					}
				}
				if (Launch.getC().getP(Launch.getC().getSelection()) != null) {
					g.drawString("Supprimer Perso", 533, 155);
				}
				// boutons creations persos
				g.setColor(Color.magenta);
				g.fillRect(320, 510, 100, 25);
				g.fillRect(780, 510, 100, 25);
				g.setColor(Color.yellow);
				g.drawString("New Nuker", 325, 513);
				g.drawString("New Healer", 785, 513);
				// affichage des persos
				if (Launch.getC().getP(0) != null) {
					if (Launch.getC().getP(0).isNuker()) {
						g.drawImage(goku.getScaledCopy(0.38f), 350, 80);
					} else if (Launch.getC().getP(0).isSoigneur()) {
						g.drawImage(vegeta.getFlippedCopy(true, false), 350, 110);
					}
				}
				if (Launch.getC().getP(1) != null) {
					if (Launch.getC().getP(1).isNuker()) {
						g.drawImage(goku.getScaledCopy(0.38f).getFlippedCopy(true, false), 675, 80);
					} else if (Launch.getC().getP(1).isSoigneur()) {
						g.drawImage(vegeta, 675, 110);
					}
				}
			}
			// affichage du perso selectionner sur le menu principal
			else {
				if (selection == 0) {
					if (Launch.getC().getP(0) != null) {
						if (Launch.getC().getP(0).isNuker()) {
							g.drawImage(goku.getScaledCopy(0.38f).getFlippedCopy(true, false), 675, 400);
						} else if (Launch.getC().getP(0).isSoigneur()) {
							g.drawImage(vegeta, 675, 430);
						}
					}
				} else if (selection == 1) {
					if (Launch.getC().getP(1) != null) {
						if (Launch.getC().getP(1).isNuker()) {
							g.drawImage(goku.getScaledCopy(0.38f).getFlippedCopy(true, false), 675, 400);
						} else if (Launch.getC().getP(1).isSoigneur()) {
							g.drawImage(vegeta, 675, 430);
						}
					}

				}
				if (Launch.getG() != null) {
					if (!Launch.getG().getC1().equals(Launch.getC().getPseudo())) {
						if (Launch.getG().getP(0) != null) {
							if (Launch.getG().getP(0).isNuker()) {
								g.drawImage(goku.getScaledCopy(0.38f), 250, 400);
							} else if (Launch.getG().getP(0).isSoigneur()) {
								g.drawImage(vegeta.getFlippedCopy(true, false), 250, 430);
							}
						}
					} else if (!Launch.getG().getC2().equals(Launch.getC().getPseudo())) {
						if (Launch.getG().getP(1) != null) {
							if (Launch.getG().getP(1).isNuker()) {
								g.drawImage(goku.getScaledCopy(0.38f), 250, 400);
							} else if (Launch.getG().getP(1).isSoigneur()) {
								g.drawImage(vegeta.getFlippedCopy(true, false), 250, 430);
							}
						}
					}
				}
				g.setColor(Color.white);
				if (Launch.getC().getP(0) != null) {
					g.drawString(Launch.getC().getPseudo() + " Lvl:"
							+ Launch.getC().getP(Launch.getC().getSelection()).getLvl(), 720, 390);
				}
				if (Launch.getG() != null) {
					if (!Launch.getC().getPseudo().equals(Launch.getG().getC1())) {
						g.drawString(Launch.getG().getC1() + " Lvl:" + Launch.getG().getP(0).getLvl(), 280, 390);
					} else if (!Launch.getC().getPseudo().equals(Launch.getG().getC2())) {
						g.drawString(Launch.getG().getC2() + " Lvl:" + Launch.getG().getP(1).getLvl(), 280, 390);
					}
				}
				// sidebar affichage joueurs joueur selectionner en jaune
				g.setColor(new Color(0f, 0f, 0f, 0.85f));
				g.fillRect(0, 0, 150, 550);
				g.setColor(Color.white);
				g.drawString("Joueurs", 35, 35);
				if (Launch.getUserKeys() != null) {
					for (int i = 0; i < Launch.getUserKeys().size(); i++) {
						if (userSelection == i) {
							g.setColor(Color.yellow);
						} else {
							g.setColor(Color.white);
						}
						g.drawString("" + Launch.getUserKeys().get(i), 15, 55 + 17 * i);
					}
				}
				g.setColor(new Color(0.31372549f, 0.0823529412f, 0.611764706f));
				g.fillRect(25, 520, 100, 20);
				g.setColor(Color.yellow);
				if (Launch.getG() != null) {
					g.drawString("Quitter", 40, 522);
				} else {
					g.drawString("Inviter", 40, 522);
				}
			}
			if (invited) {
				g.setColor(new Color(0f, 0f, 0f, 0.9f));
				g.fillRect(0, 0, 1200, 600);
				g.setColor(new Color(0.0980392157f, 0.341176471f, 0.619607843f));
				g.fillRoundRect(400, 200, 400, 200, 25);
				g.setColor(Color.black);
				g.drawString(userKey + " vous a invité à jouer avec lui,", 425, 250);
				g.drawString("souhaitez vous le rejoindre?", 475, 275);
				g.setColor(Color.magenta);
				g.fillRoundRect(450, 340, 100, 30, 20);
				g.fillRoundRect(650, 340, 100, 30, 20);
				g.setColor(Color.yellow);
				g.drawString("Oui", 480, 345);
				g.drawString("Non", 680, 345);
			}
			g.setColor(Color.white);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if (Launch.getC() != null) {
			selection = Launch.getC().getSelection();
		}
	}

	@Override
	public int getID() {
		return 1;
	}

	@Override
	public void keyPressed(int kc, char v) {
		if (kc == (Input.KEY_ESCAPE) && (characMenu)) {
			characMenu = false;
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button == 0) {
			if (!invited) {
				if (Launch.getUserKeys() != null) {
					if (x > 15 && x < 135 && y > 55 && y < 55 + 17 * Launch.getUserKeys().size()) {
						userSelection = (y - 55) / 17;
						System.out.println(y);
						System.out.println((y - 55) / 17);
						System.out.println(userSelection);
					}
				}
				if (characMenu) {
					if (Launch.getC().getP(0) != null) {
						if ((x > 355 && x < 505) && (y > 70 && y < 250)) {
							Launch.getCs().sending("MENU SELECT " + 0);
						}
					}
					if (Launch.getC().getP(1) != null) {
						if ((x > 680 && x < 830) && (y > 70 && y < 250)) {
							Launch.getCs().sending("MENU SELECT " + 1);
						}
					}
				}
				if ((x > 1100 && x < 1200) && (y > 530 && y < 550)) {
					Launch.getCs().sending("MENU MESSAGE " + tf.getText());
					tf.setText("");
				}
			}
		}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		if (!invited) {
			if (!characMenu) {
				if ((x > 340 && x < 500) && (y > 555 && y < 595)) {
					characMenu = true;
				}
				if ((x > 500 && x < 660) && (y > 555 && y < 595)) {
					if (Launch.getC().getP(0) != null) {
						if (Launch.isSearching()) {
							Launch.getCs().sending("MENU SEARCHING STOP");
						} else {
							Launch.getCs().sending("MENU SEARCHING START");
						}
					}
				}
				if ((x > 25 && x < 125) && (y > 520 && y < 540)) {
					if (userSelection != -1) {
						if (Launch.getUserKeys().get(userSelection) != null) {
							if (Launch.getG() == null) {
								Launch.getCs().sending("MENU INVITE " + Launch.getUserKeys().get(userSelection));
							}
						}
					}
					if (Launch.getG() != null && !Launch.isSearching()) {
						Launch.getCs().sending("MENU LEAVING ");
					}
				}
			} else {
				if (Launch.getC().getP(Launch.getC().getSelection()) != null) {
					if ((x > 530 && x < 670) && (y > 270 && y < 300)) {
						Launch.getCs().sending("MENU COMP 1");
					}
					if ((x > 530 && x < 670) && (y > 310 && y < 340)) {
						Launch.getCs().sending("MENU COMP 2");
					}
					if ((x > 530 && x < 670) && (y > 350 && y < 380)) {
						Launch.getCs().sending("MENU COMP 3");
					}
					if ((x > 530 && x < 670) && (y > 150 && y < 180)) {
						Launch.getCs().sending("MENU DELETE");
					}
				}
				if ((x > 340 && x < 500) && (y > 555 && y < 595)) {
					characMenu = false;
				}
				if (x > 780 && x < 880 && y > 510 && y < 530) {
					Launch.getCs().sending("MENU CREATE HEALER");
				}
				if (x > 320 && x < 420 && y > 510 && y < 530) {
					Launch.getCs().sending("MENU CREATE NUKER");
				}
			}
		} else {
			if (x > 450 && x < 550 && y > 340 && y < 360) {
				Launch.getCs().sending("MENU TARGETED Y " + userKey + " invitation accepted");
				invited = false;
			}
			if (x > 650 && x < 750 && y > 340 && y < 360) {
				Launch.getCs().sending("MENU TARGETED N " + userKey + " invitation refused");

				invited = false;
			}
		}
	}

	public void mouseWheelMoved(int change) {
		if (!invited) {
			if ((Mouse.getX() > 900 && Mouse.getX() < 1200) && (Mouse.getY() > 70 && Mouse.getY() < 550)) {
				if (change == -120) {
					if (scroll > 1) {
						System.out.println(change / 100);
						scroll += change / 100;
						System.out.println("sc" + scroll);
						System.out.println("msg" + Launch.getMsg().size());
					}
				} else if (change == 120) {
					if (scroll < Launch.getMsg().size() - 17) {
						System.out.println(change / 100);
						scroll += change / 100;
						System.out.println("sc" + scroll);
						System.out.println("msg" + Launch.getMsg().size());
					}
				}
			}
		}
	}
}