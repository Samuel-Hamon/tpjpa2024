package domain;

public class TicketDTO {
	private Long id;
	private long utilisateurId;
	private long concertId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getUtilisateurId() {
		return utilisateurId;
	}
	public void setUtilisateurId(long utilisateurId) {
		this.utilisateurId = utilisateurId;
	}
	public long getConcertId() {
		return concertId;
	}
	public void setConcertId(long concertId) {
		this.concertId = concertId;
	}
	
}
