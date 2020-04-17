package com.mitrol.example.ticketAdmin.controller;

import com.mitrol.example.ticketAdmin.exception.InvalidException;
import com.mitrol.example.ticketAdmin.exception.NoResultException;
import com.mitrol.example.ticketAdmin.model.TicketDto;
import com.mitrol.example.ticketAdmin.model.TicketStatus;
import com.mitrol.example.ticketAdmin.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import javax.validation.Valid;

@RestController
public class TicketController {

	@Autowired
	private TicketService service;
	
	private final TicketStatus STATUS_FOR_DELETE = TicketStatus.DONE;

	@GetMapping("/ticket")
	public List<TicketDto> findAll() {
		return service.findAll();
	}

	@PostMapping("/ticket")
	public TicketDto create(@Valid @RequestBody TicketDto ticket) {
		return service.create(ticket);
	}

	@GetMapping("/ticket/{id}")
	public TicketDto findById(@PathVariable("id") String id) {
		try {
			return service.findById(id);
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e.getCause());
		} catch (InvalidException e) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage(), e.getCause());
		}

	}

	@PutMapping("/ticket/{id}")
	public TicketDto updateStatus(@RequestBody TicketDto ticket, @PathVariable("id") String id) {
		try {
			return service.updateStatusById(id, ticket);
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e.getCause());
		} catch (InvalidException e) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage(), e.getCause());
		}
	}

	@DeleteMapping("/ticket/{id}")
	public String deleteById(@PathVariable("id") String id) {
		try {
			service.deleteByIdIfStatus(id, STATUS_FOR_DELETE);
			return id;
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e.getCause());
		} catch (InvalidException e) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage(), e.getCause());
		}
	}

	@GetMapping("/ticket/notFinished")
	public List<TicketDto> findAllNotFinished() {
		return service.findAllByStatus(TicketStatus.PENDING, TicketStatus.WORKING);
	}
}
