package domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Ticket {
	private Long id;
	private Utilisateur utilisateur;
	private Concert concert;

	public Ticket() {
	}

	public Ticket(Utilisateur utilisateur, Concert concert) {
		this.utilisateur = utilisateur;
		this.concert = concert;
	}

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	@ManyToOne
	public Concert getConcert() {
		return concert;
	}

	public void setConcert(Concert concert) {
		this.concert = concert;
	}

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", utilisateur=" + utilisateur + ", concert=" + concert + "]";
	}

}
