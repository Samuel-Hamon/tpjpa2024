package rest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dao.ArtisteDao;
import dao.ConcertDao;
import domain.Artiste;
import domain.ArtisteDTO;
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

@Path("artiste")
@Produces({ "application/json" })
public class ArtisteResource {

	private ArtisteDao artisteDao = new ArtisteDao(); // Création d'une instance du DAO

	@GET
	@Path("/{artisteId}")
	public ArtisteDTO getArtisteById(@PathParam("artisteId") Long artisteId) {
		// Utilisation du DAO pour récupérer le concert depuis la base de données
		Artiste artiste = artisteDao.findOne(artisteId);
		if (artiste == null) {
			// Si le concert n'existe pas, retourner une erreur
			throw new WebApplicationException("Artiste not found", Response.Status.NOT_FOUND);
		}
		ArtisteDTO dto = new ArtisteDTO();
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
	public List<ArtisteDTO> getArtistes() {
		// Récupérer tous les concerts depuis le DAO
		List<Artiste> artistes = artisteDao.findAll();

		// Convertir la liste des concerts en une liste de ConcertDTO
		List<ArtisteDTO> artisteDTOs = artistes.stream().map(artiste -> {
			ArtisteDTO dto = new ArtisteDTO();
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

}