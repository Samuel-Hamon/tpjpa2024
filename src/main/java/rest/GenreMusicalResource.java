package rest;

import java.util.List;
import java.util.stream.Collectors;

import dao.GenreMusicalDao;
import domain.Concert;
import domain.GenreMusical;
import domain.GenreMusicalDTO;
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

@Path("genreMusical")
@Produces({ "application/json" })
public class GenreMusicalResource {

	private GenreMusicalDao genreMusicalDao = new GenreMusicalDao(); // Création d'une instance du DAO

	@GET
	@Path("/{GenreMusicalId}")
	@Operation(summary = "Récupérer un genre musical", description = "Retourne le genre musical associé à l'ID fourni.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Genre musical trouvé"),
        @ApiResponse(responseCode = "404", description = "Genre musical non trouvé")
    })
	public GenreMusicalDTO getGenreMusicalById(@PathParam("GenreMusicalId") Long GenreMusicalId) {
		// Utilisation du DAO pour récupérer le GenreMusical depuis la base de données
		GenreMusical genreMusical = genreMusicalDao.findOne(GenreMusicalId);
		if (genreMusical == null) {
			// Si le GenreMusical n'existe pas, retourner une erreur
			throw new WebApplicationException("GenreMusical not found", Response.Status.NOT_FOUND);
		}
		GenreMusicalDTO dto = new GenreMusicalDTO();
		dto.setId(genreMusical.getId());
		dto.setNom(genreMusical.getNom());

		if (genreMusical.getConcerts() != null) {
			dto.setConcertsIDs(genreMusical.getConcerts().stream().map(Concert::getId).collect(Collectors.toList()));
		}

		return dto;
	}

	@GET
	@Path("/")
	@Operation(summary = "Lister les genres musicaux", description = "Retourne la liste de tous les genres musicaux.")
    @ApiResponse(responseCode = "200", description = "Liste retournée")
	public List<GenreMusicalDTO> getGenreMusicals() {
		// Récupérer tous les GenreMusicals depuis le DAO
		List<GenreMusical> genreMusicals = genreMusicalDao.findAll();

		// Convertir la liste des GenreMusicals en une liste de GenreMusicalDTO
		List<GenreMusicalDTO> genreMusicalDTOs = genreMusicals.stream().map(genreMusical -> {
			GenreMusicalDTO dto = new GenreMusicalDTO();
			dto.setId(genreMusical.getId());
			dto.setNom(genreMusical.getNom());

			// Vérification des relations
			if (genreMusical.getConcerts() != null && !genreMusical.getConcerts().isEmpty()) {
				dto.setConcertsIDs(
						genreMusical.getConcerts().stream().map(Concert::getId).collect(Collectors.toList()));
			}

			return dto;
		}).collect(Collectors.toList());

		return genreMusicalDTOs;
	}

	@POST
	@Consumes("application/json")
	@Operation(summary = "Ajouter un genre musical", description = "Crée un nouveau genre musical.")
    @ApiResponse(responseCode = "201", description = "Genre musical ajouté avec succès")
	public Response addGenreMusical(@Parameter GenreMusicalDTO genreMusicalDTO) {
	    // Création d'un nouvel objet GenreMusical
	    GenreMusical genreMusical = new GenreMusical();
	    genreMusical.setNom(genreMusicalDTO.getNom());

	    // Sauvegarde du genre musical
	    genreMusicalDao.save(genreMusical);

	    return Response.status(Response.Status.CREATED)
	            .entity("Genre musical ajouté avec succès.")
	            .build();
	}
	
	@PUT
    @Path("/{genreMusicalId}")
    @Consumes("application/json")
	@Operation(summary = "Modifier un genre musical", description = "Met à jour le genre musical identifié par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Genre musical mis à jour"),
        @ApiResponse(responseCode = "404", description = "Genre musical non trouvé")
    })
    public Response updateGenreMusical(@PathParam("genreMusicalId") Long genreMusicalId, GenreMusicalDTO genreMusicalDTO) {
        GenreMusical genreMusical = genreMusicalDao.findOne(genreMusicalId);
        if (genreMusical == null) {
            throw new WebApplicationException("GenreMusical not found", Response.Status.NOT_FOUND);
        }
        genreMusical.setNom(genreMusicalDTO.getNom());
        genreMusicalDao.update(genreMusical);
        return Response.ok().entity("GenreMusical updated successfully").build();
    }

	@DELETE
    @Path("/{genreMusicalId}")
	@Operation(summary = "Supprimer un genre musical", description = "Supprime le genre musical identifié par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Genre musical supprimé"),
        @ApiResponse(responseCode = "404", description = "Genre musical non trouvé")
    })
    public Response deleteGenreMusical(@PathParam("genreMusicalId") Long genreMusicalId) {
        GenreMusical genreMusical = genreMusicalDao.findOne(genreMusicalId);
        if (genreMusical == null) {
            throw new WebApplicationException("GenreMusical not found", Response.Status.NOT_FOUND);
        }
        genreMusicalDao.delete(genreMusical);
        return Response.ok().entity("GenreMusical deleted successfully").build();
    }


}