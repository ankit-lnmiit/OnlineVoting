package com.dollar.ankit.onlinevoting;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
//import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
//import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

//import java.util.Map;

public class login extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    //private View mProgressView;
    //private View mLoginFormView;
    TextView testDisplay;
    FirebaseFirestore db;
    //Map<String, Object> user;
    CollectionReference cities;
    //DocumentReference docRef;
    Query query;
    //public static boolean loginStatus1;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";
    public static final String loginStatus = "loginKey";
    //SharedPreferences sharedpreferences;
    static String name;
    static String email;
    //String email2;
    static String type = "default";
    //static boolean cancel = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        if (android.os.Build.VERSION.SDK_INT > 21) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }


        mEmailView = findViewById(R.id.email);

    }

    public void authentication(View view) {

        mPasswordView = findViewById(R.id.pass);
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        View focusView;

        // Check for a valid email address.
        boolean checklnmiitemail = email.endsWith("lnmiit.ac.in");

        if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            //cancel = true;
            focusView.requestFocus();
        } else if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.empty));
            focusView = mEmailView;
            //cancel = true;
            focusView.requestFocus();
        } else if (!checklnmiitemail) {
            mEmailView.setError(getString(R.string.invalid_lnmiit_email));
            focusView = mEmailView;
            //cancel = true;
            focusView.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.empty));
            focusView = mPasswordView;
            focusView.requestFocus();
            //cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            //cancel = true;
            focusView.requestFocus();
        } else {
            cities = db.collection("users");
            query = cities.whereEqualTo("email", email).whereEqualTo("password", password);
            query.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                String temp, temp2;
                                SharedPreferences sharedpreferences;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document != null) {
                                        temp = document.getString("name");
                                        temp2 = document.getString("type");

                                        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(Name, temp);
                                        editor.putString(loginStatus, "true");
                                        editor.putString(Email, email);
                                        editor.apply();

                                        if (temp2.equals("voter")) {
                                            Intent intent;
                                            intent = new Intent(login.this, user.class);
                                            startActivity(intent);
                                        } else if (temp2.equals("admin")) {
                                            Intent intent;
                                            intent = new Intent(login.this, admin.class);
                                            startActivity(intent);
                                        }
                                        else{
                                            Toast.makeText(login.this,"ERROR",Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                            Toast.makeText(login.this,"User Not Registered or wrong password",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                //boolean cancel2;
                                Toast.makeText(login.this,"ERROR",Toast.LENGTH_SHORT).show();

                                //Log.d("sample2", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    private boolean isEmailValid(String email) {

        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 4;
    }
}



