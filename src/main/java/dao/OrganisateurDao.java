package dao;

import domain.Organisateur;

public class OrganisateurDao extends AbstractJpaDao<Long, Organisateur> {
    public OrganisateurDao() {
        setClazz(Organisateur.class);
    }
}