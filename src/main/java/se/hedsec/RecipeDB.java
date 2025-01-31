package se.hedsec;

import java.sql.Connection;

public class RecipeDB {


    //TODO: Implement this method, måste kolla om det ska vara en transaction
    public static boolean addRecipe(Recipe recipe) {
        Connection con = DBManager.getConnection();

        return true;
    }
}
