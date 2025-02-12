package domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Personne {
	private Long id;
	protected String nom;
	protected String prenom;
	protected String nationalite;
	protected LocalDateTime dateNaissance;
	protected String email;
	protected String tel;

	public Personne() {
	}

	public Personne(String nom, String prenom, String nationalite, LocalDateTime dateNaissance, String email,
			String tel) {
		this.nom = nom;
		this.prenom = prenom;
		this.nationalite = nationalite;
		this.dateNaissance = dateNaissance;
		this.email = email;
		this.tel = tel;
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

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getNationalite() {
		return nationalite;
	}

	public void setNationalite(String nationalite) {
		this.nationalite = nationalite;
	}

	public LocalDateTime getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(LocalDateTime dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Override
	public String toString() {
		return "Personne [nom=" + nom + ", prenom=" + prenom + ", nationalite=" + nationalite + ", dateNaissance="
				+ dateNaissance + ", email=" + email + ", tel=" + tel + "]";
	}

}
