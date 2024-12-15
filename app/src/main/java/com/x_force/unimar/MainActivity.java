package com.x_force.unimar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.x_force.unimar.login.LoginActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getFCMToken();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);


    }
    public void getFCMToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>(){

            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(MainActivity.this, Objects.requireNonNull(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
                    return;
                }
                String token = task.getResult();
                Log.d("TOKEN" ,token);
            }
        });
    }
}