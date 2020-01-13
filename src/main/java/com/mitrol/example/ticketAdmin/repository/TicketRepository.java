package com.mitrol.example.ticketAdmin.repository;


import com.mitrol.example.ticketAdmin.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket,Integer> {
}
