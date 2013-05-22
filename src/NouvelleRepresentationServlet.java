/*
 * @(#)NouvelleRepresentationServlet.java	1.0 2007/10/31
 * 
 * Copyright (c) 2007 Sara Bouchenak.
 */

import accesBD.BDRepresentations;
import accesBD.BDSpectacles;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modele.Spectacle;
import modele.Utilisateur;
import utils.Utilitaires;

/**
 * NouvelleRepresentation Servlet.
 *
 * This servlet dynamically adds a new date a show.
 *
 * @author <a href="mailto:Sara.Bouchenak@imag.fr">Sara Bouchenak</a>
 * @version 1.0, 31/10/2007
 */
public class NouvelleRepresentationServlet extends HttpServlet {

	/**
	 * HTTP GET request entry point.
	 *
	 * @param req	an HttpServletRequest object that contains the request the
	 * client has made of the servlet
	 * @param res	an HttpServletResponse object that contains the response the
	 * servlet sends to the client
	 *
	 * @throws ServletException if the request for the GET could not be handled
	 * @throws IOException	if an input or output error is detected when the
	 * servlet handles the GET request
	 */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String numS, dateS, heureS;
		ServletOutputStream out = res.getOutputStream();
		utils.Constantes.Home = getServletContext().getRealPath("/");

		res.setContentType("text/html");

		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />");
		out.println("<title> Ajouter une nouvelle représentation </title>");
		out.println("<style>"
				+ "body {color: darkslategray; background-image: radial-gradient(white, darkred 90%, black); padding: 20px;}"
				+ "div {width: 1000px; min-height: 1000px; margin: auto; }"
				+ "a {color: black; font-weight: bold;}"
				+ "a:hover {color: red;}"
				+ "</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("<div>");
		out.println("<h1> Ajouter une nouvelle représentation </h1>");

		try {
			Utilisateur user = Utilitaires.Identification();
			if (user != null) {

				numS = req.getParameter("numS");
				dateS = req.getParameter("date");
				heureS = req.getParameter("heure");

				if (numS == null || dateS == null || heureS == null) {
					List<Spectacle> spectacles = BDSpectacles.getSpectacle(user);
					out.println("<p>Veuillez saisir les informations relatives &agrave; la nouvelle repr&eacute;sentation :</p>");
					out.print("<form action=\"");
					out.print("NouvelleRepresentationServlet\" ");
					out.println("method=POST>");

					out.println("Spectacle :");
					if (spectacles.isEmpty()) {
						out.println("<select id=\"numS\">");
						out.println("<option>Aucun spectacle disponible</option>");
					} else {
						out.println("<select id=\"numS\" name=\"numS\">");
						for (Spectacle s : spectacles) {
							out.println("<option value=\"" + s.getId() + "\">" + s.getNom() + "</option>");
						}
					}
					out.println("</select>");

					out.println("<br/>");
					out.println("Date de la repr&eacute;sentation (jj-mm-aaaa) :");
					out.println("<input type=text size=20 name=date>");
					out.println("<br/>");
					out.println("Heure de d&eacute;but de la repr&eacute;sentation (hh:mm) :");
					out.println("<input type=text size=20 name=heure>");
					out.println("<br/>");
					out.println("<input type=submit>");
					out.println("</form>");
				} else {
					// Transformation des paramètres vers les types adéquats.
					// Ajout de la nouvelle représentation.
					// Puis construction dynamique d'une page web de réponse.
					int num = Integer.parseInt(numS);
					Date d = Utilitaires.toDate(dateS + " " + heureS, "dd-MM-yyyy HH:mm");
					BDRepresentations.addRepresentation(user, num, d);

					out.println("<p>Représentation ajoutée</p>");
					out.println("<p><a href=\"NouvelleRepresentationServlet\">Retour au formulaire</a></p>");
				}
			}
		} catch (Exception e) {
			out.println("<p><i>Erreur de connexion à la base de données</i><br/>" + e + "</p>");
		}

		out.println("<hr/><p><a href=\"/admin/admin.html\">Page d'administration</a></p>");
		out.println("<hr/><p><a href=\"/index.html\">Accueil</a></p>");
		out.println("</div>");
		out.println("</body>");
		out.println("<html>");
		out.close();

	}

	/**
	 * HTTP POST request entry point.
	 *
	 * @param req	an HttpServletRequest object that contains the request the
	 * client has made of the servlet
	 * @param res	an HttpServletResponse object that contains the response the
	 * servlet sends to the client
	 *
	 * @throws ServletException if the request for the POST could not be handled
	 * @throws IOException	if an input or output error is detected when the
	 * servlet handles the POST request
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
