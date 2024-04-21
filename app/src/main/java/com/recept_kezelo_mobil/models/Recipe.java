package com.recept_kezelo_mobil.models;

import java.util.List;

public class Recipe {
    private String id;
    private String name;
    private String owner;
    private int timeInMinutes;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private String image_id;

    public Recipe() {
    }

    public Recipe(String id, String name, String owner, int timeInMinutes, List<Ingredient> ingredients, List<Step> steps, String image_id) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.timeInMinutes = timeInMinutes;
        this.ingredients = ingredients;
        this.steps = steps;
        this.image_id = image_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getTimeInMinutes() {
        return timeInMinutes;
    }

    public void setTimeInMinutes(int timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }
}
