package com.example.coltonjacobson.fblamobapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    /**
     * The Context.
     */
    Context context;
    /**
     * The Post url.
     */
    String postURL = "https://fblamobileapp.azurewebsites.net/user/login";

    private UserLoginTask mAuthTask = null;

    // UI references
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        //Hides the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        setTitle("Librarian");
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        //Hides the keyboard until the user is ready to use it
        InputMethodManager keyboardManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(

                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN

        );




    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    //Validates the basic features of the email/username entered
    private boolean isEmailValid(String email) {

        if (email.length()==7||email.contains("@gmail.com")) {
            return true;
        }

        return false;
    }

    private boolean isPasswordValid(String password) {
        if(password.length() > 6) {
            return true;
        }

        return false;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        /**
         * Instantiates a new User login task.
         *
         * @param email    the email
         * @param password the password
         */
        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            return authenticate(mEmail,mPassword);

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                finish();
                Intent login = new Intent(LoginActivity.this,mainActivity.class);
                startActivity(login);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }


    /**
     * Authenticate boolean.
     *
     * @param USERNAME the username
     * @param PASSWORD the password
     * @return the boolean
     */
//Sends a push request to the database to authenticate the username and password
    //Returns either a JSONObject with a token verifying the username and password, or returns an empty JSON Object
    public boolean authenticate(final String USERNAME, final String PASSWORD) {


        //Storing username and password in JSONObject
        final JSONObject info = new JSONObject();
        try {
            info.put("username", USERNAME);
            info.put("password", PASSWORD);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "JSON Parse error @ authenticate before request", Toast.LENGTH_SHORT).show();
        }


        RequestQueue rQueue = Volley.newRequestQueue(this);
        //Posting the object containing the username and password
        //If correct, a JWT token will be inserted into jResponse
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,postURL,info,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                        if(response != null) {
                            if (response.getString("token") != null) {
                                String token = response.getString("token");
                                if(token.indexOf(".") == 36) {
                                    writeTokenToFile(response.get("token").toString(), getApplicationContext());
                                    Toast.makeText(context, token, Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onResponse" + token);
                                }
                            }
                        }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "JSON ERROR", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error response", error.toString());
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            public Map<String,String> getParams()
            {
                Map<String,String> parameters = new HashMap<String,String>();
                parameters.put("Content-Type", "application/json");
                parameters.put("Accept", "application/json");
                return parameters;
            }

        };
        rQueue.add(postRequest);


        return readTokenFile(getApplicationContext()).indexOf(".") == 36;
    }

    @Override
    public void onBackPressed() {

    }

    private void writeTokenToFile(String token, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("userToken.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(token);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readTokenFile(Context context) {
        String token = "";

        try {
            InputStream inputStream = context.openFileInput("userToken.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                token = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return token;
    }


}

