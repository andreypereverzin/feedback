package eu.ec.feedback.service;

import eu.ec.feedback.dao.FeedbackEntryRepository;
import eu.ec.feedback.data.request.FeedbackEntryRequestData;
import eu.ec.feedback.data.response.FeedbackEntryResponseData;
import eu.ec.feedback.model.ContactType;
import eu.ec.feedback.model.FeedbackEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static eu.ec.feedback.model.ContactType.GENERAL;
import static eu.ec.feedback.model.ContactType.MARKETING;
import static eu.ec.feedback.model.ContactType.SUPPORT;
import static eu.ec.feedback.service.SortOrder.ASC;
import static eu.ec.feedback.service.SortOrder.DESC;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {
    private static final int ID_1 = 1;
    private static final int ID_2 = 2;
    private static final int ID_3 = 3;
    private static final int ID_4 = 4;
    private static final String NAME_1 = "name1";
    private static final String NAME_2 = "name2";
    private static final String NAME_3 = "name3";
    private static final String NAME_4 = "name4";
    private static final String EMAIL_1 = "aa@bb.cc";
    private static final String EMAIL_2 = "dd@bb.cc";
    private static final String EMAIL_3 = "ee@ee.ff";
    private static final String EMAIL_4 = "ff@ee.ff";
    private static final ContactType CONTACT_TYPE_1 = GENERAL;
    private static final ContactType CONTACT_TYPE_2 = MARKETING;
    private static final ContactType CONTACT_TYPE_3 = SUPPORT;
    private static final ContactType CONTACT_TYPE_4 = GENERAL;
    private static final Date CREATED_AT_1 = new Date(1L);
    private static final Date CREATED_AT_2 = new Date(2L);
    private static final Date CREATED_AT_3 = new Date(3L);
    private static final Date CREATED_AT_4 = new Date(4L);
    private static final String MESSAGE_1 = "message1";
    private static final String MESSAGE_2 = "message2";
    private static final String MESSAGE_3 = "message3";
    private static final String MESSAGE_4 = "message4";

    private FeedbackService feedbackService;

    @Mock
    private FeedbackEntryRepository feedbackEntryRepository;

    @Mock
    private JsonValidator jsonValidator;

    private FeedbackEntryRequestData feedbackEntryRequestData1;

    @Mock
    private FeedbackEntry feedbackEntry1;

    @Mock
    private FeedbackEntry feedbackEntry2;

    @Mock
    private FeedbackEntry feedbackEntry3;

    @Mock
    private FeedbackEntry feedbackEntry4;

    @BeforeEach
    public void setUp() {
        feedbackService = new FeedbackService(feedbackEntryRepository, jsonValidator);
        feedbackEntryRequestData1 = new FeedbackEntryRequestData(NAME_1, EMAIL_1, GENERAL, MESSAGE_1);
    }

    @Test
    void createFeedbackEntry_shouldCreateFeedbackEntry() throws Exception {
        //given
        ArgumentCaptor<FeedbackEntry> feedbackEntryCaptor = ArgumentCaptor.forClass(FeedbackEntry.class);

        // when
        feedbackService.createFeedbackEntry(feedbackEntryRequestData1);

        // then
        verify(jsonValidator).validate(feedbackEntryRequestData1);
        verify(feedbackEntryRepository).save(feedbackEntryCaptor.capture());
        assertThat(feedbackEntryCaptor.getValue().getName()).isEqualTo(NAME_1);
        assertThat(feedbackEntryCaptor.getValue().getEmail()).isEqualTo(EMAIL_1);
        assertThat(feedbackEntryCaptor.getValue().getContactType()).isEqualTo(GENERAL);
        assertThat(feedbackEntryCaptor.getValue().getMessage()).isEqualTo(MESSAGE_1);
    }

    @Test
    void createFeedbackEntry_shouldThrowIfValidatorFails() throws Exception {
        //given
        doThrow(new IOException("msg")).when(jsonValidator).validate(feedbackEntryRequestData1);

        // when
        // then
        assertThrows(
                HttpServerErrorException.class,
                () -> feedbackService.createFeedbackEntry(feedbackEntryRequestData1),
                "msg"
        );

        verify(jsonValidator).validate(feedbackEntryRequestData1);
        verifyNoInteractions(feedbackEntryRepository);
    }

    @Test
    void getFeedbackEntries_shouldReturnAllEntries() {
        //given
        prepareFeedbackEntry(feedbackEntry1, ID_1, NAME_1, EMAIL_1, CONTACT_TYPE_1, MESSAGE_1, CREATED_AT_1);
        prepareFeedbackEntry(feedbackEntry2, ID_2, NAME_2, EMAIL_2, CONTACT_TYPE_2, MESSAGE_2, CREATED_AT_2);
        prepareFeedbackEntry(feedbackEntry3, ID_3, NAME_3, EMAIL_3, CONTACT_TYPE_3, MESSAGE_3, CREATED_AT_3);
        prepareFeedbackEntry(feedbackEntry4, ID_4, NAME_4, EMAIL_4, CONTACT_TYPE_4, MESSAGE_4, CREATED_AT_4);
        given(feedbackEntryRepository.findAll()).willReturn(asList(feedbackEntry1, feedbackEntry2, feedbackEntry3, feedbackEntry4));

        // when
        List<FeedbackEntryResponseData> res = feedbackService.getFeedbackEntries(null, null);

        // then
        verify(feedbackEntryRepository).findAll();
        verifyNoMoreInteractions(feedbackEntryRepository);

        assertThat(res.size()).isEqualTo(4);
        assertThat(res.get(0).getId()).isEqualTo(ID_1);
        assertThat(res.get(0).getName()).isEqualTo(NAME_1);
        assertThat(res.get(0).getEmail()).isEqualTo(EMAIL_1);
        assertThat(res.get(0).getContactType()).isEqualTo(CONTACT_TYPE_1);
        assertThat(res.get(0).getMessage()).isEqualTo(MESSAGE_1);
        assertThat(res.get(0).getCreatedAt()).isEqualTo(CREATED_AT_1);
        assertThat(res.get(1).getId()).isEqualTo(ID_2);
        assertThat(res.get(1).getName()).isEqualTo(NAME_2);
        assertThat(res.get(1).getEmail()).isEqualTo(EMAIL_2);
        assertThat(res.get(1).getContactType()).isEqualTo(CONTACT_TYPE_2);
        assertThat(res.get(1).getMessage()).isEqualTo(MESSAGE_2);
        assertThat(res.get(1).getCreatedAt()).isEqualTo(CREATED_AT_2);
        assertThat(res.get(2).getId()).isEqualTo(ID_3);
        assertThat(res.get(2).getName()).isEqualTo(NAME_3);
        assertThat(res.get(2).getEmail()).isEqualTo(EMAIL_3);
        assertThat(res.get(2).getContactType()).isEqualTo(CONTACT_TYPE_3);
        assertThat(res.get(2).getMessage()).isEqualTo(MESSAGE_3);
        assertThat(res.get(2).getCreatedAt()).isEqualTo(CREATED_AT_3);
        assertThat(res.get(3).getId()).isEqualTo(ID_4);
        assertThat(res.get(3).getName()).isEqualTo(NAME_4);
        assertThat(res.get(3).getEmail()).isEqualTo(EMAIL_4);
        assertThat(res.get(3).getContactType()).isEqualTo(CONTACT_TYPE_4);
        assertThat(res.get(3).getMessage()).isEqualTo(MESSAGE_4);
        assertThat(res.get(3).getCreatedAt()).isEqualTo(CREATED_AT_4);
    }

    @Test
    void getFeedbackEntries_shouldReturnGeneral() {
        //given
        prepareFeedbackEntry(feedbackEntry1, ID_1, NAME_1, EMAIL_1, CONTACT_TYPE_1, MESSAGE_1, CREATED_AT_1);
        prepareFeedbackEntry(feedbackEntry4, ID_4, NAME_4, EMAIL_4, CONTACT_TYPE_4, MESSAGE_4, CREATED_AT_4);
        given(feedbackEntryRepository.findAllByContactType(GENERAL)).willReturn(asList(feedbackEntry1, feedbackEntry4));

        // when
        List<FeedbackEntryResponseData> res = feedbackService.getFeedbackEntries(GENERAL, null);

        // then
        verify(feedbackEntryRepository).findAllByContactType(GENERAL);
        verifyNoMoreInteractions(feedbackEntryRepository);

        assertThat(res.size()).isEqualTo(2);
        assertThat(res.get(0).getId()).isEqualTo(ID_1);
        assertThat(res.get(0).getName()).isEqualTo(NAME_1);
        assertThat(res.get(0).getEmail()).isEqualTo(EMAIL_1);
        assertThat(res.get(0).getContactType()).isEqualTo(CONTACT_TYPE_1);
        assertThat(res.get(0).getMessage()).isEqualTo(MESSAGE_1);
        assertThat(res.get(0).getCreatedAt()).isEqualTo(CREATED_AT_1);
        assertThat(res.get(1).getId()).isEqualTo(ID_4);
        assertThat(res.get(1).getName()).isEqualTo(NAME_4);
        assertThat(res.get(1).getEmail()).isEqualTo(EMAIL_4);
        assertThat(res.get(1).getContactType()).isEqualTo(CONTACT_TYPE_4);
        assertThat(res.get(1).getMessage()).isEqualTo(MESSAGE_4);
        assertThat(res.get(1).getCreatedAt()).isEqualTo(CREATED_AT_4);
    }

    @Test
    void getFeedbackEntries_shouldReturnGeneralAsc() {
        //given
        prepareFeedbackEntry(feedbackEntry1, ID_1, NAME_1, EMAIL_1, CONTACT_TYPE_1, MESSAGE_1, CREATED_AT_1);
        prepareFeedbackEntry(feedbackEntry4, ID_4, NAME_4, EMAIL_4, CONTACT_TYPE_4, MESSAGE_4, CREATED_AT_4);
        given(feedbackEntryRepository.findAllByContactType(GENERAL)).willReturn(asList(feedbackEntry1, feedbackEntry4));

        // when
        List<FeedbackEntryResponseData> res = feedbackService.getFeedbackEntries(GENERAL, ASC);

        // then
        verify(feedbackEntryRepository).findAllByContactType(GENERAL);
        verifyNoMoreInteractions(feedbackEntryRepository);

        assertThat(res.size()).isEqualTo(2);
        assertThat(res.get(0).getId()).isEqualTo(ID_1);
        assertThat(res.get(0).getName()).isEqualTo(NAME_1);
        assertThat(res.get(0).getEmail()).isEqualTo(EMAIL_1);
        assertThat(res.get(0).getContactType()).isEqualTo(CONTACT_TYPE_1);
        assertThat(res.get(0).getMessage()).isEqualTo(MESSAGE_1);
        assertThat(res.get(0).getCreatedAt()).isEqualTo(CREATED_AT_1);
        assertThat(res.get(1).getId()).isEqualTo(ID_4);
        assertThat(res.get(1).getName()).isEqualTo(NAME_4);
        assertThat(res.get(1).getEmail()).isEqualTo(EMAIL_4);
        assertThat(res.get(1).getContactType()).isEqualTo(CONTACT_TYPE_4);
        assertThat(res.get(1).getMessage()).isEqualTo(MESSAGE_4);
        assertThat(res.get(1).getCreatedAt()).isEqualTo(CREATED_AT_4);
    }

    @Test
    void getFeedbackEntries_shouldReturnGeneralDesc() {
        //given
        prepareFeedbackEntry(feedbackEntry1, ID_1, NAME_1, EMAIL_1, CONTACT_TYPE_1, MESSAGE_1, CREATED_AT_1);
        prepareFeedbackEntry(feedbackEntry4, ID_4, NAME_4, EMAIL_4, CONTACT_TYPE_4, MESSAGE_4, CREATED_AT_4);
        given(feedbackEntryRepository.findAllByContactType(GENERAL)).willReturn(asList(feedbackEntry1, feedbackEntry4));

        // when
        List<FeedbackEntryResponseData> res = feedbackService.getFeedbackEntries(GENERAL, DESC);

        // then
        verify(feedbackEntryRepository).findAllByContactType(GENERAL);
        verifyNoMoreInteractions(feedbackEntryRepository);

        assertThat(res.size()).isEqualTo(2);
        assertThat(res.get(0).getId()).isEqualTo(ID_4);
        assertThat(res.get(0).getName()).isEqualTo(NAME_4);
        assertThat(res.get(0).getEmail()).isEqualTo(EMAIL_4);
        assertThat(res.get(0).getContactType()).isEqualTo(CONTACT_TYPE_4);
        assertThat(res.get(0).getMessage()).isEqualTo(MESSAGE_4);
        assertThat(res.get(0).getCreatedAt()).isEqualTo(CREATED_AT_4);
        assertThat(res.get(1).getId()).isEqualTo(ID_1);
        assertThat(res.get(1).getName()).isEqualTo(NAME_1);
        assertThat(res.get(1).getEmail()).isEqualTo(EMAIL_1);
        assertThat(res.get(1).getContactType()).isEqualTo(CONTACT_TYPE_1);
        assertThat(res.get(1).getMessage()).isEqualTo(MESSAGE_1);
        assertThat(res.get(1).getCreatedAt()).isEqualTo(CREATED_AT_1);
    }

    private void prepareFeedbackEntry(
            FeedbackEntry feedbackEntry,
            int id,
            String name,
            String email,
            ContactType contactType,
            String message,
            Date createdAt
    ) {
        given(feedbackEntry.getId()).willReturn(id);
        given(feedbackEntry.getName()).willReturn(name);
        given(feedbackEntry.getEmail()).willReturn(email);
        given(feedbackEntry.getContactType()).willReturn(contactType);
        given(feedbackEntry.getMessage()).willReturn(message);
        given(feedbackEntry.getCreatedAt()).willReturn(createdAt);
    }
}
