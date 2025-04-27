## Fonctionnalités opérationnelles  
- Création des entités et mapping JPA
- Lecture, ajout, modification et suppression des entités via le service REST
- Documentation de l'API : http://localhost:8080/api/

## Démarrer le projet  
- Créer une base de données "tp_sir" sur MySQL et exécuter JpaTest.java pour remplir la base.
- Exécuter RestServer.java pour lancer le service REST.
- Tester avec Postman.

## Informations
- Un artiste/organisateur/genre musical ne peut pas être supprimé s'il est lié à un concert
- Les tickets sont crées en fonction de la capacité d'un concert, on ne peut pas supprimer un ticket (il faut supprimer le concert lié pour les suprrimer).
