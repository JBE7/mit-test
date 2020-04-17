package com.mitrol.example.ticketAdmin.service;

import com.mitrol.example.ticketAdmin.entity.Ticket;
import com.mitrol.example.ticketAdmin.exception.InvalidException;
import com.mitrol.example.ticketAdmin.exception.NoResultException;
import com.mitrol.example.ticketAdmin.model.TicketDto;
import com.mitrol.example.ticketAdmin.model.TicketStatus;
import com.mitrol.example.ticketAdmin.repository.TicketRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService {

	@Autowired
	private TicketRepository repository;

	/**
	 * Generates a TicketDto from a given Ticket
	 * 
	 * @param entity Entity that will be casted to TicketDto
	 * @return The generated TicketDto object
	 */
	private TicketDto toDto(Ticket entity) {
		TicketDto dto = new TicketDto();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setStatus(entity.getStatus());
		return dto;
	}

	/**
	 * Generates a Ticket entity from a given TicketDto
	 * 
	 * @param dto Data transfer object that will be casted to Ticket
	 * @return The generated Ticket entity
	 */
	private Ticket toEntity(TicketDto dto) {
		Ticket entity = new Ticket();
		entity.setName(dto.getName());
		entity.setStatus(dto.getStatus());
		return entity;
	}

	/**
	 * Obtains all instances of the type Ticket
	 * 
	 * @return A list of Tickets parsed to TicketDto objects
	 */
	public List<TicketDto> findAll() {
		List<Ticket> entities = repository.findAll();
		return entities.stream().map(this::toDto).collect(Collectors.toList());
	}

	/**
	 * Saves the given Ticket entity in the repository
	 * 
	 * @param dto item to save
	 * @return The TicketDto parsed response from the repository
	 */
	public TicketDto create(TicketDto dto) {
		Ticket entity = toEntity(dto);
		repository.save(entity);
		return toDto(entity);
	}

	/**
	 * Returns a TicketDto that matches the given id
	 * 
	 * @param ticketId Id of the Ticket to search
	 * @return Matched TicketDto
	 * @throws NoResultException If ticket was not found
	 * @throws InvalidException  If ticketId argument is not a number
	 */
	public TicketDto findById(String ticketId) throws InvalidException, NoResultException {
		Ticket entity = new Ticket();
		try {
			int id = Integer.parseInt(ticketId);

			if (id < 0)
				throw new InvalidException("Id must not be less than 0!");

			Optional<Ticket> result = repository.findById(id);
			entity = result.get();
		} catch (NumberFormatException e) {
			throw new InvalidException("Id must be a number!", e.getCause());
		} catch (NoSuchElementException e) {
			throw new NoResultException("No ticket found!", e.getCause());
		}

		return toDto(entity);
	}

	/**
	 * Updates the Status attribute of the entity that matches the given id
	 * 
	 * @param ticketId Id of the Ticket to search
	 * @param dto      TicketDto that contains the Status attribute that will be set
	 *                 on the matched repository object
	 * @return The new TicketDto entity with the updated Status field
	 * @throws NoResultException If ticket to update was not found
	 * @throws InvalidException  If ticketId argument is not a number
	 */
	public TicketDto updateStatusById(String ticketId, TicketDto dto) throws NoResultException, InvalidException {
		Ticket entity = new Ticket();
		try {
			int id = Integer.parseInt(ticketId);

			if (id < 0)
				throw new InvalidException("Id must not be less than 0!");

			Optional<Ticket> result = repository.findById(id);
			entity = result.get();
		} catch (NumberFormatException e) {
			throw new InvalidException("Id must be a number!", e.getCause());
		} catch (NoSuchElementException e) {
			throw new NoResultException("No ticket found! Update failed.", e.getCause());
		}

		entity.setStatus(dto.getStatus());
		entity = repository.save(entity);
		return toDto(entity);
	}

	/**
	 * Delete the element of the repository that matches the given id.
	 * The ticket will be deleted if the Ticket Status is the same as the Status param!
	 * 
	 * @param ticketId Id of the element that will be deleted
	 * @param status Status that will match the given TicketId
	 * @throws NoResultException If ticket to delete was not found
	 * @throws InvalidException  If ticketId argument is not a number
	 */
	public void deleteByIdIfStatus(String ticketId, TicketStatus status) throws NoResultException, InvalidException {
		if (findById(ticketId).getStatus().equals(status))
			repository.deleteById(Integer.parseInt(ticketId));
		else
			throw new InvalidException("Cannot Delete Ticket! Ticket delete is not allowed for the given status");
	}

	/**
	 * Finds all the elements that matches the TicketStatus given by argument
	 * 
	 * Use: call this function passing by argument all the TicketStatus that you
	 * need. Can be 1 or multiple arguments.
	 * 
	 * @param status Filter by this status
	 * @return A list of TicketDto who's status is in the Status parameter List
	 */
	public List<TicketDto> findAllByStatus(TicketStatus... status) {
		List<Ticket> entities = repository.findAllByStatusIn(Arrays.asList(status));
		return entities.stream().map(this::toDto).collect(Collectors.toList());
	}
}
