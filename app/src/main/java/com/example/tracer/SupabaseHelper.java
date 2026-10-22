package com.example.tracer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SupabaseHelper {

    private static final String SUPABASE_URL = "https://vkqmqseenyzsiserhifw.supabase.co";
    private static final String SUPABASE_KEY = "sb_publishable_bEEgxhYrzI8Z9Qcjw498NA_7j_U5J67";

    private static final OkHttpClient client = new OkHttpClient();

    public interface NotificationFetchCallback {
        void onSuccess(JSONArray results);
        void onFailure(String error);
    }

    public static void fetchNotifications(NotificationFetchCallback callback) {
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/notifications?select=*&order=posted_at.desc")
                .get()
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                callback.onFailure(e != null ? e.getMessage() : "Network error");
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String json = response.body().string().trim();
                        if (json.startsWith("[")) {
                            callback.onSuccess(new JSONArray(json));
                        } else if (json.startsWith("{")) {
                            JSONObject obj = new JSONObject(json);
                            if (obj.has("message")) {
                                callback.onFailure(obj.getString("message"));
                            } else {
                                // Wrap single object into array
                                JSONArray arr = new JSONArray();
                                arr.put(obj);
                                callback.onSuccess(arr);
                            }
                        } else {
                            callback.onSuccess(new JSONArray());
                        }
                    } catch (JSONException e) {
                        callback.onFailure("Parse error: " + e.getMessage());
                    }
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "";
                    callback.onFailure("Response code: " + response.code() + " " + errorBody);
                }
                response.close();
            }
        });
    }

    public interface SmsFetchCallback {
        void onSuccess(JSONArray results);
        void onFailure(String error);
    }

    public static void fetchSms(SmsFetchCallback callback) {
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/sms_messages?select=*&order=received_at.desc")
                .get()
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                callback.onFailure(e != null ? e.getMessage() : "Network error");
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String json = response.body().string().trim();
                        if (json.startsWith("[")) {
                            callback.onSuccess(new JSONArray(json));
                        } else if (json.startsWith("{")) {
                            JSONObject obj = new JSONObject(json);
                            if (obj.has("message")) {
                                callback.onFailure(obj.getString("message"));
                            } else {
                                JSONArray arr = new JSONArray();
                                arr.put(obj);
                                callback.onSuccess(arr);
                            }
                        } else {
                            callback.onSuccess(new JSONArray());
                        }
                    } catch (JSONException e) {
                        callback.onFailure("Parse error: " + e.getMessage());
                    }
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "";
                    callback.onFailure("Response code: " + response.code() + " " + errorBody);
                }
                response.close();
            }
        });
    }
}
