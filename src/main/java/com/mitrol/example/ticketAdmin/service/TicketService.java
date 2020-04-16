package com.mitrol.example.ticketAdmin.service;

import com.mitrol.example.ticketAdmin.entity.Ticket;
import com.mitrol.example.ticketAdmin.model.TicketDto;
import com.mitrol.example.ticketAdmin.model.TicketStatus;
import com.mitrol.example.ticketAdmin.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

	@Autowired
	private TicketRepository repository;

	private TicketDto toDto(Ticket entity) {
		TicketDto dto = new TicketDto();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setStatus(entity.getStatus());
		return dto;
	}

	private Ticket toEntity(TicketDto dto) {
		Ticket entity = new Ticket();
		entity.setName(dto.getName());
		entity.setStatus(dto.getStatus());
		return entity;
	}

	public List<TicketDto> findAll() {
		List<Ticket> entities = repository.findAll();
		return entities.stream().map(this::toDto).collect(Collectors.toList());
	}

	public TicketDto create(TicketDto dto) {
		Ticket entity = toEntity(dto);
		repository.save(entity);
		return toDto(entity);
	}

	/**
	 * Returns a TicketDto that matches the given id
	 * 
	 * @param id Id of the Ticket to search
	 * @return Matched TicketDto
	 */
	public TicketDto findById(int id) {
		Ticket entity = repository.findById(id).get();
		return toDto(entity);
	}

	/**
	 * Updates the Status attribute of the entity that matches the given id
	 * 
	 * @param id  Id of the Ticket to search
	 * @param dto TicketDto that contains the Status attribute that will be set on
	 *            the matched repository object
	 * @return The new TicketDto entity with the updated Status field
	 */
	public TicketDto updateStatusById(int id, TicketDto dto) {
		Ticket entity = repository.findById(id).get();
		entity.setStatus(dto.getStatus());
		entity = repository.save(entity);
		return toDto(entity);
	}

	/**
	 * Delete the element of the database that matches the given id
	 * 
	 * @param id Id of the element that will be deleted
	 * @return True if the element was deleted successfully, false if the element
	 *         stills in the database
	 */
	public boolean deleteById(int id) {
		repository.deleteById(id);

		return repository.findById(id).isPresent() ? false : true;
	}

	public List<TicketDto> findAllByStatus(TicketStatus... status) {
		List<Ticket> entities = repository.findAllByStatusIn(Arrays.asList(status));
		return entities.stream().map(this::toDto).collect(Collectors.toList());
	}
}
