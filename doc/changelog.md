# Changelog

## 1

- Update dependencies to last version to avoid vulnerabilities.
- Change code to be able to compile under new dependency versions.
- Create get request to test that the spring controller is running.

## 2

- Implement basic actions in controller (GET wallet, and POST topup on wallet).
  - TODO: implement auth interceptor to make sure user can do those actions,
  and identify user behind request.

## 3

- Create use cases and connect controller to use cases

## 4

- Refactor stripe test to use Wiremock for server mocking, and testcontainers
 for reducing local setup of wiremock.
  - TODO: Fix issue with missing wiremock extension not working

## 5

- Implemented topup wallet use case
  - TODO: Implement hibernate repositories with record locking mechanism to protect concurrency

## 6

- Implemented hibernate repositories
  - TODO: Add missing configuration (e.g. Flyway migration) to have the database created and ready for IT
