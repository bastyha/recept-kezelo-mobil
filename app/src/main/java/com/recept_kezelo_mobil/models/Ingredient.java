package com.recept_kezelo_mobil.models;

//a recipehez szukseges
public class Ingredient{
    private String nameOfIngredient;
    private double amount;
    private String unit;

    public Ingredient(String nameOfIngredient, double amount, String unit) {
        this.nameOfIngredient = nameOfIngredient;
        this.amount = amount;
        this.unit = unit;
    }

    public Ingredient() {
    }

    public String getNameOfIngredient() {
        return nameOfIngredient;
    }

    public void setNameOfIngredient(String nameOfIngredient) {
        this.nameOfIngredient = nameOfIngredient;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}