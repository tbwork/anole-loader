package org.tbwork.anole.subscriber.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
 


import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.ReferenceCountUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbwork.anole.common.ConfigType;
import org.tbwork.anole.common.message.Message;
import org.tbwork.anole.common.message.MessageType; 
import org.tbwork.anole.common.message.s_2_c.ConfigChangeNotifyMessage;
import org.tbwork.anole.common.message.s_2_c.PingAckMessage;
import org.tbwork.anole.common.message.s_2_c.ReturnConfigMessage;
import org.tbwork.anole.subscriber.client.AnoleSubscriberClient;
import org.tbwork.anole.subscriber.client.GlobalConfig; 

public abstract class SpecifiedMessageHandler extends SimpleChannelInboundHandler<Message>{

	private MessageType messageType;  
	
	public SpecifiedMessageHandler(MessageType msgType){
		super(false);
		this.messageType = msgType; 
	} 
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Message msg)
			throws Exception {  
		if(messageType.equals(msg.getType())){
			process(msg);
			// Because this is a specified MessageHandler, so it should be useless after processed.
			ReferenceCountUtil.release(msg);
			return;
		}
		ctx.fireChannelRead(msg);
	}
 
	public abstract void process(Message message) ;
	
}
