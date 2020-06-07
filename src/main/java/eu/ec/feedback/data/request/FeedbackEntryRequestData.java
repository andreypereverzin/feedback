package eu.ec.feedback.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.ec.feedback.model.ContactType;

public class FeedbackEntryRequestData {

    @JsonProperty
    private String name;

    @JsonProperty
    private String email;

    @JsonProperty(required = true)
    private ContactType contactType;

    @JsonProperty(required = true)
    private String message;

    public FeedbackEntryRequestData(String name, String email, ContactType contactType, String message) {
        this.name = name;
        this.email = email;
        this.contactType = contactType;
        this.message = message;
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

    @Override
    public String toString() {
        return "Feedback{" +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", contactType=" + contactType +
                ", message=" + message +
                '}';
    }
}
