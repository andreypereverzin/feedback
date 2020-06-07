package eu.ec.feedback.service;

import eu.ec.feedback.data.request.FeedbackEntryRequestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;

import static eu.ec.feedback.model.ContactType.GENERAL;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonValidatorTest {
    private JsonValidator jsonValidator;

    @BeforeEach
    void setUp() throws Exception {
        jsonValidator = new JsonValidator();
    }

    @Test
    void validate_should_pass_valid_entry() {
        // given
        FeedbackEntryRequestData entry = new FeedbackEntryRequestData("name", "email", GENERAL, "message");

        // when
        // then
        assertDoesNotThrow(() -> jsonValidator.validate(entry));
    }

    @Test
    void validate_should_pass_missing_name() {
        // given
        FeedbackEntryRequestData entry = new FeedbackEntryRequestData(null, "email", GENERAL, "message");

        // when
        // then
        assertDoesNotThrow(() -> jsonValidator.validate(entry));
    }

    @Test
    void validate_should_pass_missing_email() {
        // given
        FeedbackEntryRequestData entry = new FeedbackEntryRequestData("name", null, GENERAL, "message");

        // when
        // then
        assertDoesNotThrow(() -> jsonValidator.validate(entry));
    }

    @Test
    void validate_should_fail_long_name() {
        // given
        FeedbackEntryRequestData entry = new FeedbackEntryRequestData(getString(101), "email", GENERAL, "message");

        // when
        // then
        assertThrows(
                HttpClientErrorException.class,
                () -> jsonValidator.validate(entry),
                "Error validating feedback entry"
        );
    }

    @Test
    void validate_should_fail_long_email() {
        // given
        FeedbackEntryRequestData entry = new FeedbackEntryRequestData("name", getString(101), GENERAL, "message");

        // when
        // then
        assertThrows(
                HttpClientErrorException.class,
                () -> jsonValidator.validate(entry),
                "Error validating feedback entry"
        );
    }

    @Test
    void validate_should_fail_long_message() {
        // given
        FeedbackEntryRequestData entry = new FeedbackEntryRequestData("name", "email", GENERAL, getString(1001));

        // when
        // then
        assertThrows(
                HttpClientErrorException.class,
                () -> jsonValidator.validate(entry),
                "Error validating feedback entry"
        );
    }

    @Test
    void validate_should_fail_missing_contact_type() {
        // given
        FeedbackEntryRequestData entry = new FeedbackEntryRequestData("name", "email", null, "message");

        // when
        // then
        assertThrows(
                HttpClientErrorException.class,
                () -> jsonValidator.validate(entry),
                "Error validating feedback entry"
        );
    }

    @Test
    void validate_should_fail_missing_message() {
        // given
        FeedbackEntryRequestData entry = new FeedbackEntryRequestData("name", "email", GENERAL, null);

        // when
        // then
        assertThrows(
                HttpClientErrorException.class,
                () -> jsonValidator.validate(entry),
                "Error validating feedback entry"
        );
    }

    private String getString(int length) {
        char[] charArray = new char[length];
        Arrays.fill(charArray, 'n');
        return new String(charArray);
    }
}
