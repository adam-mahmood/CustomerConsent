package com.supperdrug.customerconsentform.dialogs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.supperdrug.customerconsentform.R;
import com.supperdrug.customerconsentform.activities.WebService;
import com.supperdrug.customerconsentform.httpclients.CustomerConsentFormRestClient;
import com.supperdrug.customerconsentform.listeners.EditTextWatcher;
import com.supperdrug.customerconsentform.models.Function;
import com.supperdrug.customerconsentform.models.Staff;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Waseem on 29/08/2016.
 */
public class EditStaffDialog implements WebService {
    public static final String FORENAME = "forename";
    public static final String SURNAME = "surname";
    public static final String EMAIL_ADDRESS = "email_address";
    public static final String CONTACT_NUMBER = "contact_number";
    public static final String DATE_OF_BIRTH = "date_of_birth";
    public static final String ADDRESS = "address";
    public static final String CITY = "city";
    public static final String POST_CODE = "post_code";
    public static final String MALE = "Male";
    public static final String FEMALE = "Female";
    private AlertDialog dialog;
    private final Context context;
    private final Staff staff;

    private RadioButton gender;
    private RadioGroup genderGrp;
    private ProgressBar showProgress;
    private View  editView;

    private RequestParams params = new RequestParams() ;
    public EditTextWatcher watcher ;
    private Map<String,EditTextWatcher> textWatchers = new HashMap<>();

    public EditStaffDialog(Staff staff, View v, Context context) {
        this.context = context;
        this.staff = staff;
        createDialog();
    }

    private void createDialog() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
        View view = inflater.inflate(R.layout.edit_staff, null);

        AlertDialog.Builder adBuilder = new AlertDialog.Builder(context);
        adBuilder.setTitle("Edit Staff Dialog");
        adBuilder.setView(view);

