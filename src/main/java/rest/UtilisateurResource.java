package rest;

import java.util.List;
import java.util.stream.Collectors;

import dao.UtilisateurDao;
import domain.Utilisateur;
import domain.UtilisateurDTO;
import domain.Ticket;
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

@Path("utilisateur")
@Produces({ "application/json" })
public class UtilisateurResource {

	private UtilisateurDao utilisateurDao = new UtilisateurDao(); // Création d'une instance du DAO

	@GET
	@Path("/{utilisateurId}")
	public UtilisateurDTO getUtilisateurById(@PathParam("utilisateurId") Long utilisateurId) {
		// Utilisation du DAO pour récupérer le concert depuis la base de données
		Utilisateur utilisateur = utilisateurDao.findOne(utilisateurId);
		if (utilisateur == null) {
			// Si le concert n'existe pas, retourner une erreur
			throw new WebApplicationException("utilisateur not found", Response.Status.NOT_FOUND);
		}
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
	}

	@GET
	@Path("/")
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
	public Response addutilisateur(@Parameter UtilisateurDTO utilisateurDTO) {
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setPrenom(utilisateurDTO.getPrenom());
		utilisateur.setNationalite(utilisateurDTO.getNationalite());
		utilisateur.setDateNaissance(utilisateurDTO.getDateNaissance());
		utilisateur.setEmail(utilisateurDTO.getEmail());
		utilisateur.setTel(utilisateurDTO.getTel());


		// Save the concert
		utilisateurDao.save(utilisateur);

		return Response.ok().entity("SUCCESS").build();
	}
	
	@PUT
	@Path("/{utilisateurId}")
	@Consumes("application/json")
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

	    utilisateurDao.update(utilisateur);
	    return Response.ok().entity("Utilisateur updated successfully").build();
	}

	@DELETE
	@Path("/{utilisateurId}")
	public Response deleteUtilisateur(@PathParam("utilisateurId") Long utilisateurId) {
	    Utilisateur utilisateur = utilisateurDao.findOne(utilisateurId);
	    if (utilisateur == null) {
	        throw new WebApplicationException("Utilisateur not found", Response.Status.NOT_FOUND);
	    }

	    utilisateurDao.delete(utilisateur);
	    return Response.ok().entity("Utilisateur deleted successfully").build();
	}

}