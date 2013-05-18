package fulbot.model.mail.incoming;

import java.io.IOException;
import java.io.OutputStream;

import javax.mail.MessagingException;
import javax.mail.Part;

/**
 * Reads the content inside a Part
 */
public interface ContentReader {
	
	public void read(Part part, OutputStream stream) throws IOException, MessagingException;

}
