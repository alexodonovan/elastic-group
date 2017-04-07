package com.oddsocks.elastic.documents;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocumentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentController.class);

	@Autowired
	private DocumentRepository documentRepository;

	@RequestMapping("/")
	public String index() {
		LOGGER.info("Adding documents...");

		documentRepository.addDocument("first book", "12345");
		documentRepository.addDocument("second book", "12346");
		documentRepository.addDocument("third book", "12347");
		documentRepository.addDocument("forth book", "12348");
		documentRepository.addDocument("fifth book", "12349");

		return "Added documents";
	}

	@RequestMapping("/search/{query}")
	public List<BookVO> search(@PathVariable String query) {
		return documentRepository.findBook("title", query);
	}

}
