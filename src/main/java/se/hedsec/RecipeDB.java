package se.hedsec;

import java.sql.*;

public class RecipeDB {


    //TODO: Implement this method, måste kolla om det ska vara en transaction
    public static boolean addRecipe(Recipe recipe) throws SQLException{
        Connection con = DBManager.getConnection();
        try{
            con.setAutoCommit(false);
            String insertRecipe = "INSERT INTO recipes (name, ingredients, instructions, date, image, videoUrl) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(insertRecipe);
            ps.setString(1, recipe.getName());
            ps.setString(2, recipe.getIngredients());
            ps.setString(3, recipe.getInstructions());
            ps.setDate(4, recipe.getDate());
            ps.setBytes(5, recipe.getImage());
            ps.setString(6, recipe.getVideoUrl());
            ps.executeUpdate();

            if(!checkAuthor(recipe.getAuthor())){
                String insertAuthor = "INSERT INTO author (name) VALUES (?)";
                PreparedStatement ps2 = con.prepareStatement(insertAuthor);
                ps2.setString(1, recipe.getAuthor());
                ps2.executeUpdate();
            }
            int authorId = getAuthorId(recipe.getAuthor());
            String insertAuthorRecipe = "INSERT INTO authorsrecipes (authorID, recipeID) VALUES (?, ?)";
            PreparedStatement ps3 = con.prepareStatement(insertAuthorRecipe);
            ps3.setInt(1, authorId);
            ps3.setInt(2, getRecipeId(recipe.getName()));
            ps3.executeUpdate();
            con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public static boolean checkIfRecipeExists(String videoUrl) throws SQLException {
        Connection con = DBManager.getConnection();
        String checkRecipe = "SELECT * FROM recipes WHERE videoUrl = ?";
        PreparedStatement ps = con.prepareStatement(checkRecipe);
        ps.setString(1, videoUrl);
        return ps.executeQuery().next();
    }
    private static boolean checkAuthor(String author) throws SQLException{
        Connection con = DBManager.getConnection();
        String checkAuthor = "SELECT * FROM author WHERE name = ?";
        PreparedStatement ps = con.prepareStatement(checkAuthor);
        ps.setString(1, author);
        return ps.executeQuery().next();
    }
    private static int getAuthorId(String author) throws SQLException {
        Connection con = DBManager.getConnection();
        String getAuthorId = "SELECT id FROM author WHERE name = ?";
        PreparedStatement ps = con.prepareStatement(getAuthorId);
        ps.setString(1, author);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            throw new SQLException("Author not found");
        }
    }

    private static int getRecipeId(String name) throws SQLException {
        Connection con = DBManager.getConnection();
        String getRecipeId = "SELECT id FROM recipes WHERE name = ?";
        PreparedStatement ps = con.prepareStatement(getRecipeId);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            throw new SQLException("Recipe not found");
        }
    }

}
