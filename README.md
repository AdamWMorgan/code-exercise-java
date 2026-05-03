# URL Shortener Coding Exercise

## Project Implementation

This project was created using Java 21 on the backend and React for the frontend UI, making use of MaterialUI for faster
implementation of the layout.

The pre-existing openapi.yaml is used during the build stage to automatically generate the DTOs and API interfaces.**The 
auto-generation of the objects meant that the context path was used for the objects, i.e. ShortenPost201Response, instead
of a more concise name, however I made an assumption that if you were working to a 3rd party spec and this is what was
provided, you would need to develop against the source of truth.**

## Project Testing

Testing for the backend was implemented using Junit, Mockito and the Spring framework's MockMVC for Spring based requests.
Integration test, in their own module, were implemented using the Cucumber framework alongside Junit. Features were written
in Gherkin.

Frontend testing was achieved using Jest.

## Run Project

To run the project, you simply need to execute the 'docker-compose up' command from the root directory. This will spin up the following:

- Backend API: http://localhost:8080/
- Frontend UI: http://localhost:3000/
- MongoDB: mongodb://mongodb:27017/urlshortener

To prevent data loss between sessions, a Docker volume is mapped to the MongoDB data directory.

## Issues Faced

- OpenAPI contract naming, previously mentioned.
- Reverted to using Flapdoodle's embedded MongoDB to overcome some challenges faced when using Testcontainers. I'd 
initially implemented the Testcontainers approach to stick with the overall usage of Docker, however it was proving 
difficult due to some network issues with my Docker settings that was becoming a bit of a time sink to solve. 


## Productionisation Considerations

A production grade version of this app would require (non-exhaustive) the following:
- User authentication, with history linked to specific users (which would also help filter on single user history when querying).
- Rate limiting to avoid spamming of endpoints for malicious purposes.
- Caching of URLs to avoid unnecessary calls to DB.
- Improved logging and monitoring.
- Database indexing as the data eventually grows to a huge number.
- Concurrency would be ideal here since this is I/O heavy. This project is using Java 21, so it would be quite straightforwards
to enable virtual threads.
