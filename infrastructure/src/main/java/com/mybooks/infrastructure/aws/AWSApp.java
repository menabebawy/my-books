package com.mybooks.infrastructure.aws;

import software.amazon.awscdk.core.App;

public class AWSApp {
    public static void main(String[] args) {
        App app = new App();
        new DynamodbStack(app, "my-books-dynamodb");
        app.synth();
    }
}
