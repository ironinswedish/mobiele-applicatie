package com.clarysse.jarne.university_go;

import android.content.Context;
import android.content.Intent;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RegisterActivity extends AppCompatActivity {

    private Switch characterSwitch;
    private Switch raceSwitch;
    private ImageView characterImage;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        characterImage = findViewById(R.id.character);
        characterSwitch = findViewById(R.id.characterswitch);

        raceSwitch = findViewById(R.id.raceSwitch);

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



        final Intent intent = new Intent(this, MainMenuActivity.class);
        registerButton = findViewById(R.id.registerbutton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result =register();
                if(result==0){
                startActivity(intent);
                }
                else if(result ==-1){
                    CharSequence message = "Email adress already in use.";

                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    CharSequence message = "Something went wrong.";

                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private int register(){
        //register
        AsyncTask<String, Void, Integer> r = new RegisterTask().execute();

        try {
            return r.get();

        } catch (ExecutionException e) {
            return -1;
        } catch (InterruptedException e) {
            return -1;
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

    public class RegisterTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {

            TextView register_username_field = findViewById(R.id.register_username_field);
            String username = ""+ register_username_field.getText();

            TextView passwordfield = findViewById((R.id.register_password_field));
            String password = ""+ passwordfield.getText();
            byte[] salt = new byte[0];
            try {
                salt=getSalt();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }
            String hashedPassword = getSecurePassword(password,salt);
            String saltString = new String(salt);

            TextView emailfield = findViewById(R.id.register_email_field);
            String email = ""+ emailfield.getText();

            //Decide the sprite (kijk da rijmt)
            String sprite="male1";
            if(raceSwitch.isChecked()){
                if(characterSwitch.isChecked()){
                    sprite="female1";
                }
                else{
                    sprite="male1";
                }
            } else {
                if(characterSwitch.isChecked()){
                    sprite="female2";
                }
                else{
                    sprite="male2";
                }
            }



            Map<String, String> param = new HashMap<>();
            param.put("username", username);
            param.put("email",email);
            param.put("password", hashedPassword);
            param.put("sprite_type",sprite);
            param.put("cubecount","10");
            param.put("salt",""+saltString);

            JSONObject credObject = new JSONObject(param);
            try {
                URL url = new URL("http://10.0.2.2:5000/user");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");

                OutputStream os = conn.getOutputStream();
                os.write(credObject.toString().getBytes("UTF-8"));
                os.close();
                StringBuilder sb = new StringBuilder();
                int HttpResult = conn.getResponseCode();
                System.out.println("ANTWOORD "+HttpResult);
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    Log.e("registerTask", "response " + sb.toString());
                } else {
                    String message=conn.getResponseMessage();
                    Log.e("registerTask", "response " + message);
                    if(message.equals("CREATED")){
                        return 0;
                    }
                    else if(message.equals("CONFLICT")){
                        return -1;
                    }
                }
                return 0;
            } catch (Exception e) {
                Log.e("registerTask", "something went wrong", e);
                return 1;
            }
        }
    }

}
