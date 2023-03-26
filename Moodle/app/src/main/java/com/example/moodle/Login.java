package com.example.moodle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodle.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Login extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText t1,t2;
    private TextView tv1;
    private Button b1;
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        t1=findViewById(R.id.Email);
        t2=findViewById(R.id.password);
        b1=findViewById(R.id.login);
        tv1=findViewById(R.id.redirect);
        binding.forgotTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,ForgotPasswordActivity.class));
                Toast.makeText(Login.this, "Forget Password", Toast.LENGTH_SHORT).show();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=t1.getText().toString();
                String password=t2.getText().toString();
                if(!user.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(user).matches()){
                    if(!password.isEmpty()){
                        auth.signInWithEmailAndPassword(user, password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                         if(user.equals("admin@nec.edu.in") && password.equals("Admin@123")){
                                             Toast.makeText(Login.this, "Admin User", Toast.LENGTH_SHORT).show();
                                             startActivity(new Intent(Login.this,admin.class));
                                         }
                                         else {
                                             Toast.makeText(Login.this,"Login Successfully",Toast.LENGTH_SHORT).show();
                                             Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                                              intent.putExtra("name", user);
                                              intent.putExtra("password",password);
                                                System.out.println(user);
                                                System.out.println(password);
                                               startActivity(intent);
//                                             startActivity(new Intent(Login.this, MainActivity.class));
                                             finish();
                                         }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Login.this,"Login Failed"+e.getMessage(),Toast.LENGTH_LONG).show();

                                    }
                                });
                    }
                    else{
                        t2.setError("Password can not be empty");
                    }
                    if(user.isEmpty()){
                        t1.setError("Username can not be empty");
                    }
                }

            }
        });
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                startActivity(new Intent(Login.this,Signup.class));
            }
        });
    }
}