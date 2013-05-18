package fulbot.persistence.mongo;

import javax.inject.Inject;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.dao.BasicDAO;

import fulbot.model.Event;
import fulbot.persistence.EventDao;

public class MongoEventDao extends BasicDAO<Event, ObjectId> implements EventDao {

	@Inject
	private MongoEventDao(Datastore ds) {
		super(ds);
	}

	@Override
	public Event findForMessageId(String messageId) {
		return findOne("emailData.references", messageId);
	}


}
