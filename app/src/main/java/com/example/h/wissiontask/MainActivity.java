package com.example.h.wissiontask;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.h.wissiontask.Database.SqlController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    EditText email_val,password_val;
    private String emailId_txt, password_txt;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");

        new SqlController(this).deleteAllRecords("feeds");
        new SqlController(this).deleteAllRecords("videolikecounts");

        email_val = (EditText) findViewById(R.id.enter_email);
        password_val = (EditText) findViewById(R.id.enter_password);
        login = (Button) findViewById(R.id.signin_btn);
        email_val.setText("hardi@gmail.com");
        password_val.setText("123456");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFormLogin();
                if(email_val.getText().toString().equals("hardi@gmail.com")&& password_val.getText().toString().equals("123456")){
                    Toast.makeText(MainActivity.this,"Login Successfull.",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this,FeedData.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(MainActivity.this,"Invalid Login.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean validateFormLogin() {
        int errorColor = getResources().getColor(R.color.white);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);

        String email = email_val.getText().toString().trim();
        if (!isValidEmail(email)) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("Invalid Email Id.");
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, "Invalid Email Id.".length(), 0);
            email_val.setError(spannableStringBuilder);
            email_val.requestFocus();
        }

        final String password = password_val.getText().toString().trim();
        if (!isValidPassword(password)) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("Invalid Password.");
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, "Invalid Password.".length(), 0);
            password_val.setError(spannableStringBuilder);
            password_val.requestFocus();
        }

        if (isValidEmail(email) && isValidPassword(password)) {
            emailId_txt = email;
            password_txt = password;
            return true;
        } else
            return false;

    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 6) {
            return true;
        }
        return false;
    }
}
