package com.example.instaclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class registration extends AppCompatActivity {
    private EditText username;
    private EditText name;
    private EditText email;
    private EditText password;
    private Button register;
    private TextView login;
    private FirebaseAuth auth;
    private DatabaseReference mRootRef;
    ProgressDialog pd;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        login=findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(registration.this,"login page",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(registration.this,login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

            }
        });
        username=findViewById(R.id.username);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        register=findViewById(R.id.register);
        auth=FirebaseAuth.getInstance();
        mRootRef= FirebaseDatabase.getInstance().getReference();

        pd=new ProgressDialog(this);
        pd.setMessage("Please wait");


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();

                final String mail=email.getText().toString();
                String pass=password.getText().toString();
                final String txt_name=name.getText().toString();
                final String txt_username=username.getText().toString();

                if(TextUtils.isEmpty(mail)||TextUtils.isEmpty(pass)||TextUtils.isEmpty(txt_name)||TextUtils.isEmpty(txt_username))
                {
                    Toast.makeText(registration.this,"empty credentials",Toast.LENGTH_SHORT).show();
                }
                else if(pass.length()<6)
                {
                    Toast.makeText(registration.this,"password length must be atleast 6 digits",Toast.LENGTH_LONG).show();
                }
                else{
                    auth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(registration.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String ec="just trying";
                                mRootRef.child("invite").child("demo").setValue(txt_username);


                                Toast.makeText(registration.this,"pretask done",Toast.LENGTH_SHORT).show();

                                HashMap<String,Object>map=new HashMap<>();
                                map.put("id",auth.getCurrentUser().getUid());
                                map.put("name",txt_name);
                                map.put("username",txt_username);
                                map.put("email",mail);
                                map.put("bio","");
                                map.put("imageurl","default");






                                mRootRef.child("Users").child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(registration.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            pd.dismiss();
                                            Toast.makeText(registration.this,"registered successfully",Toast.LENGTH_LONG).show();

                                            //startActivity(new Intent(registration.this, login.class));
                                            Intent intent =new Intent(registration.this,login.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();




                                        }
                                    }
                                }).addOnFailureListener(registration.this, new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(registration.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }else{
                                pd.dismiss();
                                Toast.makeText(registration.this,"unsuccessful process",Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                }




            }
        });

    }
}