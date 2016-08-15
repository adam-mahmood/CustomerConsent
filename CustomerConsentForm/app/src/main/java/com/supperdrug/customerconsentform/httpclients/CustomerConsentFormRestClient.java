package com.supperdrug.customerconsentform.httpclients;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by adam on 18/07/2016.
 */
public class CustomerConsentFormRestClient {
    private static final String BASE_URL = "http://86.27.141.14:5000/api/v1.0/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public  static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.get(getAbsoluteUrl(url),params,responseHandler);
    }

    public  static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.post(getAbsoluteUrl(url),params,responseHandler);
    }
    public static void Wait(long milliseconds) throws InterruptedException {
        client.wait(milliseconds);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
