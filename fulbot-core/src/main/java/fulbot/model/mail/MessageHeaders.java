package fulbot.model.mail;

/**
 * Constants for email header names.
 */
public interface MessageHeaders {

	public static final String MESSAGE_ID = "Message-ID";
	public static final String IN_REPLY_TO = "In-Reply-To";
	public static final String DELIVERED_TO = "Delivered-To";
	public static final String REPLY_TO = "Reply-To";
	public static final String REFERENCES = "References";
	public static final String REFERENCES_HEADER_SEPARATOR_REGEX = "\n\t|,";

}
