package dao;

import domain.Concert;

public class ConcertDao extends AbstractJpaDao<Long, Concert> {
    public ConcertDao() {
        setClazz(Concert.class);
    }
}