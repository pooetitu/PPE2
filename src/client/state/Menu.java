package client.state;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import client.Launch;
import client.utils.PassField;

public class Menu extends BasicGameState implements KeyListener, MouseListener {
	private Image blink, back, butt11, butt12, butt21, butt22;
	private SpriteSheet back1;
	private TextField pseudo, pseudo1;
	private PassField password, password1, password2;
	private Animation back1A;
	private static String errString;
	boolean blinking = false, titlePage = true, butt1 = false, butt2 = false, connection = false, inscription = false;
	int time;
	private static StateBasedGame game;
	private static GameContainer container;

	@Override
	public int getID() {
		return 0;
	}

	public static StateBasedGame getGame() {
		return game;
	}

	public static void setErrString(String errString) {
		Menu.errString = errString;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		Menu.game = game;
		Menu.setContainer(container);
		// menu
		back = new Image("res/back/mirror_code.jpg");
		blink = new Image("res/back/press_start_blanc.png");
		back1 = new SpriteSheet("res/back/fond_menu3.png", 15600 / 13, 2025 / 3);
		back1A = new Animation(back1, 50);
		butt11 = new Image("res/buttons/bouton_connexion.png");
		butt12 = new Image("res/buttons/bouton_connexion_app.png");
		butt21 = new Image("res/buttons/bouton_inscription.png");
		butt22 = new Image("res/buttons/bouton_inscription_app.png");
		// connection
		pseudo1 = new TextField(container, container.getDefaultFont(), 520, 200, 150, 30);
		pseudo1.setBorderColor(Color.black);
		pseudo1.setBackgroundColor(Color.magenta);
		pseudo1.setMaxLength(20);
		password2 = new PassField(container, container.getDefaultFont(), 520, 270, 150, 30);
		password2.setBorderColor(Color.black);
		password2.setBackgroundColor(Color.magenta);
		password2.setMaxLength(16);
		password2.setMaskEnabled(true);
		// inscriptions
		pseudo = new TextField(container, container.getDefaultFont(), 520, 170, 150, 30);
		pseudo.setBorderColor(Color.black);
		pseudo.setBackgroundColor(Color.magenta);
		pseudo.setMaxLength(20);
		password = new PassField(container, container.getDefaultFont(), 520, 240, 150, 30);
		password.setBorderColor(Color.black);
		password.setBackgroundColor(Color.magenta);
		password.setMaxLength(16);
		password.setMaskEnabled(true);
		password1 = new PassField(container, container.getDefaultFont(), 520, 310, 150, 30);
		password1.setBorderColor(Color.black);
		password1.setBackgroundColor(Color.magenta);
		password1.setMaxLength(16);
		password1.setMaskEnabled(true);
		pseudo.setAcceptingInput(false);
		pseudo1.setAcceptingInput(false);
		password.setAcceptingInput(false);
		password1.setAcceptingInput(false);
		password2.setAcceptingInput(false);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		if (titlePage) {
			g.drawImage(back, 0, 0);
			if (!blinking) {
				g.drawImage(blink, 0, 0);
			}
		} else if (!connection && !inscription) {
			g.drawAnimation(back1A, 0, 0);
			if (!butt1) {
				g.drawImage(butt11, 500, 150);
			} else {
				g.drawImage(butt12, 500, 150);
			}
			if (!butt2) {
				g.drawImage(butt21, 500, 250);
			} else {
				g.drawImage(butt22, 500, 250);
			}
		}
		if (connection) {
			g.drawAnimation(back1A, 0, 0);
			if (errString != null) {
				g.setColor(Color.red);
				g.drawString(errString, 430, 150);
				g.setColor(Color.white);
			}
			pseudo1.render(container, g);
			password2.render(container, g);
			g.drawString("Pseudo", 570, 175);
			g.drawString("Mot de passe", 545, 245);
			if (!butt1) {
				g.drawImage(butt11, 500, 340);
			} else {
				g.drawImage(butt12, 500, 340);
			}
		} else if (inscription) {
			g.drawAnimation(back1A, 0, 0);
			if (errString != null) {
				g.setColor(Color.red);
				g.drawString(errString, 500, 120);
				g.setColor(Color.white);
			}
			pseudo.render(container, g);
			password.render(container, g);
			password1.render(container, g);
			g.drawString("Pseudo", 570, 145);
			g.drawString("Mot de passe", 545, 215);
			g.drawString("Repetez mot de passe", 500, 285);
			if (!butt1) {
				g.drawImage(butt21, 500, 415);
			} else {
				g.drawImage(butt22, 500, 415);
			}
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input in = container.getInput();
		time += delta;
		if (!Mouse.isButtonDown(0)) {
			butt1 = false;
			butt2 = false;
		}
		if (titlePage) {
			if (time / 100 % 6 == 0) {
				blinking = true;
			} else {
				blinking = false;
			}
		}
		if (in.isKeyDown(Input.KEY_ESCAPE) && (inscription || connection)) {
			inscription = false;
			connection = false;
			pseudo.setAcceptingInput(false);
			pseudo1.setAcceptingInput(false);
			password.setAcceptingInput(false);
			password1.setAcceptingInput(false);
			password2.setAcceptingInput(false);
		}

	}

	@Override
	public void keyReleased(int kc, char c) {
		if (kc == Input.KEY_ESCAPE) {
			inscription = false;
			connection = false;
			errString = null;
			pseudo.setAcceptingInput(false);
			pseudo1.setAcceptingInput(false);
			password.setAcceptingInput(false);
			password1.setAcceptingInput(false);
			password2.setAcceptingInput(false);
		}
		if (connection) {
			if (kc == Input.KEY_TAB) {
				if (pseudo1.hasFocus()) {
					password2.setFocus(true);
				} else if (password2.hasFocus()) {
					pseudo1.setFocus(true);
				}
			}
			if (kc == Input.KEY_ENTER) {
				if (!pseudo1.getText().equals("") && !password2.getText().equals("")) {
					System.out.println("CONNECT SIGNUP " + pseudo1.getText() + " " + password2.getDisplayText());
					Launch.getCs().sending("CONNECT SIGNUP " + pseudo1.getText() + " " + password2.getText());
				}
			}
		}
		if (inscription) {
			if (kc == Input.KEY_TAB) {
				if (pseudo.hasFocus()) {
					password.setFocus(true);
				} else if (password.hasFocus()) {
					password1.setFocus(true);
				} else if (password1.hasFocus()) {
					pseudo.setFocus(true);
				}
			}
			if (kc == Input.KEY_ENTER) {
				if (!pseudo.getText().equals("") && !password.getText().equals("")) {
					if (password.getText().equals(password1.getText())) {
						System.out.println("CONNECT SIGNIN " + pseudo.getText() + " " + password.getDisplayText());
						Launch.getCs().sending("CONNECT SIGNIN " + pseudo.getText() + " " + password.getText());
					}
				}

			}
		}
	}

	@Override
	public void keyPressed(int kc, char c) {
		if (titlePage) {
			titlePage = false;
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button == 0 || button == 1) {
			if (!connection && !inscription) {
				if ((x > 500 && x < 698) && (y > 150 && y < 247)) {
					butt1 = true;
				} else if ((x > 500 && x < 698) && (y > 250 && y < 347)) {
					butt2 = true;
				} 
			} else if (connection) {
				if ((x > 500 && x < 698) && (y > 340 && y < 437)) {
					butt1 = true;
				}
			} else if (inscription) {
				if ((x > 500 && x < 698) && (y > 415 && y < 512)) {
					butt1 = true;
				}
			}
		}

	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		if (!titlePage) {
			if (!connection && !inscription) {
				if ((x > 500 && x < 698) && (y > 150 && y < 247)) {
					connection = true;
					pseudo1.setAcceptingInput(true);
					password2.setAcceptingInput(true);
					pseudo1.setFocus(true);
					;
				} else if ((x > 500 && x < 698) && (y > 250 && y < 347)) {
					inscription = true;
					pseudo.setFocus(true);
					pseudo.setAcceptingInput(true);
					password.setAcceptingInput(true);
					password1.setAcceptingInput(true);
					;
				}
			}
			if (inscription) {
				if ((x > 500 && x < 698) && (y > 415 && y < 512)) {
					if (!pseudo.getText().equals("") && !password.getText().equals("")) {
						if (password.getText().equals(password1.getText())) {
							System.out.println("CONNECT SIGNIN " + pseudo.getText() + " " + password.getDisplayText());
							Launch.getCs().sending("CONNECT SIGNIN " + pseudo.getText() + " " + password.getText());
						} else {
							errString = "Mot de passe non identique";
						}
					}
				}
			} else if (connection) {
				if ((x > 500 && x < 698) && (y > 340 && y < 437)) {
					if (!pseudo1.getText().equals("") && !password2.getText().equals("")) {
						System.out.println("CONNECT SIGNUP " + pseudo1.getText() + " " + password2.getDisplayText());
						Launch.getCs().sending("CONNECT SIGNUP " + pseudo1.getText() + " " + password2.getText());
					}
				}
			}
		} else {
			titlePage = false;
		}
	}

	public static GameContainer getContainer() {
		return container;
	}

	public static void setContainer(GameContainer container) {
		Menu.container = container;
	}
}