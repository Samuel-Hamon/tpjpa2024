package jpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Entity
public class Artiste extends Personne {
	private List<Concert> concerts = new ArrayList<Concert>();

	public Artiste() {
		super();
	}

	public Artiste(String nom, String prenom, String nationalite, LocalDateTime dateNaissance, String email,
			String tel) {
		super(nom, prenom, nationalite, dateNaissance, email, tel);
	}

	@ManyToMany
	public List<Concert> getConcerts() {
		return concerts;
	}

	public void setConcerts(List<Concert> concerts) {
		this.concerts = concerts;
	}

	@Override
	public String toString() {
		return "Artiste [concerts=" + concerts + ", nom=" + nom + ", prenom=" + prenom + ", nationalite=" + nationalite
				+ ", dateNaissance=" + dateNaissance + ", email=" + email + ", tel=" + tel + "]";
	}

}
