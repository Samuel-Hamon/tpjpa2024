package rest;

import java.util.List;
import java.util.stream.Collectors;

import dao.OrganisateurDao;
import domain.Organisateur;
import domain.OrganisateurDTO;
import domain.Concert;
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

@Path("organisateur")
@Produces({ "application/json" })
public class OrganisateurResource {

	private OrganisateurDao organisateurDao = new OrganisateurDao(); // Création d'une instance du DAO

	@GET
	@Path("/{organisateurId}")
	@Operation(summary = "Récupérer un organisateur", description = "Retourne l'organisateur correspondant à l'ID fourni.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Organisateur trouvé"),
        @ApiResponse(responseCode = "404", description = "Organisateur non trouvé")
    })
	public OrganisateurDTO getOrganisateurById(@PathParam("organisateurId") Long organisateurId) {
		// Utilisation du DAO pour récupérer le concert depuis la base de données
		Organisateur organisateur = organisateurDao.findOne(organisateurId);
		if (organisateur == null) {
			// Si le concert n'existe pas, retourner une erreur
			throw new WebApplicationException("Organisateur not found", Response.Status.NOT_FOUND);
		}
		OrganisateurDTO dto = new OrganisateurDTO();
		dto.setId(organisateur.getId());
		dto.setNom(organisateur.getNom());
		dto.setPrenom(organisateur.getPrenom());
		dto.setNationalite(organisateur.getNationalite());
		dto.setDateNaissance(organisateur.getDateNaissance());
		dto.setEmail(organisateur.getEmail());
		dto.setTel(organisateur.getTel());

		if (organisateur.getConcerts() != null) {
			dto.setConcertsIds(organisateur.getConcerts().stream().map(Concert::getId).collect(Collectors.toList()));
		}

		return dto;
	}

	@GET
	@Path("/")
	@Operation(summary = "Lister les organisateurs", description = "Retourne la liste de tous les organisateurs.")
    @ApiResponse(responseCode = "200", description = "Liste des organisateurs retournée")
	public List<OrganisateurDTO> getOrganisateurs() {
		// Récupérer tous les concerts depuis le DAO
		List<Organisateur> organisateurs = organisateurDao.findAll();

		// Convertir la liste des concerts en une liste de ConcertDTO
		List<OrganisateurDTO> organisateurDTOs = organisateurs.stream().map(organisateur -> {
			OrganisateurDTO dto = new OrganisateurDTO();
			dto.setId(organisateur.getId());
			dto.setNom(organisateur.getNom());
			dto.setPrenom(organisateur.getPrenom());
			dto.setNationalite(organisateur.getNationalite());
			dto.setDateNaissance(organisateur.getDateNaissance());
			dto.setEmail(organisateur.getEmail());
			dto.setTel(organisateur.getTel());

			if (organisateur.getConcerts() != null) {
				dto.setConcertsIds(organisateur.getConcerts().stream().map(Concert::getId).collect(Collectors.toList()));
			}

			return dto;
		}).collect(Collectors.toList());

		return organisateurDTOs;
	}

	@POST
	@Consumes("application/json")
	@Operation(summary = "Ajouter un organisateur", description = "Crée un nouvel organisateur à partir des informations fournies.")
    @ApiResponse(responseCode = "201", description = "Organisateur ajouté avec succès")
	public Response addOrganisateur(@Parameter OrganisateurDTO organisateurDTO) {
	    // Création d'un nouvel objet Organisateur
	    Organisateur organisateur = new Organisateur();
	    organisateur.setNom(organisateurDTO.getNom());
	    organisateur.setPrenom(organisateurDTO.getPrenom());
	    organisateur.setNationalite(organisateurDTO.getNationalite());
	    organisateur.setDateNaissance(organisateurDTO.getDateNaissance());
	    organisateur.setEmail(organisateurDTO.getEmail());
	    organisateur.setTel(organisateurDTO.getTel());

	    // Sauvegarde de l'organisateur
	    organisateurDao.save(organisateur);

	    return Response.status(Response.Status.CREATED)
	            .entity("Organisateur ajouté avec succès.")
	            .build();
	}
	
	@PUT
	@Path("/{organisateurId}")
	@Consumes("application/json")
	@Operation(summary = "Modifier un organisateur", description = "Met à jour l'organisateur identifié par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Organisateur mis à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Organisateur non trouvé")
    })
	public Response updateOrganisateur(@PathParam("organisateurId") Long organisateurId, OrganisateurDTO organisateurDTO) {
	    Organisateur organisateur = organisateurDao.findOne(organisateurId);
	    if (organisateur == null) {
	        throw new WebApplicationException("Organisateur not found", Response.Status.NOT_FOUND);
	    }

	    organisateur.setNom(organisateurDTO.getNom());
	    organisateur.setPrenom(organisateurDTO.getPrenom());
	    organisateur.setNationalite(organisateurDTO.getNationalite());
	    organisateur.setDateNaissance(organisateurDTO.getDateNaissance());
	    organisateur.setEmail(organisateurDTO.getEmail());
	    organisateur.setTel(organisateurDTO.getTel());

	    organisateurDao.update(organisateur);
	    return Response.ok().entity("Organisateur updated successfully").build();
	}

	@DELETE
	@Path("/{organisateurId}")
	@Operation(summary = "Supprimer un organisateur", description = "Supprime l'organisateur identifié par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Organisateur supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Organisateur non trouvé")
    })
	public Response deleteOrganisateur(@PathParam("organisateurId") Long organisateurId) {
	    Organisateur organisateur = organisateurDao.findOne(organisateurId);
	    if (organisateur == null) {
	        throw new WebApplicationException("Organisateur not found", Response.Status.NOT_FOUND);
	    }

	    organisateurDao.delete(organisateur);
	    return Response.ok().entity("Organisateur deleted successfully").build();
	}

}