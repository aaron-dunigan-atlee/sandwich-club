package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) throws JSONException {
        Sandwich sandwich = new Sandwich();
        JSONObject sandwichJson = new JSONObject(json);
        JSONObject nameJson = sandwichJson.getJSONObject("name");
        JSONArray ingredientsJson = sandwichJson.getJSONArray("ingredients");
        List<String> ingredients = jsonArrayToStringList(ingredientsJson);
        sandwich.setIngredients(ingredients);
        sandwich.setMainName(nameJson.getString("mainName"));
        sandwich.setDescription(sandwichJson.getString("description"));
        sandwich.setImage(sandwichJson.getString("image"));
        sandwich.setPlaceOfOrigin(sandwichJson.getString("placeOfOrigin"));
        sandwich.setAlsoKnownAs(jsonArrayToStringList(nameJson.getJSONArray("alsoKnownAs")));
        return sandwich;
    }

    // Convert a JSONArray to a List of strings.
    private static List<String> jsonArrayToStringList(JSONArray jsonArray) throws JSONException {
        List<String> stringList = new ArrayList<String>() {
        };
        for (int i=0; i<jsonArray.length(); i++) {
            stringList.add(jsonArray.getString(i));
        }
        return stringList;
    }
}
