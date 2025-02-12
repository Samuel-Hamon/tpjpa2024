package rest;

import java.util.List;
import java.util.stream.Collectors;

import dao.ConcertDao;
import domain.Artiste;
import domain.Concert;
import domain.ConcertDTO;
import domain.Ticket;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@Path("concert")
@Produces({ "application/json" })
public class ConcertResource {

	private ConcertDao concertDao = new ConcertDao(); // Création d'une instance du DAO

	@GET
	@Path("/{concertId}")
	public ConcertDTO getConcertById(@PathParam("concertId") Long concertId) {
		// Utilisation du DAO pour récupérer le concert depuis la base de données
		Concert concert = concertDao.findOne(concertId);
		if (concert == null) {
			// Si le concert n'existe pas, retourner une erreur
			throw new WebApplicationException("Concert not found", Response.Status.NOT_FOUND);
		}
		ConcertDTO dto = new ConcertDTO();
		dto.setId(concert.getId());
		dto.setCapacite(concert.getCapacite());
		dto.setDescription(concert.getDescription());
		dto.setPrix(concert.getPrix());
		dto.setLieu(concert.getLieu());
		dto.setDate(concert.getDate());
		dto.setPays(concert.getPays());

		if (concert.getOrganisateur() != null) {
			dto.setOrganisateurId(concert.getOrganisateur().getId());
		}
		if (concert.getGenreMusical() != null) {
			dto.setGenreMusicalId(concert.getGenreMusical().getId());
		}
		if (concert.getArtistes() != null) {
			dto.setArtistesIds(concert.getArtistes().stream().map(Artiste::getId).collect(Collectors.toList()));
		}
		if (concert.getTickets() != null) {
			dto.setTicketsIds(concert.getTickets().stream().map(Ticket::getId).collect(Collectors.toList()));
		}

		return dto;
	}

	@GET
	@Path("/")
	public List<ConcertDTO> getConcerts() {
		// Récupérer tous les concerts depuis le DAO
		List<Concert> concerts = concertDao.findAll();

		// Convertir la liste des concerts en une liste de ConcertDTO
		List<ConcertDTO> concertDTOs = concerts.stream().map(concert -> {
			ConcertDTO dto = new ConcertDTO();
			dto.setId(concert.getId());
			dto.setCapacite(concert.getCapacite());
			dto.setDescription(concert.getDescription());
			dto.setPrix(concert.getPrix());
			dto.setLieu(concert.getLieu());
			dto.setDate(concert.getDate());
			dto.setPays(concert.getPays());

			// Vérification des relations
			if (concert.getOrganisateur() != null) {
				dto.setOrganisateurId(concert.getOrganisateur().getId());
			}
			if (concert.getGenreMusical() != null) {
				dto.setGenreMusicalId(concert.getGenreMusical().getId());
			}
			if (concert.getArtistes() != null && !concert.getArtistes().isEmpty()) {
				dto.setArtistesIds(concert.getArtistes().stream().map(Artiste::getId).collect(Collectors.toList()));
			}
			if (concert.getTickets() != null && !concert.getTickets().isEmpty()) {
				dto.setTicketsIds(concert.getTickets().stream().map(Ticket::getId).collect(Collectors.toList()));
			}

			return dto;
		}).collect(Collectors.toList());

		return concertDTOs;
	}

	@POST
	@Consumes("application/json")
	public Response addConcert(@Parameter ConcertDTO concertDTO) {
		Concert concert = new Concert();
		concert.setId(concertDTO.getId());
		concert.setCapacite(concertDTO.getCapacite());
		concert.setDescription(concertDTO.getDescription());
		concert.setPrix(concertDTO.getPrix());
		concert.setLieu(concertDTO.getLieu());
		concert.setDate(concertDTO.getDate());
		concert.setPays(concertDTO.getPays());

		// Save the concert
		concertDao.save(concert);

		return Response.ok().entity("SUCCESS").build();
	}

}