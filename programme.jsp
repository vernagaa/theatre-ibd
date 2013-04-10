<%@ page import="utils.Utilitaires"%>
<%@ page import=" accesBD.BDCategories"%>
<%@ page import=" accesBD.BDPlaces"%>
<%@ page import=" accesBD.BDRepresentations"%>
<%@ page import=" accesBD.BDSpectacles"%>
<%@ page import=" java.io.IOException"%>
<%@ page import=" java.sql.Date"%>
<%@ page import=" java.util.List"%>
<%@ page import=" javax.servlet.ServletException"%>
<%@ page import=" javax.servlet.ServletOutputStream"%>
<%@ page import=" javax.servlet.http.HttpServlet"%>
<%@ page import=" javax.servlet.http.HttpServletRequest"%>
<%@ page import=" javax.servlet.http.HttpServletResponse"%>
<%@ page import=" javax.servlet.http.HttpSession"%>
<%@ page import=" modele.Caddie"%>
<%@ page import=" modele.Categorie"%>
<%@ page import=" modele.Representation"%>
<%@ page import=" modele.Reservation"%>
<%@ page import=" modele.Spectacle"%>
<%@ page import=" modele.Utilisateur"%>
<%@ page import=" utils.Utilitaires"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.html"%>
<%
			utils.Constantes.Home = getServletContext().getRealPath("/");
			try {
			Utilisateur user = Utilitaires.Identification();
			if (user != null) {
				Integer specId = null;
				Date date = null;
				boolean valid = false;
				Integer nbPlaces = null;
				String categorie = request.getParameter("categorie");

				if (request.getParameter("spectacle") != null) {
					specId = Integer.parseInt(request.getParameter("spectacle"));
				}
				if (request.getParameter("date") != null) {
					date = Utilitaires.toDate(request.getParameter("date"), "dd-MM-yyyy HH:mm");
				}
				if (request.getParameter("caddie") != null && request.getParameter("caddie").equals("true")) {
					valid = true;
				}
				if (request.getParameter("nbPlaces") != null) {
					nbPlaces = Integer.parseInt(request.getParameter("nbPlaces"));
				}
%>
<h3>Les spectacles :</h3>
<ul>
	<%
	List<Spectacle> spectacles = BDSpectacles.getSpectacle(user);
	List<Representation> representations = BDRepresentations.getRepresentation(user);
	for (Spectacle s : spectacles) {
	%>
	<li><a href="?spectacle=<%= s.getId() %>"><%= s.getNom() %></a></li>
	<%
	}
	%>
</ul>

<h3>Les représentations :</h3>
<ul>
	<%
	for (Representation r : representations) {
	%>
	<li><a href="?date=<%= Utilitaires.toStringBd(r.getDate()) %>"><%= Utilitaires.toString(r.getDate()) %></a></li>
	<%
	}
	%>
</ul>

<%
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
%>
<hr/><h3>Représentations du spectacle <%= spec.getNom() %></h3>
<ul>
	<%
	boolean b = true;
	for (Representation r : representations) {
		if (r.getSpectacle() == specId) {
			b = false;
	%>
	<li>
		<a href="/servlet/PlacesDispoServlet?date=<%= Utilitaires.toStringBd(r.getDate()) %>&spectacle=<%= specId %>"><%= Utilitaires.toString(r.getDate()) %></a>
		<a href="/servlet/NouvelleReservationServlet?dateR=<%= Utilitaires.toStringBd(r.getDate()) %>&spectacle=<%= specId %>">[Réserver]</a>
		<form method="post" action="ProgrammeServlet">
			<input type="hidden" name="caddie" value="true"/>
			<input type="hidden" name="spectacle" value="<%= specId %>"/>
			<input type="hidden" name="date" value="<%= Utilitaires.toStringBd(r.getDate())%>"/>
			<%
			if (categories.isEmpty()) {
			%>
			<select>
				<option>Aucune catégorie disponible</option>
				<%
				} else {
				%>
				<select name="categorie">
					<%
						for (Categorie c : categories) {
					%>
					<option value="<%= c.getCategorie() %>">
						<%= c.getCategorie() %> (<%= c.getPrix() %>&euro;) : <%= BDPlaces.getNbPlacesLibres(user, specId, r.getDate(), c.getCategorie()) %> places disponibles
					</option>
					<%
					}
					%>
				</select>
				<%
				}
				%>
				<input type="text" name="nbPlaces" value="1"/>
				<input type="submit" value="Ajouter au caddie">
				</form>
				<%
				if (valid && resa != null && resa.getRepres().getSpectacle() == r.getSpectacle() && resa.getRepres().getDate().equals(r.getDate())) {
				%>
				<span style="color: red;">Ajouté au <a href="/servlet/CaddieServlet">caddie</a></span>
				<%
				}
				%>
				</li>
				<%
				}
			}
				%>
				</ul>
				<%if (b) {
				%>
				<p><em>Aucune représentation</em></p>
				<%
				}
			}
		} else if (date != null) {
			List<Spectacle> spec = BDSpectacles.getSpectacle(user, date);

			if (spec != null) {
				%>
				<hr/><h3>Spectacles pour la date <%= Utilitaires.toString(date) %> :</h3>
				<ul>
					<%
					for (Spectacle s : spec) {
					%>
					<li><a href="?spectacle=<%= s.getId() %>"><%= s.getNom() %></a></li>
					<%
					}
					%>
				</ul>
				<%
			}
		}

	}
	} catch (Exception e) {
				%>
				<p><em>Erreur de connexion à la base de données</em><br/><%= e %></p>
				<%
				}
				%>
				<%@ include file="footer.html" %>