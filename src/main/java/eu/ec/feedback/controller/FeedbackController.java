package eu.ec.feedback.controller;

import eu.ec.feedback.data.request.FeedbackEntryRequestData;
import eu.ec.feedback.data.response.FeedbackEntryResponseData;
import eu.ec.feedback.model.ContactType;
import eu.ec.feedback.service.FeedbackService;
import eu.ec.feedback.service.SortOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Api(value = "Feedback entities API")
public class FeedbackController {
    private static final Logger log = LoggerFactory.getLogger(FeedbackController.class);

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @ApiOperation(value = "Create a feedback entry")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created feedback entry"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping(path = "/feedbackentries", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> createFeedbackEntry(@RequestBody FeedbackEntryRequestData feedbackEntry) {
        log.debug("Started creating feedback entry");

        feedbackService.createFeedbackEntry(feedbackEntry);

        log.debug("Created feedback entry {}", feedbackEntry);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "Get list of feedback entries", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved feedback entries")
    })
    @GetMapping(path = "/feedbackentries", produces = "application/json")
    public ResponseEntity<List<FeedbackEntryResponseData>> getFeedbackEntries(
            @RequestParam(name = "contactType", required = false) ContactType contactType,
            @RequestParam(name = "sortOrder", required = false) SortOrder sortOrder
    ) {
        log.debug("Started getting feedback entries");

        List<FeedbackEntryResponseData> feedbackEntries = feedbackService.getFeedbackEntries(contactType, sortOrder);

        log.debug("Got feedback entries {}", feedbackEntries.size());

        return ResponseEntity.ok(feedbackEntries);
    }
}
