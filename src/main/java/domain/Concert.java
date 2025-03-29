package domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
public class Concert implements Serializable {

    private Long id;
    private int capacite;
    private String description;
    private Double prix;
    private String lieu;
    private LocalDateTime date;
    private String pays;
    private Organisateur organisateur;
    private GenreMusical genreMusical;
    private List<Artiste> artistes = new ArrayList<>();
    private List<Ticket> tickets = new ArrayList<>();

    public Concert() {
    }

    public Concert(int capacite, String description, Double prix, String lieu, LocalDateTime date, String pays,
                   Organisateur organisateur, GenreMusical genreMusical) {
        this.capacite = capacite;
        this.description = description;
        this.prix = prix;
        this.lieu = lieu;
        this.date = date;
        this.pays = pays;
        this.organisateur = organisateur;
        this.genreMusical = genreMusical;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlElement
    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement
    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    @XmlElement
    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    @XmlElement
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @XmlElement
    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    @ManyToOne
    @XmlElement
    public Organisateur getOrganisateur() {
        return organisateur;
    }

    public void setOrganisateur(Organisateur organisateur) {
        this.organisateur = organisateur;
    }

    @ManyToOne
    @XmlElement
    public GenreMusical getGenreMusical() {
        return genreMusical;
    }

    public void setGenreMusical(GenreMusical genreMusical) {
        this.genreMusical = genreMusical;
    }

    @ManyToMany(mappedBy = "concerts")
    @XmlElement
    public List<Artiste> getArtistes() {
        return artistes;
    }

    public void setArtistes(List<Artiste> artistes) {
        this.artistes = artistes;
    }

    public void addArtiste(Artiste artiste) {
        this.artistes.add(artiste);
    }

    @OneToMany(mappedBy = "concert", cascade = CascadeType.PERSIST)
    @XmlElement
    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "Concert [id=" + id + ", capacite=" + capacite + ", description=" + description + ", prix=" + prix
                + ", lieu=" + lieu + ", date=" + date + ", pays=" + pays + ", organisateur=" + organisateur
                + ", genreMusical=" + genreMusical + ", artistes=" + artistes.size() + ", tickets=" + tickets + "]";
    }
}
