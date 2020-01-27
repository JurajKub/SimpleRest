package com.jkubinyi.simplerest.dto;

import java.util.UUID;

public class AcknowledgedDto {
	
	public static AcknowledgedDto ERROR = new AcknowledgedDto(null, true);
	
	private boolean error = false;
	private UUID actionId;
	/**
	 * @param author
	 * @param title
	 * @param year
	 */
	public AcknowledgedDto(UUID id) {
		this.actionId = id;
	}
	private AcknowledgedDto(UUID id, boolean error) {
		this(id);
		this.error = error;
	}
	public UUID getActionId() {
		return actionId;
	}
	public void error() {
		error = true;
	}
	public boolean getError() {
		return error;
	}

}