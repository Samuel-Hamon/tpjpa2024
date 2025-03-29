package domain;

public class TicketDTO {
	private Long id;
	private Long utilisateurId;
	private Long concertId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUtilisateurId() {
		return utilisateurId;
	}
	public void setUtilisateurId(long utilisateurId) {
		this.utilisateurId = utilisateurId;
	}
	public Long getConcertId() {
		return concertId;
	}
	public void setConcertId(long concertId) {
		this.concertId = concertId;
	}
	
}
