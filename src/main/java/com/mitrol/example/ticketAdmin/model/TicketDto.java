package com.mitrol.example.ticketAdmin.model;

import javax.validation.constraints.NotNull;

public class TicketDto {

    private Integer id;

    @NotNull(message = "Ticket name cannot be empty")
    private String name;

    private TicketStatus status = TicketStatus.PENDING;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
