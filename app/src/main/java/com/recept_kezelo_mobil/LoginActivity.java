package com.recept_kezelo_mobil;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null){
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            getMenuInflater().inflate(R.menu.signedout_menu, menu);
        }else {
            getMenuInflater().inflate(R.menu.signedin_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId==R.id.login){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else if (itemId == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else if(itemId == R.id.home){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (itemId == R.id.newrecipe) {
            startActivity(new Intent(this, NewRecipeActivity.class));

        } else if (itemId == R.id.ownrecipe) {
            startActivity(new Intent(this, OwnRecipeActivity.class));

        }
        return true;
    }

    public void login(View v){
        TextInputEditText emal =  findViewById(R.id.emailET);
        TextInputEditText passw = findViewById(R.id.passwordET);
        if(emal.getText()!=null&& passw.getText()!=null){
            mAuth.signInWithEmailAndPassword(emal.getText().toString(),
                                                    passw.getText().toString())
                    .addOnCompleteListener(this, task-> {

                            if(task.isSuccessful()){

                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            }else {
                                Log.e("Auth", "onComplete: ", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                    });


        }
    }
    public void moveToRegister(View v){
        startActivity(new Intent(this, RegisterActivity.class));
        finish();

    }
}