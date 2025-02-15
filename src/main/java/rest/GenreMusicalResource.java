package rest;

import java.util.List;
import java.util.stream.Collectors;

import dao.GenreMusicalDao;
import domain.Concert;
import domain.GenreMusical;
import domain.GenreMusicalDTO;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
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


}