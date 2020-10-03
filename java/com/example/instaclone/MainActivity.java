package com.example.instaclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private ImageView iconimage;
    private LinearLayout linearlayout;
    private Button signin;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iconimage=findViewById(R.id.icon_image);
        linearlayout=findViewById(R.id.linearlayout);
        signin=findViewById(R.id.login);
        register=findViewById(R.id.register);

        linearlayout.animate().alpha(0f).setDuration(10);
        TranslateAnimation animation=new TranslateAnimation(0,0,0,-1500);
        animation.setDuration(1000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MyAnimationListener());
        iconimage.setAnimation(animation);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,login.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,registration.class));
            }
        });

    }
    private class MyAnimationListener implements Animation.AnimationListener{
        @Override
        public void onAnimationEnd(Animation animation) {
            iconimage.clearAnimation();
            iconimage.setVisibility(View.INVISIBLE);
            linearlayout.animate().alpha(1f).setDuration(1000);


        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            startActivity(new Intent(MainActivity.this,login.class));
            finish();
        }
    }
}