package com.example.EventParser.parser;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.EventParser.service.EventLogParserService;

@RestController
@RequestMapping("/processLog")
public class EventLogParser {
	
	@Autowired
	EventLogParserService eventService;

	@PostMapping()
	public ResponseEntity<String> processLog(@RequestBody String path ) throws Exception {
	
		if( eventService.processLogFile(path)) {
			return ResponseEntity.ok("File is processed successfully");
		}
		
		return ResponseEntity.badRequest().body("File Not processed successfully Please check the logs for more details");
		

	}
}
