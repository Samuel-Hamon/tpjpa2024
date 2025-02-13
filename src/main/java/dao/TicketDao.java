package dao;

import domain.Ticket;

public class TicketDao extends AbstractJpaDao<Long, Ticket> {
    public TicketDao() {
        setClazz(Ticket.class);
    }
}