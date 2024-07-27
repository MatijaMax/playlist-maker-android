package com.example.myapplication.fragments;

import static android.content.ContentValues.TAG;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.myapplication.AuthenticationActivity;
import com.example.myapplication.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.myapplication.databinding.FragmentLoginBinding;

import com.example.myapplication.R;
import com.example.myapplication.sqlite.helper.DatabaseHelper;
import com.example.myapplication.sqlite.model.User;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    private EditText email;
    private EditText password;
    DatabaseHelper DB;

    public LoginFragment() { }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DB = new DatabaseHelper(getContext());
        DB.populateInitialData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        email = binding.email;
        password = binding.password;


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            if(!inputIsValid()){
                return;
            }

            signIn();


        });
    }

    private boolean inputIsValid() {
        boolean valid = true;
        if(email.getText().toString().isEmpty()){
            email.setError("Email is required");
            valid = false;
        }
        if(password.getText().toString().isEmpty()){
            password.setError("Password is required");
            valid = false;
        }
        return valid;
    }


    private void signIn() {
        //Log.d(TAG, "signIn:" + email.getText().toString());
        //dodati database login
        String user = email.getText().toString();
        String pass = password.getText().toString();

        if(user.equals("")||pass.equals(""))
            Toast.makeText(getContext(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
        else{
            Boolean checkuserpass = DB.checkUsernamePassword(user, pass);
            if(checkuserpass==true){
                //Log.d(TAG, "TRUE:" + email.getText().toString());
                Toast.makeText(getContext(), "Sign in successfull", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyAppPreferences", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", user);
                editor.apply();
                Intent intent  = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }else{
                //Log.d(TAG, "FALSE:" + email.getText().toString());
                User newUser = new User(user, pass);
                if(DB.createUser(newUser) != -1){
                    Toast.makeText(getContext(), "Registration successfull", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyAppPreferences", getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", user);
                    editor.apply();
                    Intent intent  = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }else{
                    //Log.d(TAG, "FALSE:" + email.getText().toString());
                    Toast.makeText(getContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }






}
