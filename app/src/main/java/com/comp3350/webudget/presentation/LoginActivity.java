package com.comp3350.webudget.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comp3350.webudget.R;
import com.comp3350.webudget.application.Services;

import androidx.appcompat.app.AppCompatActivity;

import javax.security.auth.login.LoginException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText email_field,pwrd_field;
    private Button login_button,signup_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_field = (EditText)findViewById(R.id.username);
        pwrd_field = (EditText)findViewById(R.id.password_input);
        login_button = (Button)findViewById(R.id.login_button);
        signup_button = (Button)findViewById(R.id.signup_button);

        login_button.setOnClickListener(this);
        signup_button.setOnClickListener(this);
    }

    private void hideKeyboard(){
        View v = getCurrentFocus();
        if ( v != null ){
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    public String[] getInputValues(EditText email,EditText password){
        String[] values = new String[2];

        values[0] = email.getText().toString();
        values[1] = password.getText().toString();

        return values;
    }

    @Override
    public void onClick(View view) {

        switch ( view.getId() ){
            case R.id.login_button:
                hideKeyboard();
                String[] inputValues = getInputValues(this.email_field,this.pwrd_field);
                try{
                    //Login
                    Services.userLogic().login(inputValues);
                    startActivity(new Intent(this , MasterActivity.class));
                }catch(LoginException e){
                    Toast toast= Toast.makeText(getApplicationContext(),
                            "Username or Password is not correct", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            break;
            case R.id.signup_button:
                hideKeyboard();
                startActivity(new Intent(this , SignupActivity.class));
            break;
        }

    }

}