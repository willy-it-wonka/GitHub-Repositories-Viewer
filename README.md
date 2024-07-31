## Description
This application provides a REST API that allows users to retrieve data about non-fork GitHub repositories associated with a specified username, along with detailed information about each repository's branches.

## Technologies

## How to use

## Task and initial assumptions
```
Acceptance criteria:
As an api consumer, given username and header “Accept: application/json”, I would like to list all
his github repositories, which are not forks. Information, which I require in the response, is:
- Repository Name
- Owner Login
- For each branch it’s name and last commit sha

As an api consumer, given not existing github user, I would like to receive 404 response in such a format:
{
    “status”: ${responseCode}
    “message”: ${whyHasItHappened}
}

Notes:
Please full-fill the given acceptance criteria, delivering us your best code compliant with industry standards.
Please use https://developer.github.com/v3 as a backing API
Application should have a proper README.md file
```
  We will need:
  1. **Controller**: serves as the entry point for our API, handling client requests.
  2. **Service**: for business logic, manages communication with GitHub API.
  3. **DTOs**: for separation between layers of the application - 1 to handle responses from GitHub API, 2 as a response to the client of our application.
  4. **Mapper**: converts data retrieved from the GitHub API into the format returns by our application.
  5. **Exception class**.
  6. **Exception handling mechanism**: to format the error responses as required.
