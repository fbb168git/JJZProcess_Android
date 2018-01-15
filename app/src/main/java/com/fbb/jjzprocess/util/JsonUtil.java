package com.fbb.jjzprocess.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    public static <T> String toJson(Object obj, Class clazz) {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(obj, clazz);
    }
    
    public static <T> String toJson(T obj) {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(obj, new TypeToken<T>(){}.getType());
    }
    
    public static <T> String toJson(List<T> obj) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(obj, new TypeToken<List<T>>() {}.getType());
    }

    public static <T> T objFromJson(String dataContentOfProtocal, Class<T> clazz) {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(dataContentOfProtocal, clazz);
    }

    public static <T> ArrayList<T> listFromJson(String dataContentOfProtocal, Class<T> clazz) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        ArrayList<T> lst =  new ArrayList<T>();
        JsonArray array = new JsonParser().parse(dataContentOfProtocal).getAsJsonArray();
        for(final JsonElement elem : array){
            lst.add(gson.fromJson(elem, clazz));
        }
//        Type type = new TypeToken<ArrayList<T>>(){}.getType();
//        ArrayList<T> objs = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(dataContentOfProtocal, type);
        return lst;
    }

}
