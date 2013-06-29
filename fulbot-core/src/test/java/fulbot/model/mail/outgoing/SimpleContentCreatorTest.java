package fulbot.model.mail.outgoing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fulbot.model.Event;

public class SimpleContentCreatorTest {

	private SimpleContentCreator contentCreator;
	
	@Before
	public void setUp() throws Exception {
		contentCreator = new SimpleContentCreator();
	}

	@Test
	public void shouldCreateEmptyStringWhenAttendanceIsEmpty() {
		Event event = new Event();
		
		String content = contentCreator.createContent(event);
		
		assertEquals("", content);
	}

	@Test
	public void shouldCreateListContainingEachAttendee() {
		Event event = new Event();
		event.getAttendance().add("First");
		event.getAttendance().add("Second");
		event.getAttendance().add("Third");
		String expected = "1. First\n2. Second\n3. Third";
		
		String content = contentCreator.createContent(event);
		
		assertEquals(expected, content.trim());
	}

}
