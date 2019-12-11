package server.commands;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {
	private static Statement stmt = null;
	private static Connection dbcon = null;

	public static void startConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
			String user = "postgres";
			String password = "pierre";
			dbcon = DriverManager.getConnection(dbUrl, user, password);
			System.out.println("connected");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean userExist(String pseudo, String mdp) {
		int id = -1;
		try {
			stmt = dbcon.createStatement();
			ResultSet rs = stmt
					.executeQuery("select id from compte where pseudo='" + pseudo + "' and mdp='" + mdp + "'");
			while (rs.next()) {
				id = rs.getInt(1);
			}
			stmt.close();
		} catch (SQLException e) {
			System.out.print("error");
			e.printStackTrace();
		}
		if (id != -1) {
			return true;
		} else {
			return false;
		}

	}

	public int userConnection(String pseudo, String mdp) {
		int id = 0;
		try {
			stmt = dbcon.createStatement();
			ResultSet rs = stmt
					.executeQuery("select * from compte where pseudo='" + pseudo + "' and mdp='" + mdp + "'");
			while (rs.next()) {
				id = rs.getInt(1);
			}
			stmt.close();
		} catch (SQLException e) {
			System.out.print("error");
			e.printStackTrace();
		}
		return id;
	}

	public void userCreation(String pseudo, String mdp) {
		try {
			Statement stmt = dbcon.createStatement();
			stmt.executeUpdate("insert into compte(pseudo,mdp)values('" + pseudo + "','" + mdp + "');");
			stmt.close();
		} catch (SQLException e) {
			System.out.println("erreur");
			e.printStackTrace();
		}

	}
}
