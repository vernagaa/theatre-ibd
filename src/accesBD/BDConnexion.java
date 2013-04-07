package accesBD;

import exceptions.ExceptionConnexion;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public final class BDConnexion {

	/**
	    * Obtenir une nouvelle connexion a la BD, en fonction des parametres
	    * contenus dans un fichier de configuration.
	    * @return  une nouvelle connexion a la BD
	    * @throws ExceptionConnexion si la connexion a echoue
	    */

	public static Connection getConnexion(String login, String mdp) throws ExceptionConnexion {
		Connection conn = null ;
		try {

			// lecture des parametres de connexion dans connection.conf
			Properties p = new Properties();
			InputStream is = null;
			is = new FileInputStream(utils.Constantes.Home + utils.Constantes.Config);
			p.load(is);
			String url = p.getProperty("url");
			String driver = p.getProperty("driver");
			
			Class.forName(driver);
			// hopper@UFR, Oracle
			conn = DriverManager.getConnection(url,login,mdp);
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			System.out.println("Connexion impossible : " + e.getMessage());// handle any errors
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		} catch (IOException e) {
			throw new ExceptionConnexion ("fichier conf illisible \n" + e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new ExceptionConnexion ("problème d'identification du pilote \n" + e.getMessage());
		}
		return conn ;
	}

	/**
	 * Fermer la connexion, l'instruction et la structure de resultats. Fermer les
	 * 3 a la fois semble correspondre a de nombreux cas.
	 * @param conn la connexion
	 * @param stmt l'instruction
	 * @param rs la structure de resultats
	 */
	public static void FermerTout (Connection conn, Statement stmt, ResultSet rs){
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				;
			}
			rs = null;
		}
		if (stmt != null) {
			try {
				stmt.close();
			}
			catch (SQLException e) {
				;
			}
			stmt = null;
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				;
			}
			conn = null;
		}
	}
}
