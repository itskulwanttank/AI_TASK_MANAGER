package com.cools01.aitaskmanager;
public class AiTopics {
    private String topicName;
    private String topicData;

    public AiTopics() {
        // Empty constructor needed for Firebase
    }

    public AiTopics(String topicName, String topicData) {
        this.topicName = topicName;
        this.topicData = topicData;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getTopicData() {
        return topicData;
    }
}

