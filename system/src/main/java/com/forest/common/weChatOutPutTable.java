package com.forest.common;

import org.springframework.data.mongodb.core.mapping.Field;

public class weChatOutPutTable {
    private String number;

    private String type;

    private String section;

    private String part;

    private String name;

    private String latinName;

    private String alias;

    private String flower;

    private String fruit;

    public weChatOutPutTable() {
    }

    public weChatOutPutTable(String number, String type, String section, String part, String name, String latinName, String alias, String flower, String fruit) {
        this.number = number;
        this.type = type;
        this.section = section;
        this.part = part;
        this.name = name;
        this.latinName = latinName;
        this.alias = alias;
        this.flower = flower;
        this.fruit = fruit;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFlower() {
        return flower;
    }

    public void setFlower(String flower) {
        this.flower = flower;
    }

    public String getFruit() {
        return fruit;
    }

    public void setFruit(String fruit) {
        this.fruit = fruit;
    }
}
