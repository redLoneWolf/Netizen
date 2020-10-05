package com.sudhar.netizen;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {


    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            activity.getWindow().getDecorView().findViewById(android.R.id.content).clearFocus();
        }


    }


    public static class OffsetDateTimeDeserializer implements JsonDeserializer<OffsetDateTime> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        public OffsetDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            return OffsetDateTime.from(formatter.parse(json.getAsString()));
        }
    }

    public static class OffsetDateTimeSerializer implements JsonSerializer<OffsetDateTime> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        public JsonElement serialize(OffsetDateTime src, Type typeOfSrc, JsonSerializationContext context) {

            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            return new JsonPrimitive(src.format(formatter));
        }
    }

}
