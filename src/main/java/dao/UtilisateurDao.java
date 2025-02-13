package dao;

import domain.Utilisateur;

public class UtilisateurDao extends AbstractJpaDao<Long, Utilisateur> {
    public UtilisateurDao() {
        setClazz(Utilisateur.class);
    }
}