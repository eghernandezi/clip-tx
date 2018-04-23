package com.clip.assessment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Transaction {

    private String id;

    private Double amount;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;

    private Integer userId;

    public Boolean validate() {
        return amount != null && amount > 0
                && description != null && description.trim() != ""
                && date != null
                && userId != null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    @JsonProperty("user_id")
    public Integer getUserId() {
        return userId;
    }
}
