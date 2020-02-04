package com.jkubinyi.simplerest.controller;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jkubinyi.elasticappender.search.EASearch;
import com.jkubinyi.elasticappender.search.SimplifiedSearch;
import com.jkubinyi.elasticappender.search.common.Field;
import com.jkubinyi.elasticappender.record.LogRecord;


@RestController
@RequestMapping("/logs")
public class ElasticAppender {

	@Autowired
	private EASearch eaSearch;
	
	@GetMapping("/search")
	public Set<LogRecord> searchByQuery(@RequestParam(value="q") Object value,
			@RequestParam(value="f") Field field,
			@RequestParam(value="results", required=false, defaultValue="10") Integer size) throws IOException {
		SimplifiedSearch search = new SimplifiedSearch(this.eaSearch);
		search.fulltext(field, value);
		search.paginated(0, size);
		return search.execute();
	}

	@GetMapping("/exact")
	public Set<LogRecord> searchExactByValue(@RequestParam(value="q") Object value,
			@RequestParam(value="f") Field field,
			@RequestParam(value="results", required=false, defaultValue="10") Integer size) throws IOException {
		SimplifiedSearch search = new SimplifiedSearch(this.eaSearch);
		search.matches(field, value, true);
		search.paginated(0, size);
		return search.execute();
	}
}
