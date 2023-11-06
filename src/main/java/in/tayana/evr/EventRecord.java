package in.tayana.evr;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

	private Date date;
	public int evrEvent;
	public int evrCause;
	public int evrReason;

	private String request;
	private String response;

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public void clear() {
		evrEvent = 0;
		evrCause = 0;
		evrReason = 0;
		date = null;
		request = "";
		response = "";
	}

	private Date setCurrentDate() {
		return date = new Date();
	}

	public String toEvrString() {

		return dateFormat.format(setCurrentDate()) + EVR_RECORD_DELIM + evrEvent + EVR_RECORD_DELIM + evrCause + EVR_RECORD_DELIM
				+ evrReason + EVR_RECORD_DELIM + request + EVR_RECORD_DELIM + response;

	}

}
