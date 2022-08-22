package com.mybooks.bookratingservice.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

import java.util.Map;

@Data
@DynamoDBTable(tableName = "Book-Rating")
public class UserBooksRatings {
    @DynamoDBHashKey
    String userId;

    @DynamoDBAttribute
    Map<String, Integer> booksRatings;
}
