package domain;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class ConcertDTO {
	private Long id;
	private int capacite;
	private String description;
	private Double prix;
	private String lieu;
	private LocalDateTime date;
	private String pays;
	private Long organisateurId;
	private Long genreMusicalId;
	private List<Long> artistesIds;
	private List<Long> ticketsIds;

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

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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

	public Long getOrganisateurId() {
		return organisateurId;
	}

	public void setOrganisateurId(Long organisateurId) {
		this.organisateurId = organisateurId;
	}

	public Long getGenreMusicalId() {
		return genreMusicalId;
	}

	public void setGenreMusicalId(Long genreMusicalId) {
		this.genreMusicalId = genreMusicalId;
	}

	public List<Long> getArtistesIds() {
		return artistesIds;
	}

	public void setArtistesIds(List<Long> artistesIds) {
		this.artistesIds = artistesIds;
	}

	public List<Long> getTicketsIds() {
		return ticketsIds;
	}

	public void setTicketsIds(List<Long> ticketsIds) {
		this.ticketsIds = ticketsIds;
	}
}
