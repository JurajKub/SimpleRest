package com.jkubinyi.simplerest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jkubinyi.simplepool.database.ConnectionPool;

@RestController
@RequestMapping(path="/pool")
public class PoolStatistics {

	@Autowired
	private ConnectionPool pool;
	
	@GetMapping("/activeConnections")
	public Integer getActiveConnections() {
		return this.pool.getNumActive();
	}
	
	@GetMapping("/createdConnections")
	public Long getCreatedConnections() {
		return this.pool.getNumCreated();
	}
	
	@GetMapping("/idleConnections")
	public Integer getIdleConnections() {
		return this.pool.getNumIdle();
	}

	@GetMapping("/destroyedConnections")
	public Long getDestroyedConnections() {
		return this.pool.getNumDestroyed();
	}
	
	@GetMapping("/clearPool")
	public ResponseEntity<?> clearPool() {
		this.pool.clear();
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/closePool")
	public ResponseEntity<?> closePool() {
		this.pool.close();
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/startPool")
	public ResponseEntity<?> startPool() {
		this.pool.create();
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
