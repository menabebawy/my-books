package com.mybooks.oauthserver.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

import java.util.Set;

@Data
@DynamoDBTable(tableName = "User")
public class UserEntity {
    @DynamoDBHashKey
    String id;

    @DynamoDBAttribute
    String name;

    @DynamoDBAttribute
    String email;

    @DynamoDBAttribute
    String password;

    @DynamoDBAttribute
    private Set<String> roles;
}
