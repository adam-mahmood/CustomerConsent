package com.supperdrug.customerconsentform.activities;

import android.view.View;

import com.loopj.android.http.RequestParams;

/**
 * Created by adammahmood on 22/07/2016.
 */
public interface WebService {

    public abstract void invokeWS(RequestParams params);
}
