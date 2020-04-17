package com.mitrol.example.ticketAdmin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitrol.example.ticketAdmin.model.TicketDto;
import com.mitrol.example.ticketAdmin.model.TicketStatus;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = TicketAdminApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
		locations = "classpath:application-integrationtest.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TicketAdminApplicationTests {

	@Autowired
	protected MockMvc mvc;

	@Autowired
	WebApplicationContext webApplicationContext;

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}
	
	/**
	 * Generates 15 items and execute a post to save each one in the repository
	 * *5 with status Pending
	 * *5 with status Working
	 * *5 with status Done
	 * 
	 * @return The generated items
	 * @throws JsonProcessingException If conversion from object to json String fails
	 * @throws Exception If MockMvc perform fails
	 */
	protected List<TicketDto> generateAndSaveData() throws JsonProcessingException, Exception {
		TicketDto ticket;
		ArrayList<TicketDto> tickets = new ArrayList<TicketDto>();
		int numberOfTickets = 15;
		
		for (int i = 0; i < numberOfTickets; i++) {
			ticket = new TicketDto();
			ticket.setName("Ticket " + i);
			if(i < 5)
				ticket.setStatus(TicketStatus.PENDING);
			else if(i < 10)
				ticket.setStatus(TicketStatus.WORKING);
			else
				ticket.setStatus(TicketStatus.DONE);
			
			mvc.perform(post("/ticket")
					.content(mapToJson(ticket))
					.contentType(MediaType.APPLICATION_JSON));
			
			tickets.add(ticket);
		}
		
		return tickets;
	}
	
	/**
	 * Bring all the data from the repository and return the last id used
	 * 
	 * @return last id from the repository
	 * @throws Exception If MockMvc perform fails
	 */
	protected int getLastIdUsedFromRepository() throws Exception {
		
		MvcResult result = mvc.perform(get("/ticket")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		ObjectMapper mapper = new ObjectMapper();
		
		List<Integer> tickets = mapper.readValue(result.getResponse().getContentAsString(), 
				new TypeReference<List<TicketDto>>() {}).stream()
                .map(TicketDto::getId)
                .collect(Collectors.toList());;
		
		return getHigherNumberFromList(tickets);		
	}
	
	/**
	 * Returns the bigger number from a list of Integers
	 * 
	 * @param ints List of Integers
	 * @return the higher number in the list
	 */
	protected int getHigherNumberFromList(List<Integer> ints) {
		int number = -1;
		
		for (Integer integer : ints) {
			if(integer.intValue() > number)
				number = integer.intValue();
		}
		
		return number;
	}

	// -------------------- Happy tests -------------------------------------------
	@Test
	@Order(1)
	void fetchTicketTest() throws Exception {
		mvc.perform(get("/ticket")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", is(Matchers.empty())));
	}


	@Test
	@Order(2)
	void createTicketTest() throws Exception {
		String ticketNameTest = "Test 1";
		TicketDto ticket = new TicketDto();
		ticket.setName(ticketNameTest);

		mvc.perform(post("/ticket")
				.content(mapToJson(ticket))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", is(ticketNameTest)));

		mvc.perform(get("/ticket")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].name", is(ticketNameTest)));

	}

	
	@Test
	@Order(3)
	void findTicketByIdTest() throws Exception {
		int id = getLastIdUsedFromRepository() + 1;
		
		String ticketNameTest = "Test 1";
		TicketDto ticket = new TicketDto();
		ticket.setName(ticketNameTest);

		mvc.perform(post("/ticket")
				.content(mapToJson(ticket))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", is(ticketNameTest)));
		
		mvc.perform(get("/ticket/{id}", id)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(id)));
	}
	
	@Test
	@Order(4)
	void updateStatusTest() throws Exception {
		int id = 1;
		TicketDto ticket = new TicketDto();
		ticket.setStatus(TicketStatus.DONE);
		
		mvc.perform(put("/ticket/{id}", id)
				.content(mapToJson(ticket))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(ticket.getStatus().toString())));
	}
	
	@Test
	@Order(5)
	void deleteByIdTest() throws Exception {
		int id = 1;
		
		MvcResult result = mvc.perform(delete("/ticket/{id}", id)
									.contentType(MediaType.APPLICATION_JSON))
									.andExpect(status().isOk())
									.andReturn();
		
		String returnId = result.getResponse().getContentAsString();
	
		assertTrue(String.valueOf(id).equals(returnId));
	}
	
	@Test
	@Order(6)
	void findAllTicketsNotFinishedTest() throws Exception {
		generateAndSaveData();
		
		MvcResult result = mvc.perform(get("/ticket/notFinished"))
			.andExpect(status().isOk())
			.andReturn();
		
		ObjectMapper mapper = new ObjectMapper();
		
		List<TicketDto> tickets = mapper.readValue(result.getResponse().getContentAsString(), 
				new TypeReference<List<TicketDto>>() {});
		
		assertFalse(tickets.stream()
					.filter(o -> o.getStatus().equals(TicketStatus.DONE))
					.findFirst()
					.isPresent());
	}
	
	// -------------------- Unhappy tests -----------------------------------------

	@Test
	@Order(7)
	void createNullTicketTest() throws Exception {
		
		mvc.perform(post("/ticket")
				.content("{}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	
	@Test
	@Order(8)
	void findTicketByStringIdTest() throws Exception {	
		mvc.perform(get("/ticket/{id}", "A")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotAcceptable());
	}
	
	@Test
	@Order(9)
	void findTicketByNegativeIdTest() throws Exception {	
		mvc.perform(get("/ticket/{id}", "-1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotAcceptable());
	}
	
	@Test
	@Order(10)
	void findTicketByNonExistentIdTest() throws Exception {	
		mvc.perform(get("/ticket/{id}", "12345")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
	
	@Test
	@Order(11)
	void updateStatusWithEmptyStatusTest() throws Exception {
		int id = getLastIdUsedFromRepository() + 1;
		
		String ticketNameTest = "Test 1";
		TicketDto ticket = new TicketDto();
		ticket.setName(ticketNameTest);
		ticket.setStatus(TicketStatus.WORKING);

		mvc.perform(post("/ticket")
				.content(mapToJson(ticket))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", is(ticketNameTest)));
		
		mvc.perform(put("/ticket/{id}", id)
				.content("{}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(TicketStatus.PENDING.toString())));
	}
	
	@Test
	@Order(12)
	void updateStatusWithInvalidStatusTest() throws Exception {
		int id = getLastIdUsedFromRepository() + 1;

		String ticketNameTest = "Test 1";
		TicketDto ticket = new TicketDto();
		ticket.setName(ticketNameTest);
		ticket.setStatus(TicketStatus.WORKING);

		mvc.perform(post("/ticket")
				.content(mapToJson(ticket))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", is(ticketNameTest)));
		
		JSONObject ticketToJson = new JSONObject();
		ticketToJson.put("status", "asd");
		
		mvc.perform(put("/ticket/{id}", id)
				.content(ticketToJson.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	@Order(13)
	void updateStatusWithNegativeIdTest() throws Exception {
		int id = -10;
		
		mvc.perform(put("/ticket/{id}", id)
				.content("{}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotAcceptable());
	}
	
	@Test
	@Order(14)
	void deleteByNegativeIdTest() throws Exception {
		int id = -1;
		
		mvc.perform(delete("/ticket/{id}", id)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotAcceptable());
	}
	
	@Test
	@Order(15)
	void deleteByStringIdTest() throws Exception {
		
		mvc.perform(delete("/ticket/{id}", "A")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotAcceptable());
	}
	
	@Test
	@Order(16)
	void deleteWithInvalidStatus() throws Exception {
		
		String ticketNameTest = "Test 1";
		TicketDto ticket = new TicketDto();
		ticket.setName(ticketNameTest);
		ticket.setStatus(TicketStatus.WORKING);

		mvc.perform(post("/ticket")
				.content(mapToJson(ticket))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", is(ticketNameTest)));
		
		int id = getLastIdUsedFromRepository();
		
		mvc.perform(delete("/ticket/{id}", id)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotAcceptable());
	}
	
	@Test
	@Order(17)
	void findAllTicketsNotFinishedEmptyRepositoryTest() throws Exception {	
		int id = getLastIdUsedFromRepository();
		
		while(id != -1) {
			JSONObject ticketToJson = new JSONObject();
			ticketToJson.put("status", "DONE");
			
			mvc.perform(put("/ticket/{id}", id)
					.content(ticketToJson.toString())
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk());
			
			mvc.perform(delete("/ticket/{id}", id)
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk());
			
			id = getLastIdUsedFromRepository();
		}
		
		MvcResult result = mvc.perform(get("/ticket/notFinished"))
			.andExpect(status().isOk())
			.andReturn();
		
		ObjectMapper mapper = new ObjectMapper();
		
		List<TicketDto> tickets = mapper.readValue(result.getResponse().getContentAsString(), 
				new TypeReference<List<TicketDto>>() {});
		
		assertTrue(tickets.size() == 0);
	}	
}
