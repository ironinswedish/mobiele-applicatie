package com.clarysse.jarne.university_go;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private Switch characterSwitch;
    private Switch raceSwitch;
    private ImageView characterImage;
    private Button registerButton;

    private ApiCallsInterface apiCallsInterface;
    private Retrofit retrofit;
    String ip2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        characterImage = findViewById(R.id.character);
        characterSwitch = findViewById(R.id.characterswitch);

        raceSwitch = findViewById(R.id.raceSwitch);
        ip2 = getResources().getString(R.string.IPaddres);
        characterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(raceSwitch.isChecked()){
                        characterImage.setImageResource(R.drawable.female1);
                    }
                    else{
                        characterImage.setImageResource(R.drawable.female2);
                    }
                } else {
                    if(raceSwitch.isChecked()){
                        characterImage.setImageResource(R.drawable.male1);
                    }
                    else{
                        characterImage.setImageResource(R.drawable.male2);
                    }

                }
            }
        });

        raceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(characterSwitch.isChecked()){
                        characterImage.setImageResource(R.drawable.female1);
                    }
                    else{
                        characterImage.setImageResource(R.drawable.male1);
                    }
                } else {
                    if(characterSwitch.isChecked()){
                        characterImage.setImageResource(R.drawable.female2);

                    }
                    else{

                        characterImage.setImageResource(R.drawable.male2);
                    }

                }
            }
        });



        final Intent intent = new Intent(this, StarterActivity.class);
        registerButton = findViewById(R.id.registerbutton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result =register();
                if(result==0){

                }
                else if(result ==-1){
                    CharSequence message = "Email adress already in use.";

                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    System.out.println(result+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                    CharSequence message = "Something went wrong. This is updated";

                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }


    private void handleRegister(int result,String token){




        Intent intent = new Intent(this, StarterActivity.class);
        if(result==0){
            SharedPreferences sp;
            sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            sp.edit().putStringSet("token", Collections.singleton(token));
            startActivity(intent);
        }
        else if(result ==-1){
            CharSequence message = "Email adress already in use.";

            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            System.out.println(result+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            CharSequence message = "Something went wrong. This is updated";

            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private int register(){
        //register
        AsyncTask<String, Void, Integer> r = new RegisterTask2().execute();

        try {
            return r.get();

        } catch (ExecutionException e) {
            return -2;
        } catch (InterruptedException e) {
            return -2;
        }

    }

    private String getSecurePassword(String passwordToHash, byte[] salt)
    {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(salt);
            //Get the hash's bytes
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    //Add salt
    private byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
        //Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt;
    }


    public class RegisterTask2 extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {

            retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(ip2).build();
            apiCallsInterface = retrofit.create(ApiCallsInterface.class);

            TextView register_username_field = findViewById(R.id.register_username_field);
            String username = "" + register_username_field.getText();

            TextView passwordfield = findViewById((R.id.register_password_field));
            String password = "" + passwordfield.getText();
            byte[] salt = new byte[0];
            try {
                salt = getSalt();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }


            String saltString = new String(salt);
            String hashedPassword = getSecurePassword(password, saltString.getBytes());
            String hashedPassword3 = getSecurePassword(password, new String(saltString.getBytes()).getBytes());
            String hashedPassword2 = getSecurePassword(password, salt);

            System.out.println("VERGELIJKING: "+hashedPassword+" EN "+hashedPassword2+ " EN "+hashedPassword3);

            TextView emailfield = findViewById(R.id.register_email_field);
            String email = "" + emailfield.getText();

            //Decide the sprite (kijk da rijmt)
            String sprite = "male1";
            if (raceSwitch.isChecked()) {
                if (characterSwitch.isChecked()) {
                    sprite = "female1";
                } else {
                    sprite = "male1";
                }
            } else {
                if (characterSwitch.isChecked()) {
                    sprite = "female2";
                } else {
                    sprite = "male2";
                }
            }


            Map<String, String> param = new HashMap<>();
            param.put("username", username);
            param.put("email", email);
            param.put("password", hashedPassword);
            param.put("sprite_type", sprite);
            param.put("cubecount", "10");
            param.put("salt", "" + saltString);

            JSONObject credObject = new JSONObject(param);



            //Call<String> registerCall =apiCallsInterface.register(credObject);


            apiCallsInterface.register(credObject).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (response.isSuccessful()) {
                        String result;
                        result=response.body();
                        String[] results = result.split("-");
                        if(results[0].equals("Tis ok")){
                            System.out.println("Status is op nul gezet"+results[1]);
                            handleRegister(0,results[1]);
                        }
                        else{
                            handleRegister(-1,null);
                        }

                        Log.e("Register", "Registered! Here is the body: " + response.body());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    handleRegister(-2,null);
                    Log.e("Register", "Unable to get the data from python");
                }
            });

            return 0;


        }
    }
}
