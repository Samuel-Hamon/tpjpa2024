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
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@Path("concert")
@Produces({ "application/json" })
public class ConcertResource {

    // Injection (ou création) des DAO utilisés
    private ConcertDao concertDao = new ConcertDao();
    private OrganisateurDao organisateurDao = new OrganisateurDao();
    private GenreMusicalDao genreMusicalDao = new GenreMusicalDao();
    private ArtisteDao artisteDao = new ArtisteDao();
    private TicketDao ticketDao = new TicketDao();

    @GET
    @Path("/{concertId}")
    public ConcertDTO getConcertById(@PathParam("concertId") Long concertId) {
        Concert concert = concertDao.findOne(concertId);
        if (concert == null) {
            throw new WebApplicationException("Concert not found", Response.Status.NOT_FOUND);
        }
        return mapConcertToDTO(concert);
    }

    @GET
    @Path("/")
    public List<ConcertDTO> getConcerts() {
        List<Concert> concerts = concertDao.findAll();
        return concerts.stream().map(this::mapConcertToDTO).collect(Collectors.toList());
    }

    @POST
    @Consumes("application/json")
    public Response addConcert(@Parameter ConcertDTO concertDTO) {
        // Construction d'un nouveau concert à partir du DTO
        Concert concert = buildConcertFromDTO(concertDTO);
        // Sauvegarde du concert (et potentiellement de ses tickets en cascade)
        concertDao.save(concert);

        // Si la cascade n'est pas configurée pour les tickets, on les sauvegarde ici
        concert.getTickets().forEach(ticketDao::save);

        // Mise à jour des artistes : on leur ajoute le concert persistant
        concert.getArtistes().forEach(artiste -> {
            artiste.addConcert(concert);
            artisteDao.update(artiste);
        });

        return Response.ok().entity("SUCCESS").build();
    }

    @PUT
    @Path("/{concertId}")
    @Consumes("application/json")
    public Response updateConcert(@PathParam("concertId") Long concertId, ConcertDTO concertDTO) {
        Concert existingConcert = concertDao.findOne(concertId);
        if (existingConcert == null) {
            throw new WebApplicationException("Concert not found", Response.Status.NOT_FOUND);
        }
        // Mise à jour partielle des champs et associations
        updateConcertFromDTO(existingConcert, concertDTO);
        concertDao.save(existingConcert);
        return Response.ok().entity("UPDATED").build();
    }

    @DELETE
    @Path("/{concertId}")
    public Response deleteConcert(@PathParam("concertId") Long concertId) {
        Concert concert = concertDao.findOne(concertId);
        if (concert == null) {
            throw new WebApplicationException("Concert not found", Response.Status.NOT_FOUND);
        }
        // Suppression des associations ou configuration de cascade/orphanRemoval dans les entités
        concertDao.delete(concert);
        return Response.ok().entity("DELETED").build();
    }

    // Méthode de mapping de Concert vers ConcertDTO
    private ConcertDTO mapConcertToDTO(Concert concert) {
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
            dto.setArtistesIds(concert.getArtistes().stream()
                    .map(Artiste::getId)
                    .collect(Collectors.toList()));
        }
        if (concert.getTickets() != null) {
            dto.setTicketsIds(concert.getTickets().stream()
                    .map(Ticket::getId)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    // Construction d'un Concert à partir d'un ConcertDTO pour la création
    private Concert buildConcertFromDTO(ConcertDTO concertDTO) {
        Concert concert = new Concert();
        concert.setCapacite(concertDTO.getCapacite());
        concert.setDescription(concertDTO.getDescription());
        concert.setPrix(concertDTO.getPrix());
        concert.setLieu(concertDTO.getLieu());
        concert.setDate(concertDTO.getDate());
        concert.setPays(concertDTO.getPays());

        // Récupération et affectation de l'organisateur
        Organisateur organisateur = organisateurDao.findOne(concertDTO.getOrganisateurId());
        if (organisateur == null) {
            throw new WebApplicationException("Organisateur invalide", Response.Status.BAD_REQUEST);
        }
        concert.setOrganisateur(organisateur);

        // Récupération et affectation du genre musical
        GenreMusical genreMusical = genreMusicalDao.findOne(concertDTO.getGenreMusicalId());
        if (genreMusical == null) {
            throw new WebApplicationException("Genre musical invalide.", Response.Status.BAD_REQUEST);
        }
        concert.setGenreMusical(genreMusical);

        // Récupération et affectation des artistes
        List<Long> artistesIds = concertDTO.getArtistesIds();
        if (artistesIds == null || artistesIds.isEmpty()) {
            throw new WebApplicationException("Un concert doit avoir au moins un artiste.",
                    Response.Status.BAD_REQUEST);
        }
        List<Artiste> artistes = artistesIds.stream()
                .map(artisteDao::findOne)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        concert.setArtistes(artistes);

        // Création des tickets (non persistés ici)
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < concertDTO.getCapacite(); i++) {
            Ticket ticket = new Ticket();
            ticket.setConcert(concert); // Association au concert
            tickets.add(ticket);
        }
        concert.setTickets(tickets);

        return concert;
    }

    // Mise à jour d'un concert existant à partir d'un ConcertDTO (update partiel)
    private void updateConcertFromDTO(Concert concert, ConcertDTO dto) {
        concert.setDescription(dto.getDescription());
        concert.setPrix(dto.getPrix());
        concert.setLieu(dto.getLieu());
        concert.setDate(dto.getDate());
        concert.setPays(dto.getPays());

        // Mise à jour de l'organisateur
        Organisateur organisateur = organisateurDao.findOne(dto.getOrganisateurId());
        if (organisateur == null) {
            throw new WebApplicationException("Organisateur invalide", Response.Status.BAD_REQUEST);
        }
        concert.setOrganisateur(organisateur);

        // Mise à jour du genre musical
        GenreMusical genreMusical = genreMusicalDao.findOne(dto.getGenreMusicalId());
        if (genreMusical == null) {
            throw new WebApplicationException("Genre musical invalide.", Response.Status.BAD_REQUEST);
        }
        concert.setGenreMusical(genreMusical);

        // Mise à jour des artistes
        List<Long> artistesIds = dto.getArtistesIds();
        if (artistesIds == null || artistesIds.isEmpty()) {
            throw new WebApplicationException("Un concert doit avoir au moins un artiste.",
                    Response.Status.BAD_REQUEST);
        }
        List<Artiste> artistes = artistesIds.stream()
                .map(artisteDao::findOne)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        concert.setArtistes(artistes);
    }
}
