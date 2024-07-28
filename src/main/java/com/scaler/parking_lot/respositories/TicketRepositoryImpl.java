package com.scaler.parking_lot.respositories;

import com.scaler.parking_lot.models.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketRepositoryImpl implements TicketRepository{
    private List<Ticket> tickets = new ArrayList<>();
    private static long idCounter = 0;
    @Override
    public Ticket save(Ticket ticket) {
        if(ticket.getId()==0)
            ticket.setId(++idCounter);
        tickets.add(ticket);
        return ticket;
    }

    @Override
    public Optional<Ticket> getTicketById(long ticketId) {
        return tickets.stream().filter(ticket -> ticket.getId()==ticketId).findFirst();
    }
}
