package com.bj58.spat.gaea.client;

import org.junit.Test;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bj58.spat.gaea.client.utility.AutoResetEvent;

/**
 * AutoResetEventTest
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class AutoResetEventTest {

	@Test
	public void testSet() throws InterruptedException {
		final AutoResetEvent event = new AutoResetEvent();
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					Logger.getLogger(AutoResetEventTest.class.getName()).log(Level.SEVERE, null, ex);
				}
				event.set();
				System.out.println("send set signal!");
			}
		});
		th.run();
		System.out.println("start wait!");
		event.waitOne(10000);
		System.out.println("end wait!");
	}

	@Test
	public void testWaitOne() throws Exception {
		final AutoResetEvent event = new AutoResetEvent();
		event.set();
		event.set();
		System.out.println("stime:" + System.currentTimeMillis());
		event.waitOne(10000);
		event.waitOne(10000);
		event.waitOne(10000);
		event.waitOne(10000);
		System.out.println("time:" + System.currentTimeMillis());
	}
}
