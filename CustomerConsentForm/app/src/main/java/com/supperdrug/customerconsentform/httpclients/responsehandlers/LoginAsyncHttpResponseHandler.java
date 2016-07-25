package com.supperdrug.customerconsentform.httpclients.responsehandlers;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by adam on 18/07/2016.
 */
public class LoginAsyncHttpResponseHandler extends JsonHttpResponseHandler {

    @Override
    public void onSuccess(int statusCode, JSONArray response) {
        super.onSuccess(statusCode, response);
    }

    @Override
    public void onFailure(int statusCode, Throwable e, JSONObject errorResponse) {
        super.onFailure(statusCode, e, errorResponse);
    }
}
