package eu.ec.feedback.data.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.ec.feedback.model.ContactType;
import eu.ec.feedback.model.FeedbackEntry;

import java.util.Date;

public class FeedbackEntryResponseData {

    @JsonProperty
    private int id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String email;

    @JsonProperty
    private ContactType contactType;

    @JsonProperty
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date createdAt;

    public FeedbackEntryResponseData() {
        //
    }

    public FeedbackEntryResponseData(FeedbackEntry feedbackEntry) {
        this.id = feedbackEntry.getId();
        this.name = feedbackEntry.getName();
        this.email = feedbackEntry.getEmail();
        this.contactType = feedbackEntry.getContactType();
        this.message = feedbackEntry.getMessage();
        this.createdAt = feedbackEntry.getCreatedAt();
    }

    public FeedbackEntryResponseData(Object o) {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public String getMessage() {
        return message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
