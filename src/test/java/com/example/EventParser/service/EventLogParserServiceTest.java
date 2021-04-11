package com.example.EventParser.service;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.io.IOException;

import javax.swing.text.html.parser.Entity;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.EventParser.model.Event;
import com.example.EventParser.repository.EventRepository;

@SpringBootTest
public class EventLogParserServiceTest {

	@Spy
	EventLogParserService parserService;

	@Mock
	EventRepository repo;

	

	@Test
	void testProcessLogFileFOrInvalidPath() throws IOException {
		Assert.isTrue(!parserService.processLogFile(null), "Not valid Path");
		Assert.isTrue(!parserService.processLogFile("Z:/test"), "Not valid Path");
	}

	@Test
	void testProcessLogFileFOrInvalidFile() throws IOException {
		Assert.isTrue(
				!parserService
						.processLogFile("C:\\digvijay\\Java\\Practice\\Event-Parser\\src\\main\\resources\\file.txt"),
				"Not valid Path");
	}

	@Test
	void testProcessLogFileFOrValidFile() throws IOException {
	
		doNothing().when(parserService).saveEvent(any());
		Assert.isTrue(
				parserService
						.processLogFile("C:\\opt\\test.json"),
				"Not valid Path");
	}

}
