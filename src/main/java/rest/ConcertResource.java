package rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import dao.ArtisteDao;
import dao.ConcertDao;
import dao.GenreMusicalDao;
import dao.OrganisateurDao;
import dao.TicketDao;
import domain.Artiste;
import domain.Concert;
import domain.ConcertDTO;
import domain.GenreMusical;
import domain.Organisateur;
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
	    concert.setCapacite(concertDTO.getCapacite()); // L'utilisateur définit la capacité
	    concert.setDescription(concertDTO.getDescription());
	    concert.setPrix(concertDTO.getPrix());
	    concert.setLieu(concertDTO.getLieu());
	    concert.setDate(concertDTO.getDate());
	    concert.setPays(concertDTO.getPays());

	    // Récupération de l'organisateur
	    Organisateur organisateur = new OrganisateurDao().findOne(concertDTO.getOrganisateurId());
	    if (organisateur == null) {
	        return Response.status(Response.Status.BAD_REQUEST).entity("Organisateur invalide.").build();
	    }
	    concert.setOrganisateur(organisateur);

	    // Récupération du genre musical
	    GenreMusical genreMusical = new GenreMusicalDao().findOne(concertDTO.getGenreMusicalId());
	    if (genreMusical == null) {
	        return Response.status(Response.Status.BAD_REQUEST).entity("Genre musical invalide.").build();
	    }
	    concert.setGenreMusical(genreMusical);

	    // Récupération des artistes et ajout du concert à chaque artiste
	    List<Artiste> artistes = concertDTO.getArtistesIds().stream()
	            .map(id -> new ArtisteDao().findOne(id))
	            .filter(Objects::nonNull)
	            .collect(Collectors.toList());

	    if (artistes.isEmpty()) {
	        return Response.status(Response.Status.BAD_REQUEST)
	                .entity("Un concert doit avoir au moins un artiste.")
	                .build();
	    }
	    
	    concert.setArtistes(artistes);

	    // Sauvegarde du concert AVANT d'ajouter les artistes
	    concertDao.save(concert);

	    // Ajout du concert à chaque artiste et mise à jour des artistes
	    ArtisteDao artisteDao = new ArtisteDao();
	    for (Artiste artiste : artistes) {
	        artiste.addConcert(concert); // Ajoute le concert à l'artiste
	        artisteDao.update(artiste);  // Mise à jour en base
	    }

	    // Création et sauvegarde des tickets
	    TicketDao ticketDao = new TicketDao();
	    List<Ticket> tickets = new ArrayList<>();
	    for (int i = 0; i < concertDTO.getCapacite(); i++) {
	        Ticket ticket = new Ticket();
	        ticket.setConcert(concert); // Associer le ticket au concert
	        ticketDao.save(ticket); // Sauvegarde du ticket en base
	        tickets.add(ticket);
	    }

	    // Mise à jour des tickets dans le concert (optionnel si OneToMany est bidirectionnel)
	    concert.setTickets(tickets);
	    concertDao.update(concert); // Mise à jour du concert avec les tickets

	    return Response.status(Response.Status.CREATED)
	            .entity("Concert ajouté avec succès avec " + concert.getCapacite() + " tickets.")
	            .build();
	}


}