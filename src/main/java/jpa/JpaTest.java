package jpa;

import java.time.LocalDateTime;
import java.util.List;

import domain.Artiste;
import domain.Concert;
import domain.GenreMusical;
import domain.Organisateur;
import domain.Personne;
import domain.Ticket;
import domain.Utilisateur;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class JpaTest {

	private EntityManager manager;

	public JpaTest(EntityManager manager) {
		this.manager = manager;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EntityManager manager = EntityManagerHelper.getEntityManager();

		JpaTest test = new JpaTest(manager);

		EntityTransaction tx = manager.getTransaction();
		tx.begin();
		try {
			// TODO create and persist entity
			test.create();
		} catch (Exception e) {
			e.printStackTrace();
		}
		tx.commit();
		
		test.listConcerts();

		manager.close();
		EntityManagerHelper.closeEntityManagerFactory();
		System.out.println(".. done");
	}

	private void create() {
		int numOfConcerts = manager.createQuery("Select a From Concert a", Concert.class).getResultList().size();
		if (numOfConcerts == 0) {
			// Création des personnes
			Personne utilisateur1 = new Utilisateur("userNom1", "userPrenom1", "Française", LocalDateTime.now(),
					"user1@gmail.com", "0123456789");
			Personne utilisateur2 = new Utilisateur("userNom2", "userPrenom2", "Française", LocalDateTime.now(),
					"user2@gmail.com", "0123456788");

			Personne organisateur1 = new Organisateur("orgNom1", "orgPrenom1", "Française", LocalDateTime.now(),
					"org1@gmail.com", "0123456799");
			Personne organisateur2 = new Organisateur("orgNom2", "orgPrenom2", "Française", LocalDateTime.now(),
					"org2@gmail.com", "0123456798");

			Personne artiste1 = new Artiste("artNom1", "artPrenom1", "Française", LocalDateTime.now(), "art1@gmail.com",
					"0123456899");
			Personne artiste2 = new Artiste("artNom2", "artPrenom2", "Française", LocalDateTime.now(), "art2@gmail.com",
					"0123456898");

			// Création de genres musicaux
			GenreMusical rock = new GenreMusical("Rock");
			GenreMusical jazz = new GenreMusical("Jazz");

			// Création de concerts
			Concert concert1 = new Concert(300, "Concert Rock Énergique", 20.00, "Rennes",
					LocalDateTime.now().plusDays(10), "France", (Organisateur) organisateur1, rock);
			Concert concert2 = new Concert(200, "Soirée Jazz Relaxante", 25.00, "Paris",
					LocalDateTime.now().plusDays(20), "France", (Organisateur) organisateur2, jazz);

			Ticket ticket1 = new Ticket((Utilisateur) utilisateur1, concert1);

			Ticket ticket2 = new Ticket((Utilisateur) utilisateur2, concert2);
			
			((Artiste) artiste1).addConcert(concert1);
			((Artiste) artiste2).addConcert(concert2);
			
			concert1.addArtiste((Artiste) artiste1);
			concert2.addArtiste((Artiste) artiste2);

			manager.persist(utilisateur1);
			manager.persist(utilisateur2);
			manager.persist(organisateur1);
			manager.persist(organisateur2);
			manager.persist(artiste1);
			manager.persist(artiste2);
			manager.persist(rock);
			manager.persist(jazz);
			manager.persist(concert1);
			manager.persist(concert2);
			manager.persist(ticket1);
			manager.persist(ticket2);
		}
	}

	private void listConcerts() {
		List<Concert> resultList = manager.createQuery("Select a From Concert a", Concert.class).getResultList();
		System.out.println("nb de concerts:" + resultList.size());
		for (Concert next : resultList) {
			System.out.println("concert suivant: " + next);
		}
	}

}