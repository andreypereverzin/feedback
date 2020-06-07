package eu.ec.feedback.service;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import eu.ec.feedback.dao.FeedbackEntryRepository;
import eu.ec.feedback.data.request.FeedbackEntryRequestData;
import eu.ec.feedback.data.response.FeedbackEntryResponseData;
import eu.ec.feedback.model.ContactType;
import eu.ec.feedback.model.FeedbackEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
public class FeedbackService {
    private static final Logger log = LoggerFactory.getLogger(FeedbackService.class);

    private final FeedbackEntryRepository feedbackEntryRepository;

    private final JsonValidator jsonValidator;

    @Autowired
    public FeedbackService(FeedbackEntryRepository feedbackEntryRepository, JsonValidator jsonValidator) {
        this.feedbackEntryRepository = feedbackEntryRepository;
        this.jsonValidator = jsonValidator;
    }

    @Transactional
    public void createFeedbackEntry(FeedbackEntryRequestData feedbackEntryData) {
        try {
            jsonValidator.validate(feedbackEntryData);
        } catch (IOException | ProcessingException ex) {
            throw new HttpServerErrorException(INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        FeedbackEntry feedbackEntry = new FeedbackEntry();
        feedbackEntry.setName(feedbackEntryData.getName());
        feedbackEntry.setEmail(feedbackEntryData.getEmail());
        feedbackEntry.setContactType(feedbackEntryData.getContactType());
        feedbackEntry.setMessage(feedbackEntryData.getMessage());

        feedbackEntryRepository.save(feedbackEntry);
    }

    @Transactional(readOnly = true)
    public List<FeedbackEntryResponseData> getFeedbackEntries(ContactType contactType, SortOrder sortOrder) {
        List<FeedbackEntry> feedbackEntryList = new ArrayList<>();

        Iterable<FeedbackEntry> feedbackEntries;
        if (contactType == null) {
            feedbackEntries = feedbackEntryRepository.findAll();
        } else {
            feedbackEntries = feedbackEntryRepository.findAllByContactType(contactType);
        }

        feedbackEntries.forEach(feedbackEntryList::add);

        if (sortOrder == null) {
            return feedbackEntryList.stream()
                    .map(FeedbackEntryResponseData::new)
                    .collect(toList());
        } else {
            Comparator<FeedbackEntry> comparator = Comparator.comparing(FeedbackEntry::getCreatedAt);
            if (sortOrder == SortOrder.DESC) {
                comparator = comparator.reversed();
            }
            return feedbackEntryList.stream()
                    .sorted(comparator)
                    .map(FeedbackEntryResponseData::new)
                    .collect(toList());
        }
    }
}
