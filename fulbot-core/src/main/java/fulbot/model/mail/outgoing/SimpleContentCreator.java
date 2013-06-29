package fulbot.model.mail.outgoing;

import fulbot.model.Event;

public class SimpleContentCreator implements ContentCreator {

	@Override
	public String createContent(Event event) {
		StringBuffer content = new StringBuffer();

		int index = 1;
		for (String attendee : event.getAttendance()) {
			content.append(index + ". " + attendee + "\n");
			index += 1;
		}

		return content.toString();
	}

}
