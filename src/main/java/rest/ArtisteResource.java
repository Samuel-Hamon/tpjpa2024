package rest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dao.ArtisteDao;
import dao.ConcertDao;
import domain.Artiste;
import domain.ArtisteDTO;
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

@Path("artiste")
@Produces({ "application/json" })
public class ArtisteResource {

	private ArtisteDao artisteDao = new ArtisteDao(); // Création d'une instance du DAO

	@GET
	@Path("/{artisteId}")
	@Operation(summary = "Récupérer un artiste", description = "Retourne l'artiste correspondant à l'ID fourni.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artiste trouvé"),
        @ApiResponse(responseCode = "404", description = "Artiste non trouvé")
    })
	public ArtisteDTO getArtisteById(@PathParam("artisteId") Long artisteId) {
		// Utilisation du DAO pour récupérer le concert depuis la base de données
		Artiste artiste = artisteDao.findOne(artisteId);
		if (artiste == null) {
			// Si le concert n'existe pas, retourner une erreur
			throw new WebApplicationException("Artiste not found", Response.Status.NOT_FOUND);
		}
		ArtisteDTO dto = new ArtisteDTO();
		dto.setId(artiste.getId());
		dto.setNom(artiste.getNom());
		dto.setPrenom(artiste.getPrenom());
		dto.setNationalite(artiste.getNationalite());
		dto.setDateNaissance(artiste.getDateNaissance());
		dto.setEmail(artiste.getEmail());
		dto.setTel(artiste.getTel());

		if (artiste.getConcerts() != null) {
			dto.setConcertsIds(artiste.getConcerts().stream().map(Concert::getId).collect(Collectors.toList()));
		}

		return dto;
	}

	@GET
	@Path("/")
	@Operation(summary = "Lister les artistes", description = "Retourne la liste complète des artistes.")
    @ApiResponse(responseCode = "200", description = "Liste des artistes retournée")
	public List<ArtisteDTO> getArtistes() {
		// Récupérer tous les concerts depuis le DAO
		List<Artiste> artistes = artisteDao.findAll();

		// Convertir la liste des concerts en une liste de ConcertDTO
		List<ArtisteDTO> artisteDTOs = artistes.stream().map(artiste -> {
			ArtisteDTO dto = new ArtisteDTO();
			dto.setId(artiste.getId());
			dto.setNom(artiste.getNom());
			dto.setPrenom(artiste.getPrenom());
			dto.setNationalite(artiste.getNationalite());
			dto.setDateNaissance(artiste.getDateNaissance());
			dto.setEmail(artiste.getEmail());
			dto.setTel(artiste.getTel());

			if (artiste.getConcerts() != null) {
				dto.setConcertsIds(artiste.getConcerts().stream().map(Concert::getId).collect(Collectors.toList()));
			}

			return dto;
		}).collect(Collectors.toList());

		return artisteDTOs;
	}

	@POST
	@Consumes("application/json")
	@Operation(summary = "Ajouter un artiste", description = "Crée un nouvel artiste à partir des informations fournies.")
    @ApiResponse(responseCode = "200", description = "Artiste ajouté avec succès")
	public Response addArtiste(@Parameter ArtisteDTO artisteDTO) {
		ConcertDao concertDao = new ConcertDao();
		// Création de l'artiste à partir du DTO
		Artiste artiste = new Artiste();
		artiste.setNom(artisteDTO.getNom());
		artiste.setPrenom(artisteDTO.getPrenom());
		artiste.setNationalite(artisteDTO.getNationalite());
		artiste.setDateNaissance(artisteDTO.getDateNaissance());
		artiste.setEmail(artisteDTO.getEmail());
		artiste.setTel(artisteDTO.getTel());

		// Ajout des concerts s'ils existent
		if (artisteDTO.getConcertsIds() != null && !artisteDTO.getConcertsIds().isEmpty()) {
			List<Concert> concerts = new ArrayList<>();
			for (Long concertId : artisteDTO.getConcertsIds()) {
				Concert concert = concertDao.findOne(concertId); // Récupérer le concert par ID
				if (concert != null) {
					concerts.add(concert);
				}
			}
			artiste.setConcerts(concerts);
		}

		// Sauvegarde de l'artiste
		artisteDao.save(artiste);

		return Response.ok().entity("Artiste ajouté avec succès !").build();
	}
	
	@PUT
	@Path("/{artisteId}")
	@Consumes("application/json")
	@Operation(summary = "Modifier un artiste", description = "Met à jour l'artiste identifié par son ID avec les nouvelles données.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artiste mis à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Artiste non trouvé")
    })
	public Response updateArtiste(@PathParam("artisteId") Long artisteId, ArtisteDTO artisteDTO) {
	    // Vérifier si l'artiste existe
	    Artiste artiste = artisteDao.findOne(artisteId);
	    if (artiste == null) {
	        throw new WebApplicationException("Artiste not found", Response.Status.NOT_FOUND);
	    }

	    // Mettre à jour les informations de l'artiste
	    artiste.setNom(artisteDTO.getNom());
	    artiste.setPrenom(artisteDTO.getPrenom());
	    artiste.setNationalite(artisteDTO.getNationalite());
	    artiste.setDateNaissance(artisteDTO.getDateNaissance());
	    artiste.setEmail(artisteDTO.getEmail());
	    artiste.setTel(artisteDTO.getTel());

	    // Mise à jour des concerts
	    ConcertDao concertDao = new ConcertDao();
	    if (artisteDTO.getConcertsIds() != null && !artisteDTO.getConcertsIds().isEmpty()) {
	        List<Concert> concerts = artisteDTO.getConcertsIds().stream()
	            .map(concertDao::findOne)
	            .filter(concert -> concert != null)
	            .collect(Collectors.toList());
	        artiste.setConcerts(concerts);
	    } else {
	        artiste.setConcerts(new ArrayList<>());
	    }

	    // Sauvegarde des modifications
	    artisteDao.update(artiste);

	    return Response.ok().entity("Artiste mis à jour avec succès !").build();
	}

	@DELETE
	@Path("/{artisteId}")
	@Operation(summary = "Supprimer un artiste", description = "Supprime l'artiste identifié par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artiste supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Artiste non trouvé")
    })
	public Response deleteArtiste(@PathParam("artisteId") Long artisteId) {
	    // Vérifier si l'artiste existe
	    Artiste artiste = artisteDao.findOne(artisteId);
	    if (artiste == null) {
	        throw new WebApplicationException("Artiste not found", Response.Status.NOT_FOUND);
	    }

	    // Suppression de l'artiste
	    artisteDao.delete(artiste);

	    return Response.ok().entity("Artiste supprimé avec succès !").build();
	}


}