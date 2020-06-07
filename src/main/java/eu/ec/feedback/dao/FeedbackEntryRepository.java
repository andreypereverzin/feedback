package eu.ec.feedback.dao;

import eu.ec.feedback.model.ContactType;
import eu.ec.feedback.model.FeedbackEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeedbackEntryRepository extends CrudRepository<FeedbackEntry, Long> {
    List<FeedbackEntry> findAllByContactType(ContactType contactType);
}
