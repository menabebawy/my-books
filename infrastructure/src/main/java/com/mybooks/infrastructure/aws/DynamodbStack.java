package com.mybooks.infrastructure.aws;

import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.core.RemovalPolicy;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.BillingMode;
import software.amazon.awscdk.services.dynamodb.Table;
import software.constructs.Construct;

public class DynamodbStack extends Stack {
    public DynamodbStack(@Nullable Construct scope, @Nullable String id) {
        super(scope, id);
        createTable("book");
        createTable("author");
    }

    private void createTable(String tableName) {
        Table.Builder.create(this, tableName)
                .tableName(tableName)
                .removalPolicy(RemovalPolicy.DESTROY)
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .partitionKey(getTablePk())
                .build();
    }

    private Attribute getTablePk() {
        return Attribute.builder().name("pk").type(AttributeType.STRING).build();
    }
}
