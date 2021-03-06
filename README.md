# Feedback Application

## Usage
This is a project using maven and jdk 8.

Download as a zip or git clone https://github.com/andreypereverzin/feedback

Run the following command to build the application:

`mvn clean install`

To start the application run this command:

`java -jar target/feedback.jar`


To run UI application:

go to `src/main/js/application`

and run this command:

`npm start`

After that go to this URL in your browser:

`http://localhost:4200/`

## Application Endpoints
After the application is started its Swagger documentation is available on the URL:

http://localhost:8080/feedback/swagger-ui.html

This URL provides interface for testing the application.
 
In JSON format Swagger documentation is available on this URL: http://localhost:8080/feedback/v2/api-docs.

## Actuator Endpoints
The following actuator endpoints are available after the application is started:

http://localhost:8080/feedback/actuator/health

http://localhost:8080/feedback/actuator/info

## Implementation Notes
The specification can be found in `doc/SoftwareEngineerInterview-Practicalexercice-290520-1552-76.pdf` file.

This implementation uses h2 in-memory database for data persistence.

## Outstanding Issues
1) HATEOAS should be used when building REST responses
2) Better error handling should be implemented
3) Pagination should be implemented in GET endpoints for multiple feedback entries
4) Error responses should be more informative and contain detailed description of the error
5) In real application proper SQL database should be used instead of h2 in-memory database
6) Other Actuator endpoints should be enabled (/metrics, /env etc)
7) Include generation of project reports in `pom.xml` (Cobertura, PMD etc.)
