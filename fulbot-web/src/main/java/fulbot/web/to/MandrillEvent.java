package fulbot.web.to;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MandrillEvent {

	@JsonProperty("msg")
	private Message message;
	
	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Message {
		@JsonProperty("raw_msg")
		private String rawMessage;
		private String email;

		public String getRawMessage() {
			return rawMessage;
		}

		public void setRawMessage(String rawMessage) {
			this.rawMessage = rawMessage;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

	}

	public static class List extends ArrayList<MandrillEvent> {
		private static final long serialVersionUID = 1L;
	}

}
