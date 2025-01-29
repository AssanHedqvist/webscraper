package se.hedsec;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {

    private String name;
    private List<String> ingredients;
    private List<String> instructions;

    public Recipe() {}
    public Recipe(String name, List<String>ingredients, List<String> instructions) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", instructions=" + instructions +
                '}';
    }
}
