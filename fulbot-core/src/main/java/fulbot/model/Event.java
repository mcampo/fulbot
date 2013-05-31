package fulbot.model;

import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;

/**
 * Represents an event that people will attend
 */
@Entity("events")
public class Event {

	@Id
	private ObjectId id;

	@Embedded
	private EmailData emailData = new EmailData();

	private Set<String> attendance = new HashSet<>();

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public EmailData getEmailData() {
		return emailData;
	}

	public void setEmailData(EmailData emailData) {
		this.emailData = emailData;
	}

	public Set<String> getAttendance() {
		return attendance;
	}

	public void setAttendance(Set<String> attendance) {
		this.attendance = attendance;
	}

}
