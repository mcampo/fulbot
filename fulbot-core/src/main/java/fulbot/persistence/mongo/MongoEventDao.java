package fulbot.persistence.mongo;

import java.util.List;

import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.dao.BasicDAO;

import fulbot.model.Event;
import fulbot.persistence.EventDao;

@Repository
public class MongoEventDao extends BasicDAO<Event, ObjectId> implements EventDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoEventDao.class);
	
	@Inject
	private MongoEventDao(Datastore ds) {
		super(ds);
	}

	@Override
	public List<Event> getAll() {
		return createQuery().order("-createdDate").asList();
	}

	@Override
	public Event findForMessageId(String messageId) {
		LOGGER.debug("MongoEventDao#findForMessageId {}", messageId);
		return findOne("emailData.references", messageId);
	}

	@Override
	public List<Event> findEventsWithReplyPending() {
		return createQuery().field("replyPending").equal(true).asList();
	}

}
