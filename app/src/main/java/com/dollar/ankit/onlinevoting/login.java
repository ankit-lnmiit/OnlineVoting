package com.dollar.ankit.onlinevoting;

import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class login extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    TextView testDisplay;
    FirebaseFirestore db;
    Map<String, Object> user;
    CollectionReference cities;
    DocumentReference docRef;
    Query query;
    String temp, temp2;
//    private MongoClient mongoClient;
//    private MongoDatabase database;
//    private MongoCollection<Document> collection;
//    private FindIterable<Document> findIterable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        testDisplay = findViewById((R.id.sampleText));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        cities = db.collection("users");
        query = cities.whereEqualTo("email","gym.president@lnmiit.ac.in");

                query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("sample", document.get("name") + " => " + document.get("password"));
                                temp = document.get("name").toString();
                                testDisplay.setText(temp);
                                Log.d("me",temp);
                            }
                        } else {
                            Log.d("sample2", "Error getting documents: ", task.getException());
                        }
                    }
                });




//        mongoClient = MongoClients.create("mongodb://dollar:lnmiit123@ds121311.mlab.com:21311/onlinevoting");
//        database = mongoClient.getDatabase("onlinevoting");

        //android.util.Log.d("name",database.getName());
        // Set up the login form.
        mEmailView = (android.widget.AutoCompleteTextView) findViewById(R.id.email);
    }

    public void authentication(View view) {
        mEmailView = (android.widget.AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.pass);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        String temp = null;

        //collection = database.getCollection("users");
        //findIterable = collection.find(new Document());

        // Check for a valid email address.
        boolean chcklnmiitemail= email.endsWith("lnmiit.ac.in");

        if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
            focusView.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(email) || !chcklnmiitemail) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
            focusView.requestFocus();
            return;
        }


        //findIterable = collection.find(eq("email", email));
//        if(findIterable!=null) {
//            for (Document doc : findIterable) {
//                temp = doc.getString("password");
//                android.util.Log.d("aa",temp);
//            }
//            cancel=false;
//        }
//        else{
//            mEmailView.setError(getString(R.string.email_not_found));
//            focusView = mEmailView;
//            cancel = true;
//            focusView.requestFocus();
//            return;
//        }

        //check Password

        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            focusView.requestFocus();
            cancel = true;
            return;
        }
        else{
            if(!password.equals(temp)){
                mPasswordView.setError(getString(R.string.wrong_password));
                focusView = mPasswordView;
                cancel = true;
                focusView.requestFocus();
                return;
            }
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return;
        } else {
            Intent intent = new Intent(this, user.class);
            startActivity(intent);
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
        }




    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
