package fulbot.persistence.mongo;

import java.util.List;

import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.dao.BasicDAO;

import fulbot.model.Event;
import fulbot.persistence.EventDao;

@Repository
public class MongoEventDao extends BasicDAO<Event, ObjectId> implements EventDao {

	@Inject
	private MongoEventDao(Datastore ds) {
		super(ds);
	}

	@Override
	public List<Event> getAll() {
		return find().asList();
	}

	@Override
	public Event findForMessageId(String messageId) {
		return findOne("emailData.references", messageId);
	}

}
