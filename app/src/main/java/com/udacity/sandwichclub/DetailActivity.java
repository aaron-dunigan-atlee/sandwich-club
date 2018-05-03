package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private TextView mOriginTextView, mAlsoKnownTextView, mIngredientsTextView, mDescriptionTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        // Get array of all sandwich details from strings.xml and pull out the json string for
        // the sandwich in question.
        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        // Pass this to JsonUtils and return a Sandwich object
        try {
            Sandwich sandwich = JsonUtils.parseSandwichJson(json);
            if (sandwich == null) {
                // Sandwich data unavailable
                closeOnError();
                return;
            }
            // Populate the UI with the sandwich details.
            populateUI(sandwich);
            Picasso.with(this)
                    .load(sandwich.getImage())
                    .into(ingredientsIv);

            setTitle(sandwich.getMainName());

        } catch (JSONException e) {
            Log.e("ParseError","Failed to parse JSON: " + e);
            closeOnError();
        }



    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        String defaultText = getResources().getString(R.string.empty_field_default_text);
        mDescriptionTextView = (TextView) findViewById(R.id.description_tv);
        setTextWithDefault(mDescriptionTextView,sandwich.getDescription(),defaultText);
        mAlsoKnownTextView = (TextView) findViewById(R.id.also_known_tv);
        mAlsoKnownTextView.setText(listToString(sandwich.getAlsoKnownAs()));
        mIngredientsTextView = (TextView) findViewById(R.id.ingredients_tv);
        mIngredientsTextView.setText(listToString(sandwich.getIngredients()));
        mOriginTextView = (TextView) findViewById(R.id.origin_tv);
        setTextWithDefault(mOriginTextView,sandwich.getPlaceOfOrigin(),defaultText);
    }

    private String listToString(List<String> stringList) {
        // Convert a list of strings to a single string separated by newline characters.
        // If list is empty, return "N/A"
        if (stringList.size() == 0) {
            return getResources().getString(R.string.empty_list_default_text);
        }
        String text = stringList.get(0);
        for (int i=1; i<stringList.size(); i++) {
            text += "\n" + stringList.get(i);
        }

        return text;
    }

    private void setTextWithDefault(TextView textView, String text, String defaultText) {
        // Set a TextView's text, but if string is empty, set to the default.
        if (text.isEmpty()) {
            textView.setText(defaultText);
        } else {
            textView.setText(text);
        }
        return;
    }
}
