package org.tbwork.anole.hub.server.lccmanager.impl;

import io.netty.channel.socket.SocketChannel;

import org.springframework.stereotype.Service;
import org.tbwork.anole.common.enums.ClientType;
import org.tbwork.anole.hub.server.lccmanager.model.clients.LongConnectionClient;
import org.tbwork.anole.hub.server.lccmanager.model.clients.PublisherClient;
import org.tbwork.anole.hub.server.lccmanager.model.clients.WorkerClient;
import org.tbwork.anole.hub.server.lccmanager.model.requests.RegisterParameter;
import org.tbwork.anole.hub.server.lccmanager.model.requests.RegisterRequest;
import org.tbwork.anole.hub.server.util.ClientEntropyUtil; 

@Service("publisherClientManager")
public class PublisherClientManager  extends LongConnectionClientManager {

	@Override
	protected LongConnectionClient createClient(int token, RegisterRequest registerRequest) { 
		return new PublisherClient(token, registerRequest.getSocketChannel());
	} 
	
}