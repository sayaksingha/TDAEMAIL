package com.tmt.evr;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.tmt.model.EmailRequest;
import lombok.Data;

@Data
public class EventRecord {

	private static final String EVR_RECORD_DELIM = "|";

	public static final int EVR_EVENT_SUCCESS = 1000;
	public static final int EVR_EVENT_FAILURE = 1001;

	// EvrCause
	public final static int EVR_CAS_SUCCESS = 0;
	public final static int EVR_CAS_FAILURE = 1;

	// EvrReason
	public final static int EVR_RSN_SUCCESS = 2000;
	public final static int EVR_RSN_FAILED = 2001;
	public final static int EVR_RSN_SYSTEM_ERROR = 2002;
	public final static int EVR_RSN_INVALID_DATA = 2003;

//	public Vector<Integer> reportId = new Vector<Integer>(5);
//	public Vector<String> reportRequest = new Vector<String>(5);
//	public Vector<String> reportResponse = new Vector<String>(5);

	private Date date;
	private int evrEvent;
	private int evrCause;
	private int evrReason;
	private EmailRequest emailRequest;
	private String response;

	public EventRecord() {
		date = new Date();
	}

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public void clear() {
		evrEvent = 0;
		evrCause = 0;
		evrReason = 0;
		date = null;
		
		response = "";
	}

	private Date setCurrentDate() {
		return date = new Date();
	}

	public String toEvrString() {
		System.out.println("EventRecord.toEvrString()");
		isNull(emailRequest);
		return dateFormat.format(date) + EVR_RECORD_DELIM + evrEvent + EVR_RECORD_DELIM + evrCause + EVR_RECORD_DELIM
				+ evrReason + EVR_RECORD_DELIM + emailRequest.getReport_id() + EVR_RECORD_DELIM + emailRequest.getFrom() + EVR_RECORD_DELIM + emailRequest.getTo()
				+ EVR_RECORD_DELIM + emailRequest.getBcc() + EVR_RECORD_DELIM + emailRequest.getCc()+ EVR_RECORD_DELIM 
				+ emailRequest.getSubject() + EVR_RECORD_DELIM + emailRequest.getBody_file() + EVR_RECORD_DELIM
				+ emailRequest.getAttachment() + EVR_RECORD_DELIM + response;

	}
	
	private static void isNull(EmailRequest emailRequest) {
		System.out.println("EventRecord.isNull()");
		if(emailRequest.getBcc()==null) {
			emailRequest.setBcc("no email for Bcc");
		}
		if(emailRequest.getCc()==null) {
			emailRequest.setCc("no email for Cc");
		}
		if(emailRequest.getTo()==null) {
			emailRequest.setTo("no email for To");
		}
	}

}
