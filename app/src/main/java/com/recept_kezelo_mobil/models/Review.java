package com.recept_kezelo_mobil.models;

import java.util.Date;

public class Review {
    private String id;
    private Long date;
    private String recipe;
    private String reviewer;
    private String text;

    public Review() {
    }

    public Review(String id, Long date, String recipe, String reviewer, String text) {
        this.id = id;
        this.date = date;
        this.recipe = recipe;
        this.reviewer = reviewer;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
