package se.hedsec;

import java.sql.Date;

public class RecipePreFormatting {

    private String username;
    private Date date;
    private String recipe;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "RecipePreFormatting{" +
                "username='" + username + '\'' +
                ", date=" + date +
                ", recipe='" + recipe + '\'' +
                '}';
    }
}
