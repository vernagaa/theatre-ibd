<%@ page import="accesBD.BDCategories"%>
<%@ page import="accesBD.BDRepresentations"%>
<%@ page import="accesBD.BDSpectacles"%>
<%@ page import="accesBD.BDTickets"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.sql.Date"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List"%>
<%@ page import="javax.servlet.ServletException"%>
<%@ page import="javax.servlet.ServletOutputStream"%>
<%@ page import="javax.servlet.http.HttpServlet"%>
<%@ page import="javax.servlet.http.HttpServletRequest"%>
<%@ page import="javax.servlet.http.HttpServletResponse"%>
<%@ page import="javax.servlet.http.HttpSession"%>
<%@ page import="modele.Caddie"%>
<%@ page import="modele.Categorie"%>
<%@ page import="modele.Representation"%>
<%@ page import="modele.Reservation"%>
<%@ page import="modele.Ticket"%>
<%@ page import="modele.Utilisateur"%>
<%@ page import="utils.Utilitaires"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.html"%>
<%
		try {
			Utilisateur user = Utilitaires.Identification();
			if (user != null) {
				Caddie caddie = (Caddie) session.getAttribute("caddie");

				if (request.getParameter("valider") != null && request.getParameter("valider").equals("true")) {
					// validation du caddie
					float total = 0;
					List<Reservation> valide = new LinkedList<Reservation>();

%>
<h2>Validation</h2>
<h3>Places réserveés</h3>
<ul>
	<%
	for (Reservation r : caddie.getReservations()) {
	%>
	<li>
		Spectacle <strong><%= BDSpectacles.getSpectacle(user, r.getRepres().getSpectacle()).getNom() %></strong>
		le <%= Utilitaires.toString(r.getRepres().getDate()) %>
		(zone <em><%= r.getCateg().getCategorie() %></em>, <%= r.getPrixTotal() %> &euro;)

		<%
		try {
			List<Ticket> tickets = BDTickets.reserver(user, r);
			total += r.getPrixTotal();
		%>
		<ol>
			<%
			for (Ticket t : tickets) {
			%>
			<li>place <%=t.getNoPlace()%> rang <%= t.getNoRang() %></li>
			<%
			}
			%>
		</ol>
		<%
		valide.add(r);
	} catch (Exception e) {
		%>
		<span class="alert">Erreur : <%= e.getMessage() %></span>
		<%
	}
		%>
	</li>
	<%
}
	%>
</ul>
<p><strong>TOTAL : <%= total %>&euro;</strong></p>
<p><a href="CaddieServlet">Retour au caddie</a></p>
<%
caddie.removeReservations(valide);
session.setAttribute("caddie", caddie);
} else {
// affichage du caddie
Integer spectacle = null;
Date date = null;
Integer nbPlaces = null;
String categorie = request.getParameter("categorie");
String messageModif = null;
String messageSuppr = null;

if (caddie == null) {
	caddie = new Caddie();
}
if (request.getParameter("spectacle") != null) {
	spectacle = Integer.parseInt(request.getParameter("spectacle"));
}
if (request.getParameter("date") != null) {
	date = Utilitaires.toDate(request.getParameter("date"), "dd-MM-yyyy HH:mm");
}
if (request.getParameter("nbPlaces") != null) {
	nbPlaces = Integer.parseInt(request.getParameter("nbPlaces"));
}

if (spectacle != null && date != null && categorie != null) {
	Representation repres = BDRepresentations.getRepresentation(user, spectacle, date);
	Categorie categ = BDCategories.getCategorie(user, categorie);
	if (repres != null && categ != null) {
		if (nbPlaces == null) {
			// suppression
			caddie.removeReservation(repres, categ);
			session.setAttribute("caddie", caddie);
			messageSuppr = "Supprimé";
		} else if (nbPlaces >= 1) {
			// màj du nombre de places
			caddie.getReservation(repres, categ).setNbPlaces(nbPlaces);
			session.setAttribute("caddie", caddie);
			messageModif = "Modifié";
		}
	}
}


if (caddie.getReservations().isEmpty()) {
%>
<p><em>Votre caddie est vide</em></p>
<%
} else {
%>
<table>
	<tr>
		<th>Spectacle</th>
		<th>Date</th>
		<th>Catégorie</th>
		<th>Nombre</th>
		<th>Total</th>
		<th></th>
	</tr>
	<%
	for (Reservation r : caddie.getReservations()) {
	%>
	<tr>
		<td><%= BDSpectacles.getSpectacle(user, r.getRepres().getSpectacle()).getNom() %></td>
		<td><%= Utilitaires.toString(r.getRepres().getDate()) %></td>
		<td><%= r.getCateg().getCategorie() %></td>
		<td>
			<form action="CaddieServlet" method="post">
				<input type="text" name="nbPlaces" value="<%= r.getNbPlaces() %>" />
				<input type="hidden" name="spectacle" value="<%= r.getRepres().getSpectacle() %>"/>
				<input type="hidden" name="date" value="<%= Utilitaires.toStringBd(r.getRepres().getDate()) %>"/>
				<input type="hidden" name="categorie" value="<%= r.getCateg().getCategorie() %>"/>
				<input type="submit" value="Modifier"/>
			</form>
		</td>
		<td><%= r.getPrixTotal() %>&euro;</td>
		<td>
			<form action="CaddieServlet" method="post">
				<input type="hidden" name="spectacle" value="<%= r.getRepres().getSpectacle() %>"/>
				<input type="hidden" name="date" value="<%= Utilitaires.toStringBd(r.getRepres().getDate()) %>"/>
				<input type="hidden" name="categorie" value="<%= r.getCateg().getCategorie() %>"/>
				<input type="submit" value="Supprimer"/>
			</form>
		</td>

		<td class="alert">
			<%= (messageModif != null && r.equals(new Reservation(
			new Representation(spectacle, new Timestamp(date.getTime())),
			new Categorie(categorie, 0), nbPlaces)) ? messageModif : "") %>
		</td>
	</tr>
	<%
	}
	%>
</table>
<%
if (messageSuppr != null) {
%>
<p style="color: red;"><%= messageSuppr %></p>
<%
}
%>
<form action="CaddieServlet" method="post">
	<input type="hidden" name="valider" value="true"/>
	<input type="submit" value="Valider le caddie"/>
</form>
<%
}
}
}
} catch (Exception e) {
%>
<p><i><font color="#FFFFFF">Erreur de connexion à la base de données</i><br/><%= e %></p>
<%
}
%>
<%@ include file="footer.html" %>