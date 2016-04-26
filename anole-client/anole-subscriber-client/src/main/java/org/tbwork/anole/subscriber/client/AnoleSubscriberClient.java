package org.tbwork.anole.subscriber.client;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tbwork.anole.common.message.Message;
import org.tbwork.anole.subscriber.TimeClientHandler;
import org.tbwork.anole.subscriber.TimeDecoder;
import org.tbwork.anole.subscriber.client.handler.AuthenticationHandler;
import org.tbwork.anole.subscriber.client.handler.MainLogicHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption; 
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory; 

import com.google.common.base.Preconditions; 
/** 
 * @author Tommy.Tang
 */
@Component
@Data
public class AnoleSubscriberClient {

	@Getter(AccessLevel.NONE)@Setter(AccessLevel.NONE) 
	private volatile boolean started;
	@Getter(AccessLevel.NONE)@Setter(AccessLevel.NONE) 
	private static final Logger logger = LoggerFactory.getLogger(AnoleSubscriberClient.class);
	@Getter(AccessLevel.NONE)@Setter(AccessLevel.NONE) 
	SocketChannel socketChannel = null;
    int clientId = 0; // assigned by the server
    int token = 0;    // assigned by the server
    
	public void connect(String remoteAddress, int port) {
		if(!started) //DCL-1
		{
			synchronized(AnoleSubscriberClient.class)
			{
				if(!started)//DCL-2
				{
					executeConnect(remoteAddress, port); 
				}
			}
		} 
    }
	
	public void close(){
		if(!started) //DCL-1
		{
			synchronized(AnoleSubscriberClient.class)
			{
				if(!started)//DCL-2
				{
					executeClose(); 
				}
			}
		} 
		
	}
	
	
	public boolean sendMessage(Message msg)
	{
		if(socketChannel != null)
		{
		   tagMessage(msg);
		   socketChannel.writeAndFlush(msg); 
		   return true;
		}
		return false;
	}
	
	public boolean sendMessageWithListeners(Message msg, ChannelFutureListener ... listeners)
	{
		if(socketChannel != null)
		{
			tagMessage(msg);
			ChannelFuture f = socketChannel.writeAndFlush(msg);
			for(ChannelFutureListener item : listeners)
			    f.addListener(item);
			return true;
		}
		return false;
	} 
	
	/**
	 * Tag each message with current clientId and token before sending.
	 */
	private void tagMessage(Message msg){
		msg.setClientId(clientId);
	    msg.setToken(token);
	}
	
	private void executeConnect(String host, int port)
	{ 
		Preconditions.checkNotNull (host  , "host should be null.");
		Preconditions.checkArgument(port>0, "port should be > 0"  );
        EventLoopGroup workerGroup = new NioEventLoopGroup(); 
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                    		new ObjectEncoder(),
                   		    new ObjectDecoder(ClassResolvers.cacheDisabled(null)), 
                    		new AuthenticationHandler(false), 
                    		new MainLogicHandler(true)
                    		);
                }
            }); 
            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();  
            if (f.isSuccess()) {
            	socketChannel = (SocketChannel)f.channel(); 
            	started = true;
            	logger.info("[:)] Anole client successfully connected to the remote Anolo hub server with remote host = '{}' and port = {}", host, port);			            	
            } 
        }
        catch (InterruptedException e) {
        	logger.error("[:(] Anole client failed to connect to the remote Anolo hub server with remote host = '{}' and port = ", host, port);
			e.printStackTrace();
		} 
	}
	
	private void executeClose()
	{
		try {
			socketChannel.closeFuture().sync();
			socketChannel = null;
		} catch (InterruptedException e) {
			logger.error("[:(] Anole client (clientId = {}) failed to close. Inner message: {}", clientId, e.getMessage());
			e.printStackTrace();
		}finally{
			if(!socketChannel.isActive())
			{
				logger.info("[:)] Anole client (clientId = {}) closed successfully !", clientId);			            	
				started = false;
			}
		}
	}
	
}