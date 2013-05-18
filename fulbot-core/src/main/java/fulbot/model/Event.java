package fulbot.model;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;

@Entity("events")
public class Event {

	@Id
	private ObjectId id;

	@Embedded
	private EmailData emailData = new EmailData();

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

}
