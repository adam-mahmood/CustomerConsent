package com.supperdrug.customerconsentform.listeners;

import android.text.Editable;
import android.text.TextWatcher;

import com.supperdrug.customerconsentform.models.Customer;
import com.supperdrug.customerconsentform.models.Function;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Waseem on 28/08/2016.
 */
public class EditTextWatcher implements TextWatcher {
    private String name;

    private String oldValue;

    public final static Map<String,String> changedFields = new HashMap<>();

    private static Customer customer;

    private Function<String, Void> function;

    public EditTextWatcher(String name, String oldValue, Function<String, Void> function) {
        this.name = name;
        this.oldValue = oldValue;
        this.function = function;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!oldValue.trim().equalsIgnoreCase(s.toString())){
            changedFields.put(name,s.toString());
        }else{
            changedFields.remove(name);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public static Customer getCustomer() {
        return customer;
    }

    public static void setCustomer(Customer customer) {
        EditTextWatcher.customer = customer;
    }

    public Function<String, Void> getFunction() {
        return function;
    }

    public void setFunction(Function<String, Void> function) {
        this.function = function;
    }

}
