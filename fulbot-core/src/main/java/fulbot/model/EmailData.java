package fulbot.model;

import java.util.ArrayList;
import java.util.List;

import com.github.jmkgreen.morphia.annotations.Indexed;

public class EmailData {

	/**
	 * Original email subject
	 */
	private String subject;
	
	/**
	 * List of message ids referenced in the email thread
	 */
	@Indexed
	private List<String> references = new ArrayList<String>();

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public List<String> getReferences() {
		return references;
	}

	public void setReferences(List<String> references) {
		this.references = references;
	}

}
