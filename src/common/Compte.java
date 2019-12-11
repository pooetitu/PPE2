package common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import server.commands.SQL;

public class Compte implements Serializable {
	private static final long serialVersionUID = -5002309920606290701L;
	private String pseudo;
	private int id, nbperso, selection;
	private Personnage[] p;

	public Compte(String pseudo, String mdp) {
		this.pseudo = pseudo;
		nbperso = 0;
		selection = -1;
		SQL sql = new SQL();
		sql.userCreation(pseudo, mdp);
		p = new Personnage[2];
		this.id = sql.userConnection(pseudo, mdp);
	}

	public int getNbperso() {
		return nbperso;
	}

	public int getSelection() {
		return selection;
	}

	public void setSelection(int selection) {
		this.selection = selection;
	}

	public String getPseudo() {
		return pseudo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Personnage getP(int x) {
		Personnage p1;
		if (x == 0) {
			p1 = this.p[0];
		} else {
			p1 = this.p[1];
		}
		return p1;
	}

	public void setP(Personnage[] p) {
		this.p = p;
	}

	public void creePerso(String choice) {
		System.out.println("nbperso");
		if (nbperso < 2) {
			switch (choice) {
			case ("NUKER"): {
				p[nbperso] = new Nuker();
				nbperso++;
				selection = nbperso - 1;
				break;
			}
			case ("HEALER"): {
				p[nbperso] = new Soigneur();
				nbperso++;
				selection = nbperso - 1;
				break;
			}
			}
			save();
		}
	}

	public void delPerso() {
		if (nbperso == 2 && selection == 0) {
			p[0] = p[1];
			p[1] = null;
			nbperso--;
			selection--;
		} else {
			p[selection] = null;
			nbperso--;
			selection--;
		}
	}

	public void save() {
		try {
			File file = new File(String.valueOf(id));
			FileOutputStream fichier = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fichier);
			oos.writeObject(this);
			oos.flush();
			oos.close();
			fichier.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "Compte [pseudo=" + pseudo + ", id=" + id + "]";
	}

}