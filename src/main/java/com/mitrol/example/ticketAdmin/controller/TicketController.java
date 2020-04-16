package com.mitrol.example.ticketAdmin.controller;

import com.mitrol.example.ticketAdmin.model.TicketDto;
import com.mitrol.example.ticketAdmin.model.TicketStatus;
import com.mitrol.example.ticketAdmin.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.constraints.NotNull;

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

	@GetMapping("/ticket/{id}")
	public TicketDto findById(@PathVariable("id") @NotNull int id) {
		return service.findById(id);
	}

	@PutMapping("/ticket/{id}")
	public TicketDto updateStatus(@RequestBody @NotNull TicketDto ticket, @PathVariable("id") @NotNull int id) {
		return service.updateStatusById(id, ticket);
	}

	@DeleteMapping("/ticket/{id}")
	public ResponseEntity<Integer> deleteById(@PathVariable("id") @NotNull int id) {
		if (service.deleteById(id)) {
			return new ResponseEntity<>(id, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(id, HttpStatus.NOT_MODIFIED);
		}
	}

	@GetMapping("/ticket/notFinished")
	public List<TicketDto> findAllNotFinished() {
		return service.findAllByStatus(TicketStatus.PENDING, TicketStatus.WORKING);
	}
}
