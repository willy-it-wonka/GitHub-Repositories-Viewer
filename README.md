## Description
This application provides REST API to retrieve data about non-fork repositories from the GitHub API, associated with the specified username, along with detailed information about the branches of each repository.
</br></br>

## Technologies
![Static Badge](https://img.shields.io/badge/Java-21-blue?style=for-the-badge&labelColor=black) ![Static Badge](https://img.shields.io/badge/Spring_Boot-3.3.2-blue?style=for-the-badge&labelColor=black) ![Static Badge](https://img.shields.io/badge/maven-3.9.6-blue?style=for-the-badge&labelColor=black) </br>
![Static Badge](https://img.shields.io/badge/GitHub_API-3-blue?style=for-the-badge&labelColor=black) ![Static Badge](https://img.shields.io/badge/webclient-black?style=for-the-badge) </br>
![Static Badge](https://img.shields.io/badge/rest_api-black?style=for-the-badge) ![Static Badge](https://img.shields.io/badge/json-black?style=for-the-badge) </br>
![Static Badge](https://img.shields.io/badge/chat_gtp-4-blue?style=for-the-badge&labelColor=black)
</br></br>

## How to use
1. Clone the repository. [Instruction](https://www.jetbrains.com/help/idea/set-up-a-git-repository.html#clone-repo).
2. Go to GitHubRepositoriesViewerApplication class and run it.
3. Now you can send http requests using [Postman](https://www.postman.com) or use a browser.
   ```
   http://localhost:8080/api/v1/users/willy-it-wonka/repos
   ```
   ```
   http://localhost:8080/api/v1/users/non-existing-user/repos
   ```
</br>

## Task and initial assumptions
```
Acceptance criteria:
As an api consumer, given username and header “Accept: application/json”,
I would like to list all his github repositories, which are not forks.
Information, which I require in the response, is:
- Repository Name
- Owner Login
- For each branch it’s name and last commit sha

As an api consumer, given not existing github user, I would like to receive 404 response
in such a format:
{
    “status”: ${responseCode}
    “message”: ${whyHasItHappened}
}

Notes:
- Please full-fill the given acceptance criteria, delivering us your best code compliant
with industry standards.
- Please use https://developer.github.com/v3 as a backing API.
- Application should have a proper README.md file.
```
We will need:
  1. **Controller**: serves as the entry point for our API, handling client requests.
  2. **Service**: for business logic, manages communication with GitHub API.
  3. **DTOs**: for separation between layers of the application - 1 to handle responses from GitHub API, 2 as a response to the client of our application.
  4. **Mapper**: converts data retrieved from the GitHub API into the format returns by our application.
  5. **Exception class**.
  6. **Exception handling mechanism**: to format the error responses as required.
