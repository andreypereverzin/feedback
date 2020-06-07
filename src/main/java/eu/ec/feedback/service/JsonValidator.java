package eu.ec.feedback.service;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import eu.ec.feedback.data.request.FeedbackEntryRequestData;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class JsonValidator {

    private static final ObjectMapper MAPPER = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);

    private final JsonSchema jsonSchemaValidator;

    public JsonValidator() throws IOException, ProcessingException {
        try (InputStream inputStream = getClass().getResourceAsStream("/schema.json")) {
            jsonSchemaValidator = JsonSchemaFactory
                .byDefault()
                .getJsonSchema(MAPPER.readTree(inputStream));
        }
    }

    public void validate(FeedbackEntryRequestData entry) throws IOException, ProcessingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MAPPER.writeValue(baos, entry);
        ProcessingReport report = jsonSchemaValidator.validate(MAPPER.readTree(baos.toByteArray()), true);

        if (!report.isSuccess()) {
            throw new HttpClientErrorException(BAD_REQUEST, "Error validating feedback entry");
        }
    }
}
