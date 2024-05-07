package com.recept_kezelo_mobil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recept_kezelo_mobil.models.Name;
import com.recept_kezelo_mobil.models.User;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore mFFst;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mFFst = FirebaseFirestore.getInstance();
        if (mAuth.getCurrentUser()!=null){
            finish();
        }
    }
    public  void register(View v){
        Editable email = ((TextInputEditText) findViewById(R.id.emailET)).getText();
        Editable passw1 = ((TextInputEditText) findViewById(R.id.passwordET)).getText();
        Editable passw2 = ((TextInputEditText) findViewById(R.id.passwordAgainEt)).getText();
        Editable firstname = ((TextInputEditText) findViewById(R.id.firstnameET)).getText();
        Editable lastname  = ((TextInputEditText) findViewById(R.id.lastnameET)).getText();
        if( email     != null &&
            passw1    != null &&
            passw2    != null &&
            firstname != null &&
            lastname  != null &&
                passw1.toString().equals(passw2.toString())){
            mAuth.createUserWithEmailAndPassword(email.toString(), passw1.toString())
                    .addOnCompleteListener(this, task-> {
                        if(task.isSuccessful()){
                            User model = new User();
                            model.setName(new Name(firstname.toString(), lastname.toString()));
                            model.setEmail(email.toString());
                            model.setId(task.getResult().getUser().getUid());

                            mFFst.collection("Users").document(model.getId()).set(model)
                                    .addOnCompleteListener(this, task1 -> {
                                        if(task1.isSuccessful()){

                                            startActivity(new Intent(this, MainActivity.class));
                                            finish();
                                        }else{
                                            Log.e("Create user", "onComplete: ", task1.getException());
                                            Toast.makeText(RegisterActivity.this, "Creation failed",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    });
                        }else {
                            Log.e("Auth", "onComplete: ", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    });


        }


    }
    public void moveToLogin(View V){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
