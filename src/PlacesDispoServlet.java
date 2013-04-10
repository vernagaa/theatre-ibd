/*
 * @(#)PlacesDispoServlet.java	1.0 2007/10/31
 * 
 * Copyright (c) 2007 Sara Bouchenak.
 */

import accesBD.BDPlaces;
import accesBD.BDRepresentations;
import accesBD.BDSpectacles;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modele.Place;
import modele.Representation;
import modele.Spectacle;
import modele.Utilisateur;
import utils.Utilitaires;

/**
 * Places Dispo Servlet.
 */
public class PlacesDispoServlet extends HttpServlet {

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
		//TODO rendre moins dégueulasse
		ServletOutputStream out = res.getOutputStream();
		utils.Constantes.Home = getServletContext().getRealPath("/");

		res.setContentType("text/html");
		
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />");
		out.println("<title> Places disponibles </title>");
		out.println("<style>"
				+ "body {color: darkslategray; background-image: radial-gradient(white, darkred 90%, black); padding: 20px;}"
				+ "div {width: 1000px; min-height: 1000px; margin: auto; }"
				+ "a {color: black; font-weight: bold;}"
				+ "a:hover {color: red;}"
				+ "</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("<div>");
		out.println("<h1> Places disponibles </h1>");

		// Ecrire une nouvelle servlet qui permet de consulter l'ensemble
		// des places disponibles pour une représentation donnée, chaque place étant décrite par
		// le numéro du rang et le numéro de place dans le rang. Intégrer cette servlet à l'application et la tester.

		try {
			Utilisateur user = Utilitaires.Identification();
			if (user != null) {
				if (req.getParameter("spectacle") != null && req.getParameter("date") != null) {
					int specId = Integer.parseInt(req.getParameter("spectacle"));
					String date = req.getParameter("date");

					Representation repres = BDRepresentations.getRepresentation(user, specId, Utilitaires.toDate(date, "dd-MM-yyyy HH:mm"));
					Spectacle spectacle = BDSpectacles.getSpectacle(user, specId);

					if (repres != null && spectacle != null) {
						out.println("<h3>Places disponibles pour la représentation du spectacle " + spectacle.getNom() + " (" + Utilitaires.toString(repres.getDate()) + ") :</h3>");

						List<Place> places = BDPlaces.getPlacesLibres(user, specId, Utilitaires.toDate(date, "dd-MM-yyyy HH:mm"));
						if (places.isEmpty()) {
							out.println("<p><em>Aucune place disponible</em></p>");
						} else {
							out.println("<ul>");
							for (Place p : places) {
								out.println("<li>place " + p.getNoPlace() + " rang " + p.getNoRang() + " zone " + p.getCategorie() + "</li>");
							}
							out.println("</ul>");
						}
						out.println("<br/><p><font color=\"#FFFFFF\"><a href=\"/servlet/ProgrammeServlet?spectacle=" + specId + "\">Retour au programme</a></p>");
					} else if (repres == null) {
						out.println("<p>représentation nulle</p>");
					}
					if (spectacle == null) {
						out.println("<p>spectacle nul</p>");
					}
				}
			}
		} catch (Exception e) {
			out.println("<p><i><font color=\"#FFFFFF\">Erreur de connexion à la base de données</i><br/>" + e + "</p>");
		}



		out.println("<hr/><p><a href=\"/index.html\">Accueil</a></p>");
		out.println("<hr/><p><a href=\"/servlet/ProgrammeServlet\">Programme</a></p>");
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
		return "Retourne les places disponibles d'une représentation donnée";
	}
}
