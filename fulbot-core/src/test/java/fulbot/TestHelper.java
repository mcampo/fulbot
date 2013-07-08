package fulbot;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestHelper {

	public static String readFile(String filename) throws IOException {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
		InputStreamReader streamReader = new InputStreamReader(inputStream, "UTF-8");
		StringBuffer stringBuffer = new StringBuffer();
	
		char[] cBuff = new char[1024];
		int read = 0;
		while ((read = streamReader.read(cBuff)) > 0) {
			stringBuffer.append(cBuff, 0, read);
		}
		streamReader.close();
	
		return stringBuffer.toString();
	}

}