        dialog = adBuilder.create();
        dialog.show();
        findViewById(dialog, staff);
    }
    private void findViewById(final AlertDialog dialog, final Staff staff) {
        editView = (View) dialog.findViewById(R.id.edit_staff_view);

        showProgress = (ProgressBar)dialog.findViewById(R.id.edit_staff_progressBar);

        params.add("staff_id", String.valueOf(staff.getId()));

        final EditText forename = (EditText)dialog.findViewById(R.id.edit_staff_forename);
        forename.setText(staff.getForename());
        watcher = new EditTextWatcher(FORENAME, forename.getText().toString(), new Function<String, Void>() {
            @Override
            public void apply(String input) {

                staff.setForename(input);
            }
        });
        textWatchers.put(FORENAME,watcher);
        forename.addTextChangedListener(watcher);

        EditText surnname = (EditText)dialog.findViewById(R.id.edit_staff_forename);
        surnname.setText(staff.getSurname());
        watcher = new EditTextWatcher(SURNAME, surnname.getText().toString(), new Function<String, Void>() {
            @Override
            public void apply(String input) {
                staff.setSurname(input);
            }
        });
        textWatchers.put(SURNAME,watcher);
        surnname.addTextChangedListener(watcher);

        EditText email = (EditText)dialog.findViewById(R.id.edit_staff_email);
        email.setText(staff.getEmailAddress());
        watcher = new EditTextWatcher(EMAIL_ADDRESS, email.getText().toString(), new Function<String, Void>() {
            @Override
            public void apply(String input) {
                staff.setEmailAddress(input);
            }
        });
        textWatchers.put(EMAIL_ADDRESS,watcher);
        email.addTextChangedListener(watcher);

        EditText contact = (EditText)dialog.findViewById(R.id.edit_staff_phone_number);
        contact.setText(staff.getPhoneNumber());
        watcher = new EditTextWatcher(CONTACT_NUMBER, contact.getText().toString(), new Function<String, Void>() {
            @Override
            public void apply(String input) {
                staff.setPhoneNumber(input);
            }
        });
        textWatchers.put(CONTACT_NUMBER,watcher);
        contact.addTextChangedListener(watcher);

        EditText dob = (EditText)dialog.findViewById(R.id.edit_staff_dob);
        dob.setText(staff.getDob());
        this.watcher = new EditTextWatcher(DATE_OF_BIRTH, dob.getText().toString(), new Function<String, Void>() {
            @Override
            public void apply(String input) {
                staff.setDob(input);
            }
        });
        textWatchers.put(DATE_OF_BIRTH,watcher);
        dob.addTextChangedListener(this.watcher);

        EditText address = (EditText)dialog.findViewById(R.id.edit_staff_address);
        address.setText(staff.getAddress());
        watcher = new EditTextWatcher(ADDRESS, address.getText().toString(), new Function<String, Void>() {
            @Override
            public void apply(String input) {
                staff.setAddress(input);
            }
        });
        textWatchers.put(ADDRESS,watcher);
        address.addTextChangedListener(watcher);

        EditText city = (EditText)dialog.findViewById(R.id.edit_staff_city);
        city.setText(staff.getCity());
        this.watcher = new EditTextWatcher(CITY, city.getText().toString(), new Function<String, Void>() {
            @Override
            public void apply(String input) {
                staff.setCity(input);
            }
        });
        textWatchers.put(CITY,watcher);
        city.addTextChangedListener(this.watcher);

        EditText post = (EditText)dialog.findViewById(R.id.edit_staff_post_code);
        post.setText(staff.getPostCode());
        this.watcher = new EditTextWatcher(POST_CODE, post.getText().toString(), new Function<String, Void>() {
            @Override
            public void apply(String input) {
                staff.setPostCode(input);
            }
        });
        textWatchers.put(POST_CODE,watcher);
        post.addTextChangedListener(this.watcher);


        genderGrp = (RadioGroup) dialog.findViewById(R.id.edit_staff_radioGrp);
        if (staff.getGender().trim().equalsIgnoreCase(MALE))
        {
            gender = (RadioButton) dialog.findViewById(R.id.edit_staff_radioM);
            gender.setText(MALE);
            gender.setSelected(true);
        }
        else
        {
            gender = (RadioButton) dialog.findViewById(R.id.edit_staff_radioF);
            gender.setText(FEMALE);
            gender.setSelected(true);
        }
        // need a radio group to make listener action

        genderGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(id);
                if (!staff.getGender().trim().equalsIgnoreCase(checkedRadioButton.getText().toString().trim())){
                    EditTextWatcher.changedFields.put("sex",checkedRadioButton.getText().toString().trim());
                }else{
                    EditTextWatcher.changedFields.remove("sex");
                }
            }
        });


        Button save = (Button) dialog.findViewById(R.id.edit_staff_button_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(EditTextWatcher.changedFields);
                if (EditTextWatcher.changedFields.isEmpty()){
                    Toast.makeText(context.getApplicationContext(), "No fields have changed!", Toast.LENGTH_LONG).show();
                    return;
                }
                for(Map.Entry<String, String> studentEntry : EditTextWatcher.changedFields.entrySet()){
                    params.add(studentEntry.getKey(),studentEntry.getValue());
                }
                invokeWS(params);
            }
        });
        Button cancel = (Button) dialog.findViewById(R.id.edit_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    @Override
    public void invokeWS(final RequestParams params) {
        // Show Progress Dialog
        showProgress(true);


        AsyncHttpResponseHandler responsehandler = new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                showProgress(false);
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if(obj.getInt("status") == 200){
                        Toast.makeText(context.getApplicationContext(), "Customer Edit Succesull!", Toast.LENGTH_LONG).show();
                        JSONArray CustomerJson = obj.getJSONArray("result");
                        System.out.println(CustomerJson);
                        updateModel();
                        System.out.println("After1:" + staff);
                        dialog.dismiss();

                    }
                    // Else display error message
                    else{
                        Toast.makeText(context.getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context.getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {

                System.out.println(statusCode);
                // Hide Progress Dialog
                showProgress(false);
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(context.getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(context.getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(context.getApplicationContext(), "Unexpected Error occured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        };
        CustomerConsentFormRestClient.post("superdrug/editcustomer",params ,responsehandler);

    }

    private void updateModel() {
        String key;
        for(Map.Entry<String, String> entry:EditTextWatcher.changedFields.entrySet()){
            key = entry.getKey();
            textWatchers.get(key).getFunction().apply(entry.getValue());
        }
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

            editView.setVisibility(show ? View.GONE : View.VISIBLE);
            editView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    editView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            showProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            showProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    showProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            showProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            editView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
