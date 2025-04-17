package rest;

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
    @Operation(summary = "Récupérer un concert", description = "Retourne les détails d'un concert via son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Concert trouvé"),
        @ApiResponse(responseCode = "404", description = "Concert non trouvé")
    })
    public ConcertDTO getConcertById(@PathParam("concertId") Long concertId) {
        Concert concert = concertDao.findOne(concertId);
        if (concert == null) {
            throw new WebApplicationException("Concert not found", Response.Status.NOT_FOUND);
        }
        return mapConcertToDTO(concert);
    }

    @GET
    @Path("/")
    @Operation(summary = "Lister les concerts", description = "Retourne la liste de tous les concerts.")
    @ApiResponse(responseCode = "200", description = "Liste des concerts retournée")
    public List<ConcertDTO> getConcerts() {
        List<Concert> concerts = concertDao.findAll();
        return concerts.stream().map(this::mapConcertToDTO).collect(Collectors.toList());
    }

    @POST
    @Consumes("application/json")
    @Operation(summary = "Ajouter un concert", description = "Crée un nouveau concert à partir du DTO fourni.")
    @ApiResponse(responseCode = "200", description = "Concert ajouté avec succès")
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
    @Operation(summary = "Modifier un concert", description = "Met à jour un concert existant identifié par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Concert mis à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Concert non trouvé")
    })
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
    @Operation(summary = "Supprimer un concert", description = "Supprime le concert identifié par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Concert supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Concert non trouvé")
    })
    public Response deleteConcert(@PathParam("concertId") Long concertId) {
        Concert concert = concertDao.findOne(concertId);
        if (concert == null) {
            throw new WebApplicationException("Concert not found", Response.Status.NOT_FOUND);
        }
        
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

        if (concertDTO.getOrganisateurId() == null) {
            throw new WebApplicationException(
                "organisateurId manquant ou invalide",
                Response.Status.BAD_REQUEST
            );
        }
        Organisateur o = organisateurDao.findOne(concertDTO.getOrganisateurId());
        if (o == null) {
            throw new WebApplicationException(
                "Organisateur introuvable pour l'ID " + concertDTO.getOrganisateurId(),
                Response.Status.BAD_REQUEST
            );
        }
        concert.setOrganisateur(o);
        
        if (concertDTO.getGenreMusicalId() == null) {
            throw new WebApplicationException(
                "genreMusicalId manquant ou invalide",
                Response.Status.BAD_REQUEST
            );
        }
        GenreMusical gm = genreMusicalDao.findOne(concertDTO.getGenreMusicalId());
        if (gm == null) {
            throw new WebApplicationException(
                "Genre musical introuvable pour l'ID " + concertDTO.getGenreMusicalId(),
                Response.Status.BAD_REQUEST
            );
        }
        concert.setGenreMusical(gm);


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
        for (int i = 0; i < concertDTO.getCapacite(); i++) {
            Ticket ticket = new Ticket(null, concert);
            concert.getTickets().add(ticket);
        }
        
        return concert;
    }

    // Mise à jour d'un concert existant à partir d'un ConcertDTO (update partiel)
    private void updateConcertFromDTO(Concert concert, ConcertDTO dto) {
        concert.setDescription(dto.getDescription());
        concert.setPrix(dto.getPrix());
        concert.setLieu(dto.getLieu());
        concert.setDate(dto.getDate());
        concert.setPays(dto.getPays());

        if (dto.getOrganisateurId() == null) {
            throw new WebApplicationException(
                "organisateurId manquant ou invalide",
                Response.Status.BAD_REQUEST
            );
        }
        Organisateur o = organisateurDao.findOne(dto.getOrganisateurId());
        if (o == null) {
            throw new WebApplicationException(
                "Organisateur introuvable pour l'ID " + dto.getOrganisateurId(),
                Response.Status.BAD_REQUEST
            );
        }
        concert.setOrganisateur(o);
        
        if (dto.getGenreMusicalId() == null) {
            throw new WebApplicationException(
                "genreMusicalId manquant ou invalide",
                Response.Status.BAD_REQUEST
            );
        }
        GenreMusical gm = genreMusicalDao.findOne(dto.getGenreMusicalId());
        if (gm == null) {
            throw new WebApplicationException(
                "Genre musical introuvable pour l'ID " + dto.getGenreMusicalId(),
                Response.Status.BAD_REQUEST
            );
        }
        concert.setGenreMusical(gm);

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
