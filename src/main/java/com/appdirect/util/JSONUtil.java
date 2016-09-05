package com.appdirect.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by abidk on 05/09/16.
 * Defines JSON utility methods
 */
public class JSONUtil {

    /**
     *
     * @param jsonObject
     * @param field
     * @return
     */
    public static Long getLong(JSONObject jsonObject, String field){
        return  jsonObject.getLong(field);
    }

    /**
     *
     * @param jsonObject
     * @param field
     * @return
     */
    public static String getString(JSONObject jsonObject, String field){
        return  jsonObject.getString(field);
    }

    /**
     *
     * @param jsonObject
     * @param field
     * @return
     */
    public static JSONObject getJSONObject(JSONObject jsonObject, String field){
        return  jsonObject.getJSONObject(field);
    }

    /**
     *
     * @param jsonObject
     * @param field
     * @return
     */
    public static JSONArray getJSONArray(JSONObject jsonObject, String field){
        return  jsonObject.getJSONArray(field);
    }
}
