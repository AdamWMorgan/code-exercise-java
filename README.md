# URL Shortener Coding Exercise

<img width="952" height="646" alt="image" src="https://github.com/user-attachments/assets/0e1426d5-f46d-49d1-8466-43b9a1c07d76" />

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

UTs
<img width="961" height="170" alt="image" src="https://github.com/user-attachments/assets/f153fd59-d019-44f8-bf61-c8842ad100c2" />
<br>
ITs
<img width="374" height="194" alt="image" src="https://github.com/user-attachments/assets/e2ade916-dd7d-4eb9-9e6f-7cf947c94a31" />
<br>
Frontend testing was achieved using Jest.

<img width="386" height="221" alt="image" src="https://github.com/user-attachments/assets/104e71c8-6dc8-4ee3-91c0-31de95b45bd1" />


## Build & Run Project

To build and run the project, you simply need to execute the 'docker-compose up --build' command from the root directory. This will spin up the following:

- Backend API: http://localhost:8080/
- Frontend UI: http://localhost:3000/
- MongoDB (Internal): mongodb://mongodb:27017/urlshortener
- MongoDB (External/Local): mongodb://localhost:27017
<br>
<img width="1353" height="195" alt="image" src="https://github.com/user-attachments/assets/7b969bb4-fc0e-4d8b-b6e4-006167dec3ad" />
<br>
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
