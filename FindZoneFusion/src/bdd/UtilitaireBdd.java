package bdd;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilitaireBdd {

	public static void main(String[] args) throws SQLException {
		
		BaseDeDonnee uneBDD = new BaseDeDonnee("bddfindzone");
		
		/*
		uneBDD.envoyeUneRequetteDEcriture("DELETE FROM COORDONNER WHERE Id_Co IN (0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16) ");

		uneBDD.envoyeUneRequetteDEcriture("INSERT INTO COORDONNER VALUES(1,0,0,0)");
		uneBDD.envoyeUneRequetteDEcriture("INSERT INTO COORDONNER VALUES(2,0,0,250)");
		uneBDD.envoyeUneRequetteDEcriture("INSERT INTO COORDONNER VALUES(3,0,250,250)");
		uneBDD.envoyeUneRequetteDEcriture("INSERT INTO COORDONNER VALUES(4,0,250,0)");
		
		uneBDD.envoyeUneRequetteDEcriture("INSERT INTO COORDONNER VALUES(5,1,100,100)");
		uneBDD.envoyeUneRequetteDEcriture("INSERT INTO COORDONNER VALUES(6,1,100,250)");
		uneBDD.envoyeUneRequetteDEcriture("INSERT INTO COORDONNER VALUES(7,1,250,250)");
		uneBDD.envoyeUneRequetteDEcriture("INSERT INTO COORDONNER VALUES(8,1,250,100)");

		uneBDD.envoyeUneRequetteDEcriture("INSERT INTO COORDONNER VALUES(9,2,250,250)");
		uneBDD.envoyeUneRequetteDEcriture("INSERT INTO COORDONNER VALUES(10,2,500,250)");
		uneBDD.envoyeUneRequetteDEcriture("INSERT INTO COORDONNER VALUES(11,2,500,500)");
		uneBDD.envoyeUneRequetteDEcriture("INSERT INTO COORDONNER VALUES(12,2,250,500)");
*/
//		uneBDD.envoyeUneRequetteDEcriture("DELETE FROM theme");
		uneBDD.afficherLaBdd("theme", "IMA_THE");
//		uneBDD.afficherQuestion();
//		uneBDD.envoyeUneRequetteDEcriture("");
	}

}
