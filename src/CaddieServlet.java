/*
 * CaddieServlet.java
 */

import accesBD.BDCategories;
import accesBD.BDRepresentations;
import accesBD.BDSpectacles;
import java.io.IOException;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modele.Caddie;
import modele.Categorie;
import modele.Representation;
import modele.Reservation;
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
		out.println("<head><title>Votre caddie</title></head>");
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
				Integer spectacle = null;
				Date date = null;
				Integer nbPlaces = null;
				String categorie = req.getParameter("categorie");

				if (caddie == null) {
					caddie = new Caddie();
				}
				if (req.getParameter("spectacle") != null) {
					spectacle = Integer.parseInt(req.getParameter("spectacle"));
				}
				if (req.getParameter("date") != null) {
					date = Utilitaires.toDate(req.getParameter("date"), "dd-MM-yyyy HH:mm");
				}
				if (req.getParameter("nbPlaces") != null) {
					nbPlaces = Integer.parseInt(req.getParameter("nbPlaces"));
				}

				if (spectacle != null && date != null && categorie != null && nbPlaces == null) {
					// suppression
					Representation repres = BDRepresentations.getRepresentation(user, spectacle, date);
					Categorie categ = BDCategories.getCategorie(user, categorie);
					if (repres != null && categ != null) {
						caddie.removeReservation(repres, categ);
						session.setAttribute("caddie", caddie);
					}
				} else if (spectacle != null && date != null && categorie != null && nbPlaces != null && nbPlaces <= 1) {
					// màj du nombre de places
					Representation repres = BDRepresentations.getRepresentation(user, spectacle, date);
					Categorie categ = BDCategories.getCategorie(user, categorie);
					if (repres != null && categ != null) {
						caddie.getReservation(repres, categ).setNbPlaces(nbPlaces);
						session.setAttribute("caddie", caddie);
					}
				}

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
					for (Reservation r : caddie.getReservations()) {
						out.println("<tr>");
						out.println("<td>" + BDSpectacles.getSpectacle(user, r.getRepres().getSpectacle()).getNom() + "</td>");
						out.println("<td>" + Utilitaires.toString(r.getRepres().getDate()) + "</td>");
						out.println("<td>" + r.getCateg().getCategorie() + "</td>");
						out.println("<td>"
								+ "<form action=\"CaddieServlet\" method=\"post\">"
								+ "<input type=\"text\" name=\"nbPlaces\" value=\"" + r.getNbPlaces() + "\" />"
								+ "<input type=\"hidden\" name=\"spectacle\" value=\"" + r.getRepres().getSpectacle() + "\"/>"
								+ "<input type=\"hidden\" name=\"date\" value=\"" + Utilitaires.toStringBd(r.getRepres().getDate()) + "\"/>"
								+ "<input type=\"hidden\" name=\"categorie\" value=\"" + r.getCateg().getCategorie() + "\"/>"
								+ "<input type=\"submit\" value=\"Modifier\"/>"
								+ "</form>"
								+ "</td>");
						out.println("<td>" + r.getPrixTotal() + "&euro;</td>");
						out.println("<td>"
								+ "<form action=\"CaddieServlet\" method=\"post\">"
								+ "<input type=\"hidden\" name=\"spectacle\" value=\"" + r.getRepres().getSpectacle() + "\"/>"
								+ "<input type=\"hidden\" name=\"date\" value=\"" + Utilitaires.toStringBd(r.getRepres().getDate()) + "\"/>"
								+ "<input type=\"hidden\" name=\"categorie\" value=\"" + r.getCateg().getCategorie() + "\"/>"
								+ "<input type=\"submit\" value=\"Supprimer\"/>"
								+ "</form>"
								+ "</td>");
						out.println("</tr>");
					}
					out.println("</table>");
				}

			}
		} catch (Exception e) {
			String o = "";
			for (StackTraceElement st : e.getStackTrace()) {
				o += "<br/>" + st;
			}
			out.println("<p><i><font color=\"#FFFFFF\">Erreur de connexion à la base de données</i><br/>" + e + o + "</p>");
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