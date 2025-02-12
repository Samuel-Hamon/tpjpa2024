package domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Utilisateur extends Personne {

	private List<Ticket> tickets = new ArrayList<Ticket>();

	public Utilisateur() {
		super();
	}

	public Utilisateur(String nom, String prenom, String nationalite, LocalDateTime dateNaissance, String email,
			String tel) {
		super(nom, prenom, nationalite, dateNaissance, email, tel);
	}

	@OneToMany(mappedBy = "utilisateur", cascade = CascadeType.PERSIST)
	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	@Override
	public String toString() {
		return "Utilisateur [tickets=" + tickets + ", nom=" + nom + ", prenom=" + prenom + ", nationalite="
				+ nationalite + ", dateNaissance=" + dateNaissance + ", email=" + email + ", tel=" + tel + "]";
	}

}
