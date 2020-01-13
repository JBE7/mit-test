package com.mitrol.example.ticketAdmin.controller;

import com.mitrol.example.ticketAdmin.model.TicketDto;
import com.mitrol.example.ticketAdmin.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TicketController {

    @Autowired
    private TicketService service;

    @GetMapping("/ticket")
    public List<TicketDto> findAll() {
        return service.findAll();
    }

    @PostMapping("/ticket")
    public TicketDto create(@RequestBody TicketDto ticket) {
        return service.create(ticket);
    }

}
