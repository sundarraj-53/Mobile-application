package com.example.moodle;

import static android.app.ProgressDialog.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {
      private FirebaseAuth auth;
      private EditText t1,t2,t3,t4;
      private TextView tv1;
      private Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth=FirebaseAuth.getInstance();
        t1=findViewById(R.id.Email);
        t2=findViewById(R.id.regno);
        t3=findViewById(R.id.password);
        t4=findViewById(R.id.confirm_password);
        b1=findViewById(R.id.signup);
        tv1=findViewById(R.id.new1);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=t1.getText().toString().trim();
                String regno= String.valueOf(Integer.parseInt(String.valueOf(t2.getText())));
                String pass=t3.getText().toString().trim();
                String cpass=t4.getText().toString().trim();

                if(username.isEmpty()){
                    t1.setError("Email can not be empty");
                }
                if(regno.isEmpty()){
                    t2.setError("Regno can not be empty");
                }
                if(pass.isEmpty()){
                    t3.setError("Password is can not be Empty");
                }
                if(cpass.isEmpty()){
                    t4.setError("Confirm password can not be empty");
                    if (!cpass.equals(pass))
                    {
                        t4.setError("Password does not match");
                    }
                }
                else{
                    auth.createUserWithEmailAndPassword(username, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           if(task.isSuccessful()){
                               Toast.makeText(Signup.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(Signup.this, Login.class));

                           }
                           else{
                               Toast.makeText(Signup.this, "Sign up Failed" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                           }

                        }
                    });
                }
            }
        });
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, Login.class));
            }
        });
    }
}