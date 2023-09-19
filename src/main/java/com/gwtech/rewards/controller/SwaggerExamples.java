package com.gwtech.rewards.controller;

public class SwaggerExamples {
    public static final String EXAMPLE_TRANSACTION_REQUEST = "{\n" +
            "  \"title\": \"FV 10/09/2023\",\n" +
            "  \"purchaseValue\": 101.15,\n" +
            "  \"purchaseAt\": \"2023-09-19 11:30:38\"\n" +
            "}";

    public static final String EXAMPLE_TRANSACTION_RESPONSE = "{\n" +
            "  \"id\": \"11\",\n" +
            "  \"title\": \"FV 10/09/2023\",\n" +
            "  \"purchaseValue\": 101.15,\n" +
            "  \"purchaseAt\": \"2023-09-19 11:30:38\"\n" +
            "}";

    public static final String EXAMPLE_CUSTOMER_REQUEST = "{\n" +
            "  \"name\": \"Customer Name\"\n" +
            "}";

    public static final String EXAMPLE_CUSTOMER_RESPONSE = "{\n" +
            "  \"id\": \"1\",\n"  +
            "  \"name\": \"Customer Name\"\n" +
            "}";

    public static final String EXAMPLE_REWARDS_REPORT = "[\n" +
            "  {\n" +
            "    \"customerName\": \"Customer Name\",\n" +
            "    \"customerId\": 1,\n" +
            "    \"rewards\": [\n" +
            "      {\n" +
            "        \"yearMonth\": \"2023-07\",\n" +
            "        \"rewardValue\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"yearMonth\": \"2023-08\",\n" +
            "        \"rewardValue\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"yearMonth\": \"2023-09\",\n" +
            "        \"rewardValue\": 162\n" +
            "      }\n" +
            "    ],\n" +
            "    \"totalRewardValue\": 162\n" +
            "  },\n" +
            "  {\n" +
            "    \"customerName\": \"Customer Abc\",\n" +
            "    \"customerId\": 2,\n" +
            "    \"rewards\": [\n" +
            "      {\n" +
            "        \"yearMonth\": \"2023-07\",\n" +
            "        \"rewardValue\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"yearMonth\": \"2023-08\",\n" +
            "        \"rewardValue\": 0\n" +
            "      },\n" +
            "      {\n" +
            "        \"yearMonth\": \"2023-09\",\n" +
            "        \"rewardValue\": 1\n" +
            "      }\n" +
            "    ],\n" +
            "    \"totalRewardValue\": 1\n" +
            "  }\n" +
            "]";
}
