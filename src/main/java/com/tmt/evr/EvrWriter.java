package com.tmt.evr;

import org.apache.log4j.Logger;

public class EvrWriter {

	private static final Logger mLogger = Logger.getLogger(EvrWriter.class);

	private static Logger successLogger;
	private static Logger failureLogger;
	private static Logger reqRespLogger;

	public EvrWriter() {
		EvrWriter.setup();
	}

	public static boolean setup() {
		// Log in console in and log file
		try {
			successLogger = Logger.getLogger("EVR.Success");
			failureLogger = Logger.getLogger("EVR.failure");
			reqRespLogger = Logger.getLogger("EVR.request");
			// setKey(generateSymmetricKey());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static void WriteEvr(String evr) {
		
		setup();
		successLogger.info(evr);
	
	}

//	public static void WriteEvr(EventRecord evr) {
//		if (evr.isSuccess())
//			successLogger.info(evr.toCSV());
//		else
//			failureLogger.info(evr.toCSV());
//		reqRespLogger.info(evr.logRequestResponse());
//	}

}
