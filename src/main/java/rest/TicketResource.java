package rest;

import java.util.List;
import java.util.stream.Collectors;

import dao.ConcertDao;
import dao.TicketDao;
import dao.UtilisateurDao;
import domain.Ticket;
import domain.TicketDTO;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@Path("ticket")
@Produces({ "application/json" })
public class TicketResource {

	private TicketDao ticketDao = new TicketDao(); // Création d'une instance du DAO

	@GET
	@Path("/{ticketId}")
	public TicketDTO getTicketById(@PathParam("ticketId") Long ticketId) {
		// Utilisation du DAO pour récupérer le ticket depuis la base de données
		Ticket ticket = ticketDao.findOne(ticketId);
		if (ticket == null) {
			// Si le concert n'existe pas, retourner une erreur
			throw new WebApplicationException("ticket not found", Response.Status.NOT_FOUND);
		}
		TicketDTO dto = new TicketDTO();
		dto.setId(ticket.getId());

		if (ticket.getUtilisateur() != null) {
			dto.setUtilisateurId(ticket.getUtilisateur().getId());
		}
		if (ticket.getConcert() != null) {
			dto.setConcertId(ticket.getConcert().getId());
		}
		return dto;
	}

	@GET
	@Path("/")
	public List<TicketDTO> getConcerts() {
		// Récupérer tous les concerts depuis le DAO
		List<Ticket> tickets = ticketDao.findAll();

		// Convertir la liste des concerts en une liste de ConcertDTO
		List<TicketDTO> ticketsDTOs = tickets.stream().map(ticket -> {
			TicketDTO dto = new TicketDTO();
			dto.setId(ticket.getId());

			if (ticket.getUtilisateur() != null) {
				dto.setUtilisateurId(ticket.getUtilisateur().getId());
			}
			if (ticket.getConcert() != null) {
				dto.setConcertId(ticket.getConcert().getId());
			}
			return dto;
		}).collect(Collectors.toList());

		return ticketsDTOs;
	}

	@POST
	@Consumes("application/json")
	public Response addTicket(@Parameter TicketDTO ticketDTO) {
		Ticket ticket = new Ticket();
		ticket.setId(ticketDTO.getId());

		// Save the concert
		ticketDao.save(ticket);

		return Response.ok().entity("SUCCESS").build();
	}
	
	@PUT
	@Path("/{ticketId}")
	@Consumes("application/json")
	public Response updateTicket(@PathParam("ticketId") Long ticketId, TicketDTO ticketDTO) {
		UtilisateurDao utilisateurDao = new UtilisateurDao();
		ConcertDao concertDao = new ConcertDao();
	    Ticket ticket = ticketDao.findOne(ticketId);
	    if (ticket == null) {
	        throw new WebApplicationException("Ticket not found", Response.Status.NOT_FOUND);
	    }

	    if (ticketDTO.getUtilisateurId() != null) {
	        ticket.setUtilisateur(utilisateurDao.findOne(ticketDTO.getUtilisateurId()));
	    }
	    if (ticketDTO.getConcertId() != null) {
	        ticket.setConcert(concertDao.findOne(ticketDTO.getConcertId()));
	    }

	    ticketDao.update(ticket);
	    return Response.ok().entity("Ticket updated successfully").build();
	}

	@DELETE
	@Path("/{ticketId}")
	public Response deleteTicket(@PathParam("ticketId") Long ticketId) {
	    Ticket ticket = ticketDao.findOne(ticketId);
	    if (ticket == null) {
	        throw new WebApplicationException("Ticket not found", Response.Status.NOT_FOUND);
	    }

	    ticketDao.delete(ticket);
	    return Response.ok().entity("Ticket deleted successfully").build();
	}
}