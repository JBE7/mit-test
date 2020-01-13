package com.mitrol.example.ticketAdmin.service;

import com.mitrol.example.ticketAdmin.entity.Ticket;
import com.mitrol.example.ticketAdmin.model.TicketDto;
import com.mitrol.example.ticketAdmin.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Autowired
    private TicketRepository repository;

    private TicketDto toDto(Ticket entity){
        TicketDto dto = new TicketDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    private Ticket toEntity(TicketDto dto){
        Ticket entity = new Ticket();
        entity.setName(dto.getName());
        entity.setStatus(dto.getStatus());
        return entity;
    }


    public List<TicketDto> findAll(){
        List<Ticket> entities = repository.findAll();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public TicketDto create(TicketDto ticket){
        Ticket entity = toEntity(ticket);
        repository.save(entity);
        return toDto(entity);
    }




}
