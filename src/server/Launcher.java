package server;

import server.commands.GameServer;
import server.commands.SQL;

public class Launcher {
	public Launcher() {

	}


	public static void main(String[] args) {
		SQL.startConnection();
		GameServer gs = new GameServer();
		gs.open();
		System.out.println("Serveur lancé.");

		// ecriture object

//		Personnage p1 = new Soigneur("pooetitu");
//		Personnage p2 = new Nuker("pooet");
//		Personnage p3 = new Soigneur("poooooet");
//		Personnage p4 = new Nuker("poetituuuuuu");
//		
//		Group g1= new Group(p1,p2,null,null);
//		Group g2=new Group(p3,p4,null,null);
//		File file = new File("0");
//		Arene a=new Arene(g1,g2);
//		System.out.println(a);
//		ObjectOutputStream oos = null;
//		try {
//
//			final FileOutputStream fichier = new FileOutputStream(file);
//
//			oos = new ObjectOutputStream(fichier);
//			oos.writeObject(a);
//
//		} catch (final IOException e) {
//
//			e.printStackTrace();
//
//		}
//		
//		
//		
//		ObjectInputStream ois = null;
//		
//		 
//		
//		try {
//		
//		final FileInputStream fichier = new FileInputStream(file);
//		
//		ois = new ObjectInputStream(fichier);
//
//
//		Arene a1 = (Arene) ois.readObject();
//		System.out.println(a1);
//		
//		} catch (final IOException e) {
//		
//		e.printStackTrace();
//		
//		} catch (final ClassNotFoundException e) {
//		
//		e.printStackTrace();
//		
//		}

	}

}
