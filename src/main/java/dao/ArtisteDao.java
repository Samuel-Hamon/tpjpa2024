package dao;

import domain.Artiste;

public class ArtisteDao extends AbstractJpaDao<Long, Artiste> {
    public ArtisteDao() {
        setClazz(Artiste.class);
    }
}