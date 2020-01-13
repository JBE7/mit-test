package com.mitrol.example.ticketAdmin.entity;

import com.mitrol.example.ticketAdmin.model.TicketStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Ticket {

   @GeneratedValue
   @Id
   private Integer id;

   private String name;

   private TicketStatus status;

   public Integer getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public TicketStatus getStatus() {
      return status;
   }

   public void setStatus(TicketStatus status) {
      this.status = status;
   }
}
