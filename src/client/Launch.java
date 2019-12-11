package client;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import client.state.Game;
import client.state.Lobby;
import client.state.Menu;
import common.Compte;
import common.Group;

public class Launch extends StateBasedGame {
	private final static String name = "Mirror Code";
	private static CommandHandler ch;
	private static CommandSender cs;
	private static Compte c;
	private static Group g,g2;
	private static boolean inGroup,playing=false;
	private static boolean searching=false;
	private static ArrayList<String> userKeys;
	private static ArrayList<String> msg = new ArrayList<String>();
	public static Group getG() {
		return g;
	}
	public static boolean isInGroup() {
		return inGroup;
	}

	public static void setInGroup(boolean inGroup) {
		Launch.inGroup = inGroup;
	}

	public static void setG(Group g) {
		Launch.g = g;
	}

	public static ArrayList<String> getMsg() {
		return msg;
	}

	public static ArrayList<String> getUserKeys() {
		return userKeys;
	}

	public static void setUserKeys(ArrayList<String> userKeys) {
		Launch.userKeys = userKeys;
	}

	public Launch(String name) {
		super(name);
	}

	public static CommandHandler getCh() {
		return ch;
	}

	public static CommandSender getCs() {
		return cs;
	}

	public static Compte getC() {
		return c;
	}

	public static void setC(Compte a) {
		Launch.c = a;
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.addState(new Menu());
		this.addState(new Lobby());
		this.addState(new Game());
		gc.setAlwaysRender(true);
		gc.setTargetFrameRate(60);
		gc.setVSync(false);
	}

	public static void main(String[] args) {
		cs = new CommandSender();
		ch = new CommandHandler();
		Thread t = new Thread(ch);
		t.start();
		AppGameContainer appGC;
		try {
			appGC = new AppGameContainer(new Launch(name));
			appGC.setDisplayMode(1200, 600, false);
			appGC.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	public static boolean isSearching() {
		return searching;
	}
	public static void setSearching(boolean searching) {
		Launch.searching = searching;
	}
	public static Group getG2() {
		return g2;
	}
	public static void setG2(Group g2) {
		Launch.g2 = g2;
	}
	public static boolean isPlaying() {
		return playing;
	}
	public static void setPlaying(boolean playing) {
		Launch.playing = playing;
	}
}