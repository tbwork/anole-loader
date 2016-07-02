package org.tbwork.anole.hub.client.worker.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.tbwork.anole.common.enums.ClientType;
import org.tbwork.anole.common.message.Message;
import org.tbwork.anole.common.message.MessageType;
import org.tbwork.anole.common.message.c_2_s.C2SMessage;
import org.tbwork.anole.common.message.c_2_s.worker_2_boss.RegisterResultMessage;
import org.tbwork.anole.common.message.s_2_c.PingAckMessage;
import org.tbwork.anole.common.message.s_2_c.boss._2_worker.RegisterClientMessage;
import org.tbwork.anole.common.message.s_2_c.worker._2_subscriber.ReturnConfigMessage;
import org.tbwork.anole.hub.client.AnoleClient;
import org.tbwork.anole.hub.server.AnoleServer;
import org.tbwork.anole.hub.server.util.ClientInfoGenerator;
import org.tbwork.anole.hub.server.util.ClientInfoGenerator.ClientInfo; 

public class OtherLogicHandler  extends SimpleChannelInboundHandler<Message>{

	public OtherLogicHandler(){
		super(true);
	}
	
	static Logger logger = LoggerFactory.getLogger(OtherLogicHandler.class);
	 
	@Autowired
	private AnoleClient worker; 
	
	@Autowired
	@Qualifier("publishServer")
	private AnoleServer publishServer;
	 
	@Autowired
	@Qualifier("subscriberServer")
	private AnoleServer subscriberServer;
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Message msg)
			throws Exception { 
		 MessageType msgType = msg.getType(); 
		 switch(msgType)
		 {  
		 	case S2C_PING_ACK:{ 
		 		PingAckMessage paMsg = (PingAckMessage) msg; 
		 	} break;
		 	case S2C_REGISTER_CLIENT:{
		 		RegisterClientMessage rcm = (RegisterClientMessage) msg;
		 		ClientType clientType = rcm.getClientType();
		 		worker.sendMessage(generateRegisterResultMessage(clientType));
		 	} break; 
		 	default:{ 
		 	} break; 
		 }  
	} 
	
	
	private RegisterResultMessage generateRegisterResultMessage(ClientType clientType){
		RegisterResultMessage result = new RegisterResultMessage();
		ClientInfo clientInfo = ClientInfoGenerator.generate(clientType);
		result.setResultClientId(clientInfo.getClientId());;
		result.setResultToken(clientInfo.getToken());
		result.setResultPort(clientType == ClientType.PUBLISHER?publishServer.getPort(): subscriberServer.getPort());
		result.setResultClientType(clientType); 
		return result; 
	}
	

}