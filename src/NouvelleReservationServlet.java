/*
 * @(#)NouvelleReservationServlet.java	1.0 2007/10/31
 * 
 */

import accesBD.BDCategories;
import accesBD.BDPlaces;
import accesBD.BDRepresentations;
import accesBD.BDSpectacles;
import accesBD.BDTickets;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modele.Categorie;
import modele.Representation;
import modele.Spectacle;
import modele.Ticket;
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
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />");
		out.println("<title> Réserver une place </title>");
		out.println("<style>"
				+ "body {color: darkslategray; background-image: radial-gradient(white, darkred 90%, black); padding: 20px;}"
				+ "div {width: 1000px; min-height: 1000px; margin: auto; }"
				+ "a {color: black; font-weight: bold;}"
				+ "a:hover {color: red;}"
				+ "</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("<div>");
		out.println("<h1> Réserver une place </h1>");

		try {
			Utilisateur user = Utilitaires.Identification();
			if (user != null) {
				Integer numS = null;
				Date dateR = null;
				String categorie = null;
				String valid = null;
				if (req.getParameter("spectacle") != null) {
					numS = Integer.parseInt(req.getParameter("spectacle"));
				}
				if (req.getParameter("dateR") != null) {
					dateR = Utilitaires.toDate(req.getParameter("dateR"), "dd-MM-yyyy HH:mm");
				}
				if (req.getParameter("categorie") != null) {
					categorie = req.getParameter("categorie");
				}
				if (req.getParameter("valid") != null) {
					valid = req.getParameter("valid");
				}

				if (valid != null && numS != null && dateR != null && categorie != null && valid.equals("true")) {
					Ticket t = BDTickets.reserver(user, numS, dateR, categorie);

					if (t != null) {
						out.println("<h3>Réservation effectuée</h3>");
						out.println("<p>"
								+ "Spectacle : " + BDSpectacles.getSpectacle(user, t.getNumS()).getNom() + "<br/>"
								+ "Date : " + Utilitaires.toString(t.getDateRep()) + "<br/>"
								+ "Place : " + t.getNoPlace() + "<br/>"
								+ "Rang : " + t.getNoRang() + "<br/>"
								+ "Prix : " + BDCategories.getCategorie(user, categorie).getPrix() + "<br/>"
								+ "</p>");
					} else {
						out.println("<p>La réservation n'a pas pu être effectuée</p>");
					}
					out.println("<p><a href=\"NouvelleReservationServlet\">Retour aux réservations</a></p>");
				} else {
					// choix du spectacle
					List<Spectacle> spectacles = BDSpectacles.getSpectacle(user);

					out.println("</ul>");
					out.println("<p>");
					out.print("<form action=\"");
					out.print("NouvelleReservationServlet\" ");
					out.println("method=\"get\">");
					out.println("<input type=\"hidden\" id=\"valid\" name=\"valid\">");
					out.println("<table>");
					out.println("<tr><td><label for=\"spectacle\">Spectacle :</label></td>");
					if (spectacles.isEmpty()) {
						out.println("<td><select id=\"spectacle\">");
						out.println("<option>Aucun spectacle disponible</option>");
					} else {
						out.println("<td><select id=\"spectacle\" name=\"spectacle\" onchange=\"submit()\">");
						for (Spectacle s : spectacles) {
							out.println("<option ");
							if (numS != null && numS == s.getId()) {
								out.print("selected=\"true\" ");
							}
							out.print("value=\"" + s.getId() + "\">" + s.getNom() + "</option>");
						}
					}
					out.println("</select></td></tr>");

					// choix de la représentation
					if (numS != null) {
						//TODO afficher les représentation à venir uniquement
						List<Representation> representations = BDRepresentations.getRepresentations(user, numS);
						List<Categorie> categories = BDCategories.getCategorie(user);

						out.println("<tr><td><label for=\"dateR\">Date de représentation :</label></td>");
						if (representations.isEmpty()) {
							out.println("<td><select id=\"dateR\">");
							out.println("<option>Aucune représentation disponible</option>");
						} else {
							out.println("<td><select id=\"dateR\" name=\"dateR\" onchange=\"submit()\">");
							for (Representation r : representations) {
								out.println("<option ");
								if (dateR != null && r.getDate().compareTo(dateR) == 0) {
									out.print("selected=\"true\" ");
								}
								out.print("value=\"" + Utilitaires.toStringBd(r.getDate()) + "\">" + Utilitaires.toString(r.getDate()) + "</option>");
							}
							out.println("</select></td></tr>");

							if (dateR != null) {
								out.println("<tr><td><label for=\"categorie\">Categorie :</label></td>");
								if (categories.isEmpty()) {
									out.println("<td><select id=\"categorie\">");
									out.println("<option>Aucune catégorie disponible</option>");
								} else {
									out.println("<td><select id=\"categorie\" name=\"categorie\" onchange=\"submit()\">");
									for (Categorie c : categories) {
										out.println("<option ");
										if (categorie != null && categorie.equals(c.getCategorie())) {
											out.print("selected=\"true\" ");
										}
										out.print("value=\"" + c.getCategorie() + "\">");
										out.print(c.getCategorie() + " (" + c.getPrix() + "&euro;) : " + BDPlaces.getNbPlacesLibres(user, numS, dateR, c.getCategorie()) + " places disponibles");
										out.print("</option>");
									}
								}
								if (categorie != null) {
									out.print("<input type=\"button\""
											+ " value=\"Réserver une place\""
											+ " onclick=\"this.form.valid.value='true';this.form.method='post';submit();\""
											+ "/>");
								}
							}
						}
						out.println("</select></td></tr>");
					}

					out.println("<tr><td></td><td><input type=submit></td></tr>");
					out.println("</table>");
					out.println("</form>");

				}
			}
		} catch (Exception e) {
			out.println("<p><i>Erreur de connexion à la base de données</i><br/>" + e + "</p>");
		}

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
