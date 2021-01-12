package com.xqd.mylibrary.utlis;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 * Gson的方法类
 */

public class DataFactory {

    /**
     * 设置自定义TypeAdapter
     *
     * @param clazz
     * @param json
     * @return
     */
    public static Object getInstanceByJson(Class<?> clazz, String json) {
        Object obj = null;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
                .registerTypeAdapter(boolean.class, booleanAsIntAdapter)
                .create();
        obj = gson.fromJson(json, clazz);
        return obj;
    }

    /**
     * json字符串转换为list(这是先转换为数组，再把数组转换为集合)
     * 设置自定义TypeAdapter
     * 由Arrays.asList() 返回的是Arrays的内部类ArrayList， 而不是java.util.ArrayList。Arrays的内部类ArrayList和java.util.ArrayList都是继承AbstractList，
     * remove、add等方法AbstractList中是默认throw UnsupportedOperationException而且不作任何操作
     *
     * @param json
     * @param clazz
     * @return
     * @author I321533
     */
    public static <T> List<T> jsonToList(String json, Class<T[]> clazz) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
                .registerTypeAdapter(boolean.class, booleanAsIntAdapter)
                .create();
        T[] array = gson.fromJson(json, clazz);
        return Arrays.asList(array);//
    }

    /**
     * 自定义TypeAdapter
     */
    private static final TypeAdapter<Boolean> booleanAsIntAdapter = new TypeAdapter<Boolean>() {
        @Override
        public void write(com.google.gson.stream.JsonWriter out, Boolean value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value);
            }
        }

        @Override
        public Boolean read(com.google.gson.stream.JsonReader in) throws IOException {
            JsonToken peek = in.peek();
            switch (peek) {
                case BOOLEAN:
                    return in.nextBoolean();
                case NULL:
                    in.nextNull();
                    return null;
                case NUMBER:
                    return in.nextInt() != 0;
                case STRING:
                    return Boolean.parseBoolean(in.nextString());
                default:
                    throw new IllegalStateException("Expected BOOLEAN or NUMBER but was " + peek);
            }
        }


    };

    /**
     * 设置自定义TypeAdapter，转换为集合
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
                .registerTypeAdapter(boolean.class, booleanAsIntAdapter)
                .create();
        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(gson.fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

    /**
     * 转换为集合
     *
     * @param json
     * @param t
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToBeanList(String json, Class<T> t) {
        List<T> list = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonArray jsonarray = parser.parse(json).getAsJsonArray();
        for (JsonElement element : jsonarray) {
            list.add(new Gson().fromJson(element, t));
        }
        return list;
    }

    public static <T> T getTFromJson(String jsonString, Class<T> t) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, t);
    }

    /**
     * 转换为集合
     *
     * 如果用布尔去解析01会崩
     *
     * @param jsonString
     * @param <T>
     * @return
     */
    public static <T> T getListFromJson(String jsonString, TypeToken typeToken) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, typeToken.getType());
    }

    public static ArrayList getArrayListFromJson(String jsonString) {
        JsonParser jsonParser = new JsonParser();
        Gson gson = new Gson();
        JsonElement jsonElement = jsonParser.parse(jsonString);
        ArrayList arrayList = new ArrayList();
        if (jsonElement.isJsonObject()) {
            arrayList.add(gson.fromJson(jsonElement, HashMap.class));
        } else if (jsonElement.isJsonArray()) {
            arrayList = getListFromJson(jsonString, new TypeToken<ArrayList>() {
            });
        }
        return arrayList;
    }

    public static ArrayList getArrayListMapFromJson(String jsonString) {
        JsonParser jsonParser = new JsonParser();
        Gson gson = new Gson();
        JsonElement jsonElement = jsonParser.parse(jsonString);
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        if (jsonElement.isJsonObject()) {
            arrayList.add(gson.fromJson(jsonElement, HashMap.class));
        } else if (jsonElement.isJsonArray()) {
            arrayList = getListFromJson(jsonString, new TypeToken<ArrayList<HashMap<String, Object>>>() {
            });
        }
        return arrayList;
    }

    public static Object getObjectListFromJson(String jsonString) {
        Gson gson = new Gson();
        Object object = gson.fromJson(jsonString, new TypeToken<List<Object>>() {
        }.getType());
        return object;
    }

    public static Object getObjectFromJson(String jsonString) {
        Gson gson = new Gson();
        Object object = gson.fromJson(jsonString, Object.class);
        return object;
    }


}
