/*
 * @(#)NouvelleReservationServlet.java	1.0 2007/10/31
 * 
 */

import accesBD.BDCategories;
import accesBD.BDRepresentations;
import accesBD.BDSpectacles;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import modele.Categorie;
import modele.Representation;
import modele.Spectacle;
import modele.Utilisateur;
import utils.Utilitaires;

/**
 * NouvelleReservation Servlet.
 *
 * This servlet dynamically adds a new reservation.
 *
 */
public class NouvelleReservationServlet extends HttpServlet {

	/**
	 * HTTP GET request entry point.
	 *
	 * @param req	an HttpServletRequest object that contains the request 
	 *			the client has made of the servlet
	 * @param res	an HttpServletResponse object that contains the response 
	 *			the servlet sends to the client
	 *
	 * @throws ServletException   if the request for the GET could not be handled
	 * @throws IOException	 if an input or output error is detected 
	 *				 when the servlet handles the GET request
	 */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		ServletOutputStream out = res.getOutputStream();

		res.setContentType("text/html");

		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />");
		out.println("<head><title> Réserver une place </title></head>");
		out.println("<body bgproperties=\"fixed\" background=\"/images/rideau.JPG\">");
		out.println("<font color=\"#FFFFFF\"><h1> Réserver une place </h1>");

		try {
			Utilisateur user = Utilitaires.Identification();
			if (user != null) {
				Integer numS = null;
				Date dateR = null;
				String categorie = null;
				if (req.getParameter("spectacle") != null) {
					numS = Integer.parseInt(req.getParameter("spectacle"));
				}
				if (req.getParameter("dateR") != null) {
					dateR = Utilitaires.toDate(req.getParameter("dateR"), "dd-MM-yyyy HH:mm");
				}
				if (req.getParameter("categorie") != null) {
					categorie = req.getParameter("categorie");
				}

				// choix du spectacle
				List<Spectacle> spectacles = BDSpectacles.getSpectacle(user);

				out.println("</ul>");
				out.println("<p>");
				out.print("<form action=\"");
				out.print("NouvelleReservationServlet\" ");
				out.println("method=GET>");
				out.println("<label for=\"spectacle\">Spectacle :</label>");
				out.println("<select id=\"spectacle\" name=\"spectacle\" onchange=\"submit()\">");
				for (Spectacle s : spectacles) {
					if (numS != null && numS == s.getId()) {
						out.println("<option selected=\"true\" value=\"" + s.getId() + "\">" + s.getNom() + "</option>");
					} else {
						out.println("<option value=\"" + s.getId() + "\">" + s.getNom() + "</option>");
					}
				}
				out.println("</select>");
				out.println("<br>");

				// choix de la représentation
				if (numS != null) {
					List<Representation> representations = BDRepresentations.getRepresentations(user, numS);
					List<Categorie> categories = BDCategories.getCategorie(user);

					out.println("<label for=\"dateR\">Date de représentation :</label>");
					out.println("<select id=\"dateR\" name=\"dateR\" onchange=\"submit()\">");
					for (Representation r : representations) {
						if (dateR != null && r.getDate().compareTo(dateR) == 0) {
							out.println("<option selected=\"true\" value=\"" + Utilitaires.toStringBd(r.getDate()) + "\">" + Utilitaires.toString(r.getDate()) + "</option>");
						} else {
							out.println("<option value=\"" + Utilitaires.toStringBd(r.getDate()) + "\">" + Utilitaires.toString(r.getDate()) + "</option>");
						}
					}
					out.println("</select>");
					out.println("<br>");

					out.println("<label for=\"categorie\">Categorie :</label>");
					out.println("<select id=\"categorie\" name=\"categorie\" onchange=\"submit()\">");
					for (Categorie c : categories) {
						//TODO inclure le nombre de places disponibles pour chaque catégorie
						if (categorie != null && categorie.equals(c.getCategorie())) {
							out.println("<option selected=\"true\" value=\"" + c.getCategorie() + "\">" + c.getCategorie() + " (" + c.getPrix() + "&euro;)</option>");
						} else {
							out.println("<option value=\"" + c.getCategorie() + "\">" + c.getCategorie() + " (" + c.getPrix() + "&euro;)</option>");
						}
					}
					out.println("</select>");
					out.println("<br>");
				}

				out.println("<input type=submit>");
				out.println("</form>");


			}
		} catch (Exception e) {
			out.println("<p><i><font color=\"#FFFFFF\">Erreur de connexion à la base de données</i><br/>" + e + "</p>");
		}

		out.println("<hr><p><font color=\"#FFFFFF\"><a href=\"/index.html\">Page d'accueil</a></p>");
		out.println("</body>");
		out.println("</html>");
		out.close();

	}

	/**
	 * HTTP POST request entry point.
	 *
	 * @param req	an HttpServletRequest object that contains the request 
	 *			the client has made of the servlet
	 * @param res	an HttpServletResponse object that contains the response 
	 *			the servlet sends to the client
	 *
	 * @throws ServletException   if the request for the POST could not be handled
	 * @throws IOException	   if an input or output error is detected 
	 *					   when the servlet handles the POST request
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doGet(req, res);
	}

	/**
	 * Returns information about this servlet.
	 *
	 * @return String information about this servlet
	 */
	@Override
	public String getServletInfo() {
		return "Ajoute une représentation à une date donnée pour un spectacle existant";
	}
}
