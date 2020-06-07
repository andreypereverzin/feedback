package eu.ec.feedback.controller;

import eu.ec.feedback.Application;
import eu.ec.feedback.data.request.FeedbackEntryRequestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static eu.ec.feedback.model.ContactType.GENERAL;
import static eu.ec.feedback.model.ContactType.SUPPORT;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(classes = Application.class,
        webEnvironment = WebEnvironment.RANDOM_PORT)
public class FeedbackControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void should_create_and_get_entries() {
        // No entries
        ResponseEntity<List> res1 = restTemplate.getForEntity(getUrl(), List.class);
        assertEquals(OK, res1.getStatusCode());
        assertEquals(res1.getBody().size(), 0);

        // Create entry 1
        FeedbackEntryRequestData entry1 = new FeedbackEntryRequestData("name1", "email1", SUPPORT, "message1");
        ResponseEntity<Void> responseEntity1 = restTemplate.postForEntity(getUrl(), entry1, Void.class);
        assertEquals(CREATED, responseEntity1.getStatusCode());

        // Create entry 2
        FeedbackEntryRequestData entry2 = new FeedbackEntryRequestData("name2", "email2", GENERAL, "message2");
        ResponseEntity<Void> responseEntity2 = restTemplate.postForEntity(getUrl(), entry2, Void.class);
        assertEquals(CREATED, responseEntity2.getStatusCode());

        // Create entry 3
        FeedbackEntryRequestData entry3 = new FeedbackEntryRequestData("name3", "email3", SUPPORT, "message3");
        ResponseEntity<Void> responseEntity3 = restTemplate.postForEntity(getUrl(), entry3, Void.class);
        assertEquals(CREATED, responseEntity3.getStatusCode());

        // GET all entries
        ResponseEntity<List> res2 = restTemplate.getForEntity(getUrl(), List.class);
        assertEquals(OK, res2.getStatusCode());
        assertEquals(res2.getBody().size(), 3);
        assertEquals(((Map)res2.getBody().get(0)).get("name"), "name1");
        assertEquals(((Map)res2.getBody().get(1)).get("name"), "name2");
        assertEquals(((Map)res2.getBody().get(2)).get("name"), "name3");

        // GET SUPPORT entries
        ResponseEntity<List> res3 = restTemplate
                .getForEntity(getUrl() + "?contactType=SUPPORT", List.class);
        assertEquals(OK, res3.getStatusCode());
        assertEquals(res3.getBody().size(), 2);
        assertEquals(((Map)res3.getBody().get(0)).get("name"), "name1");
        assertEquals(((Map)res3.getBody().get(1)).get("name"), "name3");

        // GET SUPPORT entries in DESC order
        ResponseEntity<List> res4 = restTemplate
                .getForEntity(getUrl() + "?contactType=SUPPORT&sortOrder=DESC", List.class);
        assertEquals(OK, res4.getStatusCode());
        assertEquals(res4.getBody().size(), 2);
        assertEquals(((Map)res4.getBody().get(0)).get("name"), "name3");
        assertEquals(((Map)res4.getBody().get(1)).get("name"), "name1");
    }

    @Test
    public void should_handle_lower_case_contact_type() {
        ResponseEntity<List> res = restTemplate
                .getForEntity(getUrl() + "?contactType=general", List.class);
        assertEquals(OK, res.getStatusCode());
    }

    @Test
    public void should_handle_lower_case_sort_order() {
        ResponseEntity<List> res = restTemplate
                .getForEntity(getUrl() + "?sortOrder=desc", List.class);
        assertEquals(OK, res.getStatusCode());
    }

    private String getUrl() {
        return "http://localhost:" + port + "/feedback/feedbackentries";
    }
}
