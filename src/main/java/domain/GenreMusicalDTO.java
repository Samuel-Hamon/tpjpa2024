package domain;

import java.util.List;

public class GenreMusicalDTO {
	private Long id;
	private String nom;
	private List<Long> concertsIDs;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public List<Long> getConcertsIDs() {
		return concertsIDs;
	}
	public void setConcertsIDs(List<Long> concertsIDs) {
		this.concertsIDs = concertsIDs;
	}
}
