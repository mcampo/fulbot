package fulbot.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;

/**
 * Represents an event that people will attend to
 */
@Entity("events")
public class Event {

	@Id
	private ObjectId id;

	@Embedded
	private EmailData emailData = new EmailData();

	private List<String> attendance = new ArrayList<>();

	private Boolean replyPending = Boolean.TRUE;

	private Date createdDate = new Date();

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

	public List<String> getAttendance() {
		return attendance;
	}

	public void setAttendance(List<String> attendance) {
		this.attendance = attendance;
	}

	public Boolean getReplyPending() {
		return replyPending;
	}

	public void setReplyPending(Boolean replyPending) {
		this.replyPending = replyPending;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
