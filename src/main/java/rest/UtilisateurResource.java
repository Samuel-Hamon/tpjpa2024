package rest;

import java.util.List;
import java.util.stream.Collectors;

import dao.TicketDao;
import dao.UtilisateurDao;
import domain.Utilisateur;
import domain.UtilisateurDTO;
import domain.Ticket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@Path("utilisateur")
@Produces({ "application/json" })
public class UtilisateurResource {

	private UtilisateurDao utilisateurDao = new UtilisateurDao(); // Création d'une instance du DAO
	private TicketDao ticketDao = new TicketDao();

	@GET
	@Path("/{utilisateurId}")
	@Operation(summary = "Récupérer un utilisateur", description = "Retourne l'utilisateur correspondant à l'ID fourni.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Utilisateur trouvé"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
	public UtilisateurDTO getUtilisateurById(@PathParam("utilisateurId") Long utilisateurId) {
		// Utilisation du DAO pour récupérer le concert depuis la base de données
		Utilisateur utilisateur = utilisateurDao.findOne(utilisateurId);
		if (utilisateur == null) {
			// Si le concert n'existe pas, retourner une erreur
			throw new WebApplicationException("utilisateur not found", Response.Status.NOT_FOUND);
		}
		UtilisateurDTO dto = new UtilisateurDTO();
		dto.setId(utilisateur.getId());
		dto.setNom(utilisateur.getNom());
		dto.setPrenom(utilisateur.getPrenom());
		dto.setNationalite(utilisateur.getNationalite());
		dto.setDateNaissance(utilisateur.getDateNaissance());
		dto.setEmail(utilisateur.getEmail());
		dto.setTel(utilisateur.getTel());

		if (utilisateur.getTickets() != null) {
			dto.setTicketsIds(utilisateur.getTickets().stream().map(Ticket::getId).collect(Collectors.toList()));
		}

		return dto;
	}

	@GET
	@Path("/")
	@Operation(summary = "Lister les utilisateurs", description = "Retourne la liste de tous les utilisateurs.")
    @ApiResponse(responseCode = "200", description = "Liste des utilisateurs retournée")
	public List<UtilisateurDTO> getUtilisateurs() {
		// Récupérer tous les concerts depuis le DAO
		List<Utilisateur> utilisateurs = utilisateurDao.findAll();

		// Convertir la liste des concerts en une liste de ConcertDTO
		List<UtilisateurDTO> utilisateurDTOs = utilisateurs.stream().map(utilisateur -> {
			UtilisateurDTO dto = new UtilisateurDTO();
			dto.setNom(utilisateur.getNom());
			dto.setPrenom(utilisateur.getPrenom());
			dto.setNationalite(utilisateur.getNationalite());
			dto.setDateNaissance(utilisateur.getDateNaissance());
			dto.setEmail(utilisateur.getEmail());
			dto.setTel(utilisateur.getTel());

			if (utilisateur.getTickets() != null) {
				dto.setTicketsIds(utilisateur.getTickets().stream().map(Ticket::getId).collect(Collectors.toList()));
			}

			return dto;
		}).collect(Collectors.toList());

		return utilisateurDTOs;
	}

	@POST
	@Consumes("application/json")
	@Operation(summary = "Ajouter un utilisateur", description = "Crée un nouvel utilisateur à partir des informations fournies.")
    @ApiResponse(responseCode = "200", description = "Utilisateur ajouté avec succès")
	public Response addUtilisateur(@Parameter UtilisateurDTO utilisateurDTO) {
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setNom(utilisateurDTO.getNom());
		utilisateur.setPrenom(utilisateurDTO.getPrenom());
		utilisateur.setNationalite(utilisateurDTO.getNationalite());
		utilisateur.setDateNaissance(utilisateurDTO.getDateNaissance());
		utilisateur.setEmail(utilisateurDTO.getEmail());
		utilisateur.setTel(utilisateurDTO.getTel());
		
		utilisateurDao.save(utilisateur);

		// Récupération et affectation des tickets
		List<Long> ticketsIds = utilisateurDTO.getTicketsIds();
		if (ticketsIds != null && !ticketsIds.isEmpty()) {
			for (Long ticketId : ticketsIds) {
				Ticket ticket = ticketDao.findOne(ticketId); // Récupérer le concert par ID
				if (ticket != null) {
					ticket.setUtilisateur(utilisateur);
					ticketDao.update(ticket);
				}
			}
		}

		return Response.ok().entity("Utilisateur ajouté avec succès.").build();
	}

	@PUT
	@Path("/{utilisateurId}")
	@Consumes("application/json")
	@Operation(summary = "Modifier un utilisateur", description = "Met à jour l'utilisateur identifié par son ID avec les nouvelles informations.")
    @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour avec succès")
	public Response updateUtilisateur(@PathParam("utilisateurId") Long utilisateurId, UtilisateurDTO utilisateurDTO) {
		Utilisateur utilisateur = utilisateurDao.findOne(utilisateurId);
		if (utilisateur == null) {
			throw new WebApplicationException("Utilisateur not found", Response.Status.NOT_FOUND);
		}

		if (utilisateurDTO.getNom() != null) {
			utilisateur.setNom(utilisateurDTO.getNom());
		}
		if (utilisateurDTO.getPrenom() != null) {
			utilisateur.setPrenom(utilisateurDTO.getPrenom());
		}
		if (utilisateurDTO.getNationalite() != null) {
			utilisateur.setNationalite(utilisateurDTO.getNationalite());
		}
		if (utilisateurDTO.getDateNaissance() != null) {
			utilisateur.setDateNaissance(utilisateurDTO.getDateNaissance());
		}
		if (utilisateurDTO.getEmail() != null) {
			utilisateur.setEmail(utilisateurDTO.getEmail());
		}
		if (utilisateurDTO.getTel() != null) {
			utilisateur.setTel(utilisateurDTO.getTel());
		}
		if (utilisateurDTO.getTicketsIds() != null) {
			// Mise à jour des tickets
			List<Long> ticketsIds = utilisateurDTO.getTicketsIds();
			if (ticketsIds != null && !ticketsIds.isEmpty()) {
				for (Long ticketId : ticketsIds) {
					Ticket ticket = ticketDao.findOne(ticketId); // Récupérer le concert par ID
					if (ticket != null) {
						ticket.setUtilisateur(utilisateur);
						ticketDao.update(ticket);
					}
				}
			}
		}

		utilisateurDao.update(utilisateur);
		return Response.ok().entity("Utilisateur updated successfully").build();

	}

	@DELETE
	@Path("/{utilisateurId}")
	@Operation(summary = "Supprimer un utilisateur", description = "Supprime l'utilisateur identifié par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Utilisateur supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
	public Response deleteUtilisateur(@PathParam("utilisateurId") Long utilisateurId) {
		Utilisateur utilisateur = utilisateurDao.findOne(utilisateurId);
		if (utilisateur == null) {
			throw new WebApplicationException("Utilisateur not found", Response.Status.NOT_FOUND);
		}
		List<Ticket> tickets = utilisateur.getTickets();
		for (Ticket ticket : tickets) {
			ticket.setUtilisateur(null); 
		    ticketDao.update(ticket);
		}
		utilisateurDao.delete(utilisateur);
		return Response.ok().entity("Utilisateur deleted successfully").build();
	}

}