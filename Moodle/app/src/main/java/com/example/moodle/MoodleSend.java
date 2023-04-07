package com.example.moodle;

import android.content.Context;
import android.os.StrictMode;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MoodleSend {
        private static String BASE_URL="https://fcm.googleapis.com/fcm/send";
    private static String SERVER_KEY="AAAASEW5G04:APA91bEkspqLLM3wn0B76hSnmOncbpQRfwiIN52DUgtk879Fnp8wCTrho-dkgmAeP40fBWHnHlzf5oNTcT9JIxMF9qkq-uH2t6T0lqiwBCkJekNUCxDGJGVenhmTPQO9YDrLStCtMzlO";

    public static void pushNotification(Context context,String token,String title,String message){
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestQueue queue= Volley.newRequestQueue(context);
        try{
            JSONObject json=new JSONObject();
            json.put("to",token);
            JSONObject notification=new JSONObject();
            notification.put("title",title);
            notification.put("body",message);
            json.put("notification",notification);

            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, BASE_URL, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("MoodleSend"+response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String,String> getHeaders() throws AuthFailureError{
                    Map<String,String> params=new HashMap<>();
                    params.put("Content-Type","application/json");
                    params.put("Authorization","key="+SERVER_KEY);
                    return params;
                }
            };
            queue.add(jsonObjectRequest);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
}
