package org.tbwork.anole.hub.server.client.manager;

public class StaticConfiguration {

	/**
	 * <p>Why "MAX + 1" ?
	 * <p>Let's assume to set this value as 5. Think about one 
	 * situation that the server already add a ping promise count
	 * for certain client whose ping_promise_count is already four. 
	 * Before the server receives the promised PingMessage, the 
	 * promise-add thread runs and set 
	 * scavenger thread runs and will detect that the ping_promise_count 
	 * of the client is already MAX, and then unregister it. 
	 * To prevent this problem we just need to increase the set
	 * MAX_PROMISE_COUNT by one!
	 */
	public static final int MAX_PROMISE_COUNT = 5 + 1;
	public static final int PING_PERIOD_SECOND = 5*1000; //  ms 
}
