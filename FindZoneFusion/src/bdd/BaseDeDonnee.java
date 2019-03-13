package bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class BaseDeDonnee {

	private String nom;
	private Connection connection;

	public BaseDeDonnee(String nom) {
		super();
		this.nom = nom;

	}

	public ResultSet envoyeUneRequetteDeLecture(String requette) throws SQLException {
		this.connectionBDD();
		Statement st = null;
		ResultSet res = null;
		st = connection.createStatement();
		res = st.executeQuery(requette);
		System.out.println("Requette OK\n");
		return res;
	}

	public void envoyeUneRequetteDEcriture(String requette) throws SQLException {
		this.connectionBDD();
		try {
			Statement state = connection.createStatement();
			state.execute(requette);
			state.close();
		} catch (Exception e) {
			e.printStackTrace();
			;
		}
		System.out.println("Requette OK\n");
	}

	public void connectionBDD() {
		try {
			Class.forName("org.h2.Driver");
			System.out.println("driver OK");
			Properties props = new Properties();
			props.setProperty("user", "root");
			props.setProperty("password", "root");
			connection = DriverManager.getConnection(
					"jdbc:h2:..\\EditFindZone\\BDD\\" + nom + ".db", props);
			System.out.println("Connexion effective !");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void afficherRequetteConsol(ResultSet res, String colonne) throws SQLException {
		this.connectionBDD();
		while (res.next())
			System.out.println(res.getString(colonne));
	}

	public void afficherLaBdd(String table, String colonne) throws SQLException {
		this.connectionBDD();
		afficherRequetteConsol(envoyeUneRequetteDeLecture("SELECT " + colonne + " FROM " + table + ";"), colonne);
	}

	public void afficherCoordonnner() throws SQLException {
		this.connectionBDD();
		String rep = null;
		Statement state = connection.createStatement();
		ResultSet rs = state.executeQuery("select * from COORDONNER;");
		ResultSetMetaData resultMeta = rs.getMetaData();
		while (rs.next()) {
			for (int i = 1; i <= resultMeta.getColumnCount(); i++)
				rep += " " + rs.getObject(i).toString();
			System.out.println(rep);
			rep = null;
		}
	}

	public void afficherTheme() throws SQLException {
		this.connectionBDD();
		String rep = "";
		Statement state = connection.createStatement();
		ResultSet rs = state.executeQuery("select * from THEME;");
		ResultSetMetaData resultMeta = rs.getMetaData();
		while (rs.next()) {
			for (int i = 1; i <= resultMeta.getColumnCount(); i++)
				rep += " " + rs.getObject(i).toString();
			System.out.println(rep);
			rep = null;
		}
	}

	public void afficherQuestion() throws SQLException {
		this.connectionBDD();
		String rep = null;
		Statement state = connection.createStatement();
		ResultSet rs = state.executeQuery("select * from QUESTION;");
		ResultSetMetaData resultMeta = rs.getMetaData();
		while (rs.next()) {
			for (int i = 1; i <= resultMeta.getColumnCount(); i++)
				rep += " " + rs.getObject(i).toString();
			System.out.println(rep);
			rep = null;
		}
	}
}
