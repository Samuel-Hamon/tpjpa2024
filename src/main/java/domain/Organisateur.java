package domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Organisateur extends Personne {
	private List<Concert> concerts = new ArrayList<Concert>();

	public Organisateur() {
		super();
	}

	public Organisateur(String nom, String prenom, String nationalite, LocalDateTime dateNaissance, String email,
			String tel) {
		super(nom, prenom, nationalite, dateNaissance, email, tel);
	}

	@OneToMany(mappedBy = "organisateur", cascade = CascadeType.PERSIST)
	public List<Concert> getConcerts() {
		return concerts;
	}

	public void setConcerts(List<Concert> concerts) {
		this.concerts = concerts;
	}

	@Override
	public String toString() {
		return "Organisateur [concerts=" + concerts + ", nom=" + nom + ", prenom=" + prenom + ", nationalite="
				+ nationalite + ", dateNaissance=" + dateNaissance + ", email=" + email + ", tel=" + tel + "]";
	}

}