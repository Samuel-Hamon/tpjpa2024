package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@SuppressWarnings("serial")
@Entity
public class GenreMusical implements Serializable {
	private Long id;
	private String nom;
	private List<Concert> concerts = new ArrayList<Concert>();

	public GenreMusical() {
	}

	public GenreMusical(String nom) {
		this.nom = nom;
	}

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
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

	@OneToMany(mappedBy = "genreMusical", cascade = CascadeType.PERSIST)
	public List<Concert> getConcerts() {
		return concerts;
	}

	public void setConcerts(List<Concert> concerts) {
		this.concerts = concerts;
	}

	@Override
	public String toString() {
		return "GenreMusical [id=" + id + ", nom=" + nom + ", concerts=" + concerts + "]";
	}

}