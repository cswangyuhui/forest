package com.forest.common;

import org.springframework.data.mongodb.core.mapping.Field;

public class weChatOutPutDetail {

    private String feature;

    private String environment;

    private String growthHabit;

    private String geographicalDistribution;

    private String morphology;

    private String medical;

    private String ecological;

    private String story;

    public weChatOutPutDetail(String feature, String environment, String growthHabit, String geographicalDistribution, String morphology, String medical, String ecological, String story) {
        this.feature = feature;
        this.environment = environment;
        this.growthHabit = growthHabit;
        this.geographicalDistribution = geographicalDistribution;
        this.morphology = morphology;
        this.medical = medical;
        this.ecological = ecological;
        this.story = story;
    }

    public weChatOutPutDetail() {
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getGrowthHabit() {
        return growthHabit;
    }

    public void setGrowthHabit(String growthHabit) {
        this.growthHabit = growthHabit;
    }

    public String getGeographicalDistribution() {
        return geographicalDistribution;
    }

    public void setGeographicalDistribution(String geographicalDistribution) {
        this.geographicalDistribution = geographicalDistribution;
    }

    public String getMorphology() {
        return morphology;
    }

    public void setMorphology(String morphology) {
        this.morphology = morphology;
    }

    public String getMedical() {
        return medical;
    }

    public void setMedical(String medical) {
        this.medical = medical;
    }

    public String getEcological() {
        return ecological;
    }

    public void setEcological(String ecological) {
        this.ecological = ecological;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }
}
