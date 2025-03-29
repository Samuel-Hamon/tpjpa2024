package domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
public class Artiste extends Personne implements Serializable {
	private List<Concert> concerts = new ArrayList<Concert>();

	public Artiste() {
		super();
	}

	public Artiste(String nom, String prenom, String nationalite, LocalDateTime dateNaissance, String email,
			String tel) {
		super(nom, prenom, nationalite, dateNaissance, email, tel);
	}

	@ManyToMany
    @JoinTable(
        name = "artiste_concert",
        joinColumns = @JoinColumn(name = "artiste_id"),
        inverseJoinColumns = @JoinColumn(name = "concert_id")
    )
	public List<Concert> getConcerts() {
		return concerts;
	}

	public void setConcerts(List<Concert> concerts) {
		this.concerts = concerts;
	}
	
	public void addConcert(Concert concert) {
		this.concerts.add(concert);
	}

	@Override
	public String toString() {
		return "Artiste [concerts=" + concerts.size() + ", nom=" + nom + ", prenom=" + prenom + ", nationalite=" + nationalite
				+ ", dateNaissance=" + dateNaissance + ", email=" + email + ", tel=" + tel + "]";
	}

}
