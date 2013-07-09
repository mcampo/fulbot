package fulbot.persistence;

public interface IncomingEmailDao {

	/**
	 * Stores the given json that represents an incoming email
	 * @param incomingEmailJson
	 */
	public void save(String incomingEmailJson);
	
}
