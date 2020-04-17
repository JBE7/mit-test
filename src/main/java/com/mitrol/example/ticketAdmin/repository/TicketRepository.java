package com.mitrol.example.ticketAdmin.repository;

import com.mitrol.example.ticketAdmin.entity.Ticket;
import com.mitrol.example.ticketAdmin.model.TicketStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

	/**
	 * Find all Tickets that matches some of the Status values passed by argument
	 * 
	 * @param list List of Status arguments to use in the search
	 * @return A list of Tickets that matches some of the Status argument
	 */
	List<Ticket> findAllByStatusIn(List<TicketStatus> list);
}
