/*
 * CaddieServlet.java
 */

import accesBD.BDSpectacles;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modele.Caddie;
import modele.Utilisateur;
import utils.Utilitaires;

public class CaddieServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		//Get the session object
		HttpSession session = req.getSession(true);

		//Get the output stream
		ServletOutputStream out = res.getOutputStream();
		utils.Constantes.Home = getServletContext().getRealPath("/");
		
		res.setContentType("text/html");

		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />");
		out.println("<head><title> Ajouter une nouvelle représentation </title></head>");
		out.println("<style>"
				+ "a {color: black; font-weight: bold;}"
				+ "a:hover {color: red;}"
				+ "body {color: white;}"
				+ "</style>");
		out.println("<body bgproperties=\"fixed\" background=\"/images/rideau.JPG\">");
		out.println("<h1>Votre caddie</h1>");

		try {
			Utilisateur user = Utilitaires.Identification();
			if (user != null) {
				Caddie caddie = (Caddie) session.getAttribute("caddie");
				if (caddie == null) {
					caddie = new Caddie();
				}
//		session.setAttribute("caddie", caddie);

				if (caddie.getReservations().isEmpty()) {
					out.println("<p><em>Votre caddie est vide</em></p>");
				} else {
					out.println("<table>");
					out.println("<tr>");
					out.println("<th>Spectacle</th>");
					out.println("<th>Date</th>");
					out.println("<th>Catégorie</th>");
					out.println("<th>Nombre</th>");
					out.println("<th>Total</th>");
					out.println("</tr>");
					for (Caddie.Reservation r : caddie.getReservations()) {
						out.println("<tr>");
						out.println("<td>"+BDSpectacles.getSpectacle(user, r.getRepres().getSpectacle()).getNom()+"</td>");
						out.println("<td>"+Utilitaires.toString(r.getRepres().getDate())+"</td>");
						out.println("<td>"+r.getCateg().getCategorie()+"</td>");
						out.println("<td>"+r.getNbPlaces()+"</td>");
						out.println("<td>"+r.getPrixTotal()+"&euro;</td>");
						out.println("</tr>");
					}
					out.println("</table>");
				}

			}
		} catch (Exception e) {
			out.println("<p><i><font color=\"#FFFFFF\">Erreur de connexion à la base de données</i><br/>" + e + "</p>");
		}
		out.println("<hr><p><font color=\"#FFFFFF\"><a href=\"/index.html\">Page d'accueil</a></p>");
		out.println("</body>");
		out.println("</html>");
		out.close();
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doGet(req, res);
	}

	@Override
	public String getServletInfo() {
		return "A simple session servlet";
	}
}