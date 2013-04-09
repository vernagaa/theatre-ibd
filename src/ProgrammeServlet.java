/*
 * @(#)ProgrammeServlet.java	1.0 2007/10/31
 * 
 * Copyright (c) 2007 Sara Bouchenak.
 */

import accesBD.BDCategories;
import accesBD.BDPlaces;
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
import javax.servlet.http.HttpSession;
import modele.Caddie;
import modele.Categorie;
import modele.Representation;
import modele.Reservation;
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
		HttpSession session = req.getSession(true);
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
				Integer specId = null;
				Date date = null;
				boolean valid = false;
				Integer nbPlaces = null;
				String categorie = req.getParameter("categorie");

				if (req.getParameter("spectacle") != null) {
					specId = Integer.parseInt(req.getParameter("spectacle"));
				}
				if (req.getParameter("date") != null) {
					date = Utilitaires.toDate(req.getParameter("date"), "dd-MM-yyyy HH:mm");
				}
				if (req.getParameter("caddie") != null && req.getParameter("caddie").equals("true")) {
					valid = true;
				}
				if (req.getParameter("nbPlaces") != null) {
					nbPlaces = Integer.parseInt(req.getParameter("nbPlaces"));
				}

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

				if (specId != null) {
					Spectacle spec = BDSpectacles.getSpectacle(user, specId);

					if (spec != null) {
						// ajout au caddie
						Reservation resa = null;
						if (valid && date != null && categorie != null && nbPlaces != null && nbPlaces >= 1) {
							Caddie caddie = (Caddie) session.getAttribute("caddie");
							if (caddie == null) {
								caddie = new Caddie();
							}
							Representation repres = BDRepresentations.getRepresentation(user, specId, date);
							Categorie categ = BDCategories.getCategorie(user, categorie);
							if (repres != null && categ != null) {
								resa = caddie.addReservation(repres, categ, nbPlaces);
								session.setAttribute("caddie", caddie);
							}
						}
						List<Categorie> categories = BDCategories.getCategorie(user);

						out.println("<hr/><h3>Représentations du spectacle " + spec.getNom() + " :</h3>");
						boolean b = true;
						for (Representation r : representations) {
							if (r.getSpectacle() == specId) {
								b = false;
								out.println("<li>");
								out.println("<a href=\"/servlet/PlacesDispoServlet?date=" + Utilitaires.toStringBd(r.getDate()) + "&spectacle=" + specId + "\">" + Utilitaires.toString(r.getDate()) + "</a>");
								out.println("<a href=\"/servlet/NouvelleReservationServlet?dateR=" + Utilitaires.toStringBd(r.getDate()) + "&spectacle=" + specId + "\">[Réserver]</a>");
								out.println("<form method=\"post\" action=\"ProgrammeServlet\">"
										+ "<input type=\"hidden\" name=\"caddie\" value=\"true\"/>"
										+ "<input type=\"hidden\" name=\"spectacle\" value=\"" + specId + "\"/>"
										+ "<input type=\"hidden\" name=\"date\" value=\"" + Utilitaires.toStringBd(r.getDate()) + "\"/>");

								if (categories.isEmpty()) {
									out.println("<select>");
									out.println("<option>Aucune catégorie disponible</option>");
								} else {
									out.println("<select name=\"categorie\">");
									for (Categorie c : categories) {
										out.println("<option value=\"" + c.getCategorie() + "\">");
										out.print(c.getCategorie() + " (" + c.getPrix() + "&euro;) : " + BDPlaces.getNbPlacesLibres(user, specId, r.getDate(), c.getCategorie()) + " places disponibles");
										out.print("</option>");
									}
									out.println("</select>");
								}

								out.println("<input type=\"text\" name=\"nbPlaces\" value=\"1\"/>"
										+ "<input type=\"submit\" value=\"Ajouter au caddie\">"
										+ "</form>");
								if(valid && resa != null && resa.getRepres().getSpectacle() == r.getSpectacle() && resa.getRepres().getDate().equals(r.getDate())) {
									out.println("Ajouté au <a href=\"/servlet/CaddieServlet\">panier</a>");
								}
								out.println("</li>");
							}
						}
						if (b) {
							out.println("<p><em>Aucune représentation</em></p>");
						}
					}
				} else if (date != null) {
					List<Spectacle> spec = BDSpectacles.getSpectacle(user, date);

					if (spec != null) {
						out.println("<hr/><h3>Spectacles pour la date " + Utilitaires.toString(date) + " :</h3>");
						for (Spectacle s : spec) {
							out.println("<li><a href=\"?spectacle=" + s.getId() + "\">" + s.getNom() + "</a></li>");
						}
					}
				}

			}
		} catch (Exception e) {
			String o = "";
			for (StackTraceElement st : e.getStackTrace()) {
				o+="<br/>"+st;
			}
			out.println("<p><i><font color=\"#FFFFFF\">Erreur de connexion à la base de données</i><br/>" + e + o + "</p>");
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
