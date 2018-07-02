package com.dollar.ankit.onlinevoting;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

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
    public static boolean loginStatus1;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";
    public static final String loginStatus = "loginKey";
    SharedPreferences sharedpreferences;

    //String temp, temp2;
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

        mEmailView = (android.widget.AutoCompleteTextView) findViewById(R.id.email);

    }

    public void authentication(View view) {
        mEmailView = (android.widget.AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.pass);
        db = FirebaseFirestore.getInstance();

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        final boolean[] cancel = new boolean[2];
        View focusView;
        final String[] temp = new String[2];

        // Check for a valid email address.
        boolean checklnmiitemail= email.endsWith("lnmiit.ac.in");

        if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            //cancel = true;
            focusView.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.empty));
            focusView = mEmailView;
            //cancel = true;
            focusView.requestFocus();
            return;
        }
        else if(!checklnmiitemail){
            mEmailView.setError(getString(R.string.invalid_lnmiit_email));
            focusView = mEmailView;
            //cancel = true;
            focusView.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.empty));
            focusView = mPasswordView;
            focusView.requestFocus();
            //cancel = true;
            return;
        }
        else if(!isPasswordValid(password)){
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                //cancel = true;
                focusView.requestFocus();
                return;
        }
        else{
            cities = db.collection("users");
            query = cities.whereEqualTo("email",email).whereEqualTo("password",password);

            query.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    temp[0] = document.get("name").toString();
                                    temp[1] = document.get("type").toString();
                                    cancel[0] = false;
                                }
                            } else {
                                Toast.makeText(login.this, "ERROR" + R.string.email_not_found,
                                        Toast.LENGTH_SHORT).show();
                                cancel[0] = true;
                                Log.d("sample2", "Error getting documents: ", task.getException());
                            }
                        }
                    });

            if(cancel[0]){
                return;
            }
            else{
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Name, temp[0]);
                editor.putString(loginStatus, "true");
                editor.putString(Email, email);
                editor.commit();

                if(temp[1].equals("voter")) {
                    Intent intent = new Intent(this, user.class);
                    loginStatus1=true;
                    startActivity(intent);
                }
                else if(temp[1].equals("admin")) {
                    Intent intent = new Intent(this, admin.class);
                    loginStatus1=true;
                    startActivity(intent);
                }

            }
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
