package org.tbwork.anole.hub.server.client.manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PublisherUnregisterRequest implements BaseOperationRequest{

	private int token;
	
}