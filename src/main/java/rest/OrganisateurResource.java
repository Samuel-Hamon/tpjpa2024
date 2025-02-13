package rest;

import java.util.List;
import java.util.stream.Collectors;

import dao.OrganisateurDao;
import domain.Organisateur;
import domain.OrganisateurDTO;
import domain.Concert;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
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
	public OrganisateurDTO getOrganisateurById(@PathParam("organisateurId") Long organisateurId) {
		// Utilisation du DAO pour récupérer le concert depuis la base de données
		Organisateur organisateur = organisateurDao.findOne(organisateurId);
		if (organisateur == null) {
			// Si le concert n'existe pas, retourner une erreur
			throw new WebApplicationException("Organisateur not found", Response.Status.NOT_FOUND);
		}
		OrganisateurDTO dto = new OrganisateurDTO();
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
	public List<OrganisateurDTO> getOrganisateurs() {
		// Récupérer tous les concerts depuis le DAO
		List<Organisateur> organisateurs = organisateurDao.findAll();

		// Convertir la liste des concerts en une liste de ConcertDTO
		List<OrganisateurDTO> organisateurDTOs = organisateurs.stream().map(organisateur -> {
			OrganisateurDTO dto = new OrganisateurDTO();
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
	public Response addOrganisateur(@Parameter OrganisateurDTO organisateurDTO) {
		Organisateur organisateur = new Organisateur();
		organisateur.setPrenom(organisateurDTO.getPrenom());
		organisateur.setNationalite(organisateurDTO.getNationalite());
		organisateur.setDateNaissance(organisateurDTO.getDateNaissance());
		organisateur.setEmail(organisateurDTO.getEmail());
		organisateur.setTel(organisateurDTO.getTel());


		organisateurDao.save(organisateur);

		return Response.ok().entity("SUCCESS").build();
	}

}