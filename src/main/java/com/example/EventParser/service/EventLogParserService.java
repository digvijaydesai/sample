package com.example.EventParser.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.EventParser.model.Event;
import com.example.EventParser.repository.EventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EventLogParserService {
	
	@Autowired
	EventRepository repo;
	
	Logger logger = LogManager.getLogger(EventLogParserService.class);

	public boolean processLogFile(String inputPath) throws IOException {
		FileInputStream inputStream = null;
		Scanner sc = null;
		try {

			ObjectMapper mapper = new ObjectMapper();

			try {
				logger.debug("Path {} :", inputPath);
				inputStream = new FileInputStream(inputPath);
			} catch ( Exception e1) {
				e1.printStackTrace();
				logger.error("Path is invalid {} :", inputPath);
				return false;
			}
			sc = new Scanner(inputStream, "UTF-8");

			HashMap<String, List<Event>> map = new HashMap<>();
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				Event e;
				try {
					e = mapper.readValue(line, Event.class);
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
					return false;
				}

				String eventId = e.getId();
				if (map.containsKey(eventId)) {

					Event ev = calculateEventDuration(eventId, e, map);
					saveEvent(ev);
					map.remove(eventId);
					

				} else {
					List<Event> listeEvent = new ArrayList<>();
					listeEvent.add(e);
					map.put(eventId, listeEvent);
				}
			}

			if (sc.ioException() != null) {
				throw sc.ioException();
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (sc != null) {
				sc.close();
			}
		}
		return true;
	}
	
	public void saveEvent(Event ev) {
		repo.save(ev);
	}

	public Event calculateEventDuration(String eventId, Event e, Map<String, List<Event>> map) {
		List<Event> listEvent = map.get(eventId);
		listEvent.add(e);
		long duration = Math.abs(listEvent.get(0).getTimestamp() - listEvent.get(1).getTimestamp());
		logger.info("Time taken {} :  duration {}" ,e.getId(), duration);
		e.setAlert(duration > 4);
		e.setDuration(duration);
		return e;
	}

}
