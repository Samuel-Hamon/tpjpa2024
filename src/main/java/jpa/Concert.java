package jpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Concert {
	private Long id;
	private int capacite;
	private String description;
	private Double prix;
	private String lieu;
	private LocalDateTime date;
	private String pays;
	private Organisateur organisateur;
	private GenreMusical genreMusical;
	private List<Artiste> artistes = new ArrayList<Artiste>();
	private List<Ticket> tickets = new ArrayList<Ticket>();;

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
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getCapacite() {
		return capacite;
	}

	public void setCapacite(int capacite) {
		this.capacite = capacite;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrix() {
		return prix;
	}

	public void setPrix(Double prix) {
		this.prix = prix;
	}

	public String getLieu() {
		return lieu;
	}

	public void setLieu(String lieu) {
		this.lieu = lieu;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getPays() {
		return pays;
	}

	public void setPays(String pays) {
		this.pays = pays;
	}

	@ManyToOne
	public Organisateur getOrganisateur() {
		return organisateur;
	}

	public void setOrganisateur(Organisateur organisateur) {
		this.organisateur = organisateur;
	}

	@ManyToOne
	public GenreMusical getGenreMusical() {
		return genreMusical;
	}

	public void setGenreMusical(GenreMusical genreMusical) {
		this.genreMusical = genreMusical;
	}

	@ManyToMany
	public List<Artiste> getArtistes() {
		return artistes;
	}

	public void setArtistes(List<Artiste> artistes) {
		this.artistes = artistes;
	}

	@OneToMany(mappedBy = "concert", cascade = CascadeType.PERSIST)
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
				+ ", genreMusical=" + genreMusical + ", artistes=" + artistes + ", tickets=" + tickets + "]";
	}

}
