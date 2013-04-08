/*
 * @(#)ProgrammeServlet.java	1.0 2007/10/31
 * 
 * Copyright (c) 2007 Sara Bouchenak.
 */

import accesBD.BDRepresentations;
import accesBD.BDSpectacles;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modele.Representation;
import modele.Spectacle;
import modele.Utilisateur;
import utils.Utilitaires;

/**
 * Proramme Servlet.
 *
 * This servlet dynamically returns the theater program.
 *
 * @author <a href="mailto:Sara.Bouchenak@imag.fr">Sara Bouchenak</a>
 * @version 1.0, 31/10/2007
 */
public class ProgrammeServlet extends HttpServlet {

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
		ServletOutputStream out = res.getOutputStream();
		utils.Constantes.Home = getServletContext().getRealPath("/");

		res.setContentType("text/html");

		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />");
		out.println("<head><title> Programme de la saison </title></head>");
		out.println("<style>"
				+ "a {color: black; font-weight: bold;}"
				+ "a:hover {color: red;}"
				+ "</style>");
		out.println("<body bgproperties=\"fixed\" background=\"/images/rideau.JPG\">");
		out.println("<font color=\"#FFFFFF\"><h1> Programme de la saison </h1>");

		// TO DO
		// Récupération de la liste de tous les spectacles de la saison.
		// Puis construction dynamique d'une page web décrivant ces spectacles.

		try {
			Utilisateur user = Utilitaires.Identification();
			if (user != null) {
				out.println("<h3>Les spectacles :</h3>");
				out.println("<ul>");
				List<Spectacle> spectacles = BDSpectacles.getSpectacle(user);
				List<Representation> representations = BDRepresentations.getRepresentation(user);
				for (Spectacle s : spectacles) {
					out.println("<li><a href=\"?spectacle=" + s.getId() + "\">" + s.getNom() + "</a></li>");
				}
				out.println("</ul>");

				out.println("<h3>Les représentations :</h3>");
				out.println("<ul>");
				for (Representation r : representations) {
					out.println("<li><a href=\"?date=" + Utilitaires.toStringBd(r.getDate()) + "\">" + Utilitaires.toString(r.getDate()) + "</a></li>");
				}
				out.println("</ul>");

				if (req.getParameter("spectacle") != null) {
					int specId = Integer.parseInt(req.getParameter("spectacle"));
					Spectacle spec = BDSpectacles.getSpectacle(user, specId);

					if (spec != null) {
						out.println("<hr/><h3>Représentations du spectacle " + spec.getNom() + " :</h3>");
						boolean b = true;
						for (Representation r : representations) {
							if (r.getSpectacle() == specId) {
								b = false;
								out.println("<li>");
								out.println("<a href=\"/servlet/PlacesDispoServlet?date=" + Utilitaires.toStringBd(r.getDate()) + "&spectacle=" + specId + "\">" + Utilitaires.toString(r.getDate()) + "</a>");
								out.println("<a href=\"/servlet/NouvelleReservationServlet?dateR=" + Utilitaires.toStringBd(r.getDate()) + "&spectacle=" + specId + "\">[Réserver]</a>");
								out.println("</li>");
							}
						}
						if (b) {
							out.println("<p><em>Aucune représentation</em></p>");
						}
					}
				} else if (req.getParameter("date") != null) {
					String date = req.getParameter("date");
					List<Spectacle> spec = BDSpectacles.getSpectacle(user, Utilitaires.toDate(date, "dd-MM-yyyy HH:mm"));

					if (spec != null) {
						out.println("<hr/><h3>Spectacles pour la date " + date + " :</h3>");
						for (Spectacle s : spec) {
							out.println("<li><a href=\"?spectacle=" + s.getId() + "\">" + s.getNom() + "</a></li>");
						}
					}
				}

			}
		} catch (Exception e) {
			out.println("<p><i><font color=\"#FFFFFF\">Erreur de connexion à la base de données</i><br/>" + e + "</p>");
		}



		out.println("<hr/><p><font color=\"#FFFFFF\"><a href=\"/index.html\">Accueil</a></p>");
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
		return "Retourne le programme du théatre";
	}
}
