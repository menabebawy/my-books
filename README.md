# my-books

## Table of Contents

1. [Overview](#overview)
2. [Technologies](#tech-stack)
3. [Getting Started](#getting-started)
4. [Building](#build)
5. [REST APIs](#rest-apis)
6. [Books](#books)

## Overview

A Book Store server side project in purpose of providing APIs that are used to CRUD books and their authors.

## Tech stack

* Java 11
* Spring Boot
* AWS Dynamo DB
* AWS Cognito
* OAuth2
* JWT
* JUnit5
* Mockito
* Lombok
* MapStruct
* Swagger
* Microservices (implemented on a feature branch)

## Getting Started

Create the particular tables on AWS DynamoDB and make sure to provide your AWS credentials in order to run successfully
the app.

You can rely on model `infrastracture` where these tables are created by AWS CDK JAVA.

### Build

From the root folder, just execute the following command which will build/run unit and integration tests.

`./gradlew clean build`

### REST APIs

#### Books

1. Get book by id `GET /book/{id}`

   Response `200`

    ```json
    {
      "id": "d18784b8-a7f3-4d45-b45c-2688e643b9f2",
      "title": "Goals",
      "authorId": "112bf093-a47e-4d09-a2a8-02c20ef5e2f7"
    }
    ```

2. Add new book `POST /book`

   Response `201`
    ```json
    {
      "id": "931e3c25-de9d-4a88-b955-021bcd3c1616"
    }
    ```

3. Update a book `PUT /book/{id}`

   Response `200`
    ```json
    {
      "id": "d18784b8-a7f3-4d45-b45c-2688e643b9f2",
      "title": "My Goals",
      "authorId": "112bf093-a47e-4d09-a2a8-02c20ef5e2f7"
    }
    ```

4. Delete a book `DELETE /book/{id}`

   Response `204`