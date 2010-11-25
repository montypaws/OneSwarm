package edu.washington.cs.oneswarm.test.util;

import java.util.concurrent.CountDownLatch;

import edu.washington.cs.oneswarm.test.integration.LocalOneSwarm;
import edu.washington.cs.oneswarm.test.integration.LocalOneSwarmListener;

public class TestUtils {

	/** Blocks until the LocalOneSwarm {@code instance} has started. */
	public static void awaitInstanceStart(LocalOneSwarm instance) {
		final CountDownLatch latch = new CountDownLatch(1);

		/*
		 * We need to add the listener before checking if we're running to avoid an
		 * initialization race.
		 */
		LocalOneSwarmListener listener = new LocalOneSwarmListener() {
			public void instanceStarted(LocalOneSwarm instance) {
				latch.countDown();
			}
		};
		instance.addListener(listener);

		try {
			if (instance.getState() == LocalOneSwarm.State.RUNNING) {
				latch.countDown();
			}
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			instance.removeListener(listener);
		}
	}
}