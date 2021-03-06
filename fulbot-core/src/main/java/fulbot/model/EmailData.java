package fulbot.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.jmkgreen.morphia.annotations.Indexed;

/**
 * Email related information about an event.
 */
public class EmailData {

	/**
	 * Original email subject
	 */
	private String subject;

	/**
	 * Email address where the messages received by fulbot are delivered to
	 */
	private String address;

	/**
	 * List of message ids referenced in the email thread
	 */
	@Indexed
	private List<String> references = new ArrayList<>();

	/**
	 * List of email addresses to send a reply to
	 */
	private Set<String> replyTo = new HashSet<>();

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<String> getReferences() {
		return references;
	}

	public void setReferences(List<String> references) {
		this.references = references;
	}

	public Set<String> getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(Set<String> replyTo) {
		this.replyTo = replyTo;
	}

}
