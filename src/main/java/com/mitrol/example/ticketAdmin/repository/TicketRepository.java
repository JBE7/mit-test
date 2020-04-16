package com.mitrol.example.ticketAdmin.repository;

import com.mitrol.example.ticketAdmin.entity.Ticket;
import com.mitrol.example.ticketAdmin.model.TicketStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

	List<Ticket> findAllByStatusIn(List<TicketStatus> list);

}
