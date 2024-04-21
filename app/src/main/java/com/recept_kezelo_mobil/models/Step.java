package com.recept_kezelo_mobil.models;

//a recipehez szukseges
public class Step{
    private String stepDescription;

    public Step(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public Step() {
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }
}
