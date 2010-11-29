package edu.washington.cs.oneswarm.test.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/** Utility class which forks a thread to print the logs of a {@code Process}. */
public class ProcessLogConsumer extends Thread {

	private static Logger logger = Logger.getLogger(ProcessLogConsumer.class.getName());

	static {
		logger.setUseParentHandlers(false);
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(new Formatter(){
			@Override
			public String format(LogRecord record) {
				// Just want the message for this, we'll have prepended the
				// instance name when generating it.
				return record.getMessage() + "\n";
			}});
		logger.addHandler(consoleHandler);
	}

	/** The process from which to consume logs. */
	Process process;

	/** A label for the log of this process. */
	String label;

	public ProcessLogConsumer(String label, Process process) {
		this.label = label;
		this.process = process;
		setName("ProcessLogConsumer: " + label);
		setDaemon(true);
	}

	/** The consumer thread task. */
	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				logger.info("[" + label + "]: " + line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.info("[" + label + "]: Ended.");
	}
}
