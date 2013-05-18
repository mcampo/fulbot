package fulbot.model.mail.incoming;

import java.io.IOException;
import java.io.OutputStream;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

/**
 * ContentReader implementation that only reads the text/plain content inside a Part.
 * If a Part contains multiple child parts, all of them will be scan for content. 
 */
public class TextPlainContentReader implements ContentReader {
	
	public static final String TEXT_PLAIN = "text/plain";
	
	@Override
	public void read(Part part, OutputStream stream) throws IOException, MessagingException {
		
		Object content = part.getContent();

		if (content instanceof String) {
			//part is text/plain, write it on the stream
			//note: string content other than text/plain is ignored (i.e. text/html)
			if (isTextPlain(part.getContentType())) {
				stream.write(part.getContent().toString().getBytes());
			}
		}
		
		if (content instanceof Multipart) {
			Multipart multipart = (Multipart)content;
			//read each part
			for (int i = 0; i < multipart.getCount(); i++) {
				read(multipart.getBodyPart(i), stream);
			}
		}
	}

	private boolean isTextPlain(String contentType) {
		return contentType.startsWith(TEXT_PLAIN);
	}

}
