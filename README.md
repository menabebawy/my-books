# my-books

## Table of Contents

1. [Overview](#overview)
2. [Technologies](#tech-stack)
3. [Getting Started](#getting-started)
4. [Building](#build)
5. [REST APIs](#rest-apis)
6. [User](#user)
7. [Book](#book)
8. [Author](#author)

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

You can rely on model `infrastracture` where these tables are created by AWS CDK JAVA. All APIs are defined on model
`api` .

You have to authorize firstly to get `access token` & `refresh token`, then use `refresh token` to gain a new
`access token` if it expires. Books and authors APIs are exposed by `access token`, otherwise you will get `401
Unauthorized`.

### Build

From the root folder, just execute the following command which will build/run unit and integration tests.

`./gradlew clean build`

### REST APIs

#### User

1. Login user `POST /user/login`

   Request body
   ```json
    {
      "email": "foo.bar@test.com",
      "password": "user@123"
    }
    ```
   Response `200`

    ```json
    {
      "accessToken": "{access_token}",
      "refreshToken": "{refreshToken}"
    }
    ```

2. Signup new user `POST /user/signup`

   Request body
   ```json
    {
      "email": "foo.bar@test.com",
      "password": "user@123"
    }
    ```
   Response `201`

3. Refresh access token `GET /user/refresh-token`

   Response `200`
    ```json
    {
      "accessToken": "{access_token}",
      "refreshToken": "{refreshToken}"
    }
    ```

#### Book

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

   Request body
   ```json
    {
      "title": "Sky is not far",
      "authorId": "112bf093-a47e-4d09-a2a8-02c20ef5e2f7"
    }
    ```
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

#### Author

1. Get author by id `GET /author/{id}`

   Response `200`

    ```json
    {
      "id": "112bf093-a47e-4d09-a2a8-02c20ef5e2f7",
      "firstName": "Foo",
      "lastName": "Bar"
    }
    ```

2. Add new author `POST /author`

   Request body
   ```json
    {
      "firstName": "Json",
      "lastName": "Jakarta"
    }
    ```
   Response `201`
    ```json
    {
      "id": "81f4ee81-9e40-4a8d-a0c7-0d9df3e6ee2a"
    }
    ```

3. Update an author `PUT /author/{id}`

   Response `200`
    ```json
    {
      "id": "81f4ee81-9e40-4a8d-a0c7-0d9df3e6ee2a",
      "firstName": "Json",
      "lastName": "Bar"
    }
    ```

4. Delete an author `DELETE /book/{id}`

   Response `204`