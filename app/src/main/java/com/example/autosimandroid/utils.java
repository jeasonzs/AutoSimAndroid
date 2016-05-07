package com.example.autosimandroid;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Administrator on 2016/5/7.
 */
public class utils {
    public static int[] jsonGetIntArray(JSONArray jsonArray) {
        try {
            int[] integer = new int[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                integer[i] = jsonArray.getInt(i);
            }
            return integer;
        }catch (JSONException e) {
            return null;
        }
    }
}
