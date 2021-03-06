package com.clarysse.jarne.university_go;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class LoginActivity extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private TextView signinlabel;
    private static int RC_SIGN_IN = 11;
    private Button logoutButton;
    private Button loginButton;
    private Button registerButton;
    private String email;

    private ApiCallsInterface apiCallsInterface;
    private Retrofit retrofit;
    String ip2;
    private WifiBroadCastReceiver broadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ip2 = getResources().getString(R.string.IPaddres);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.silentSignIn().addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
            @Override
            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                handleSignInResult(task);
            }
        });

        broadCastReceiver = new WifiBroadCastReceiver();

        this.registerReceiver(broadCastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        /*signinlabel = findViewById(R.id.loginlabel);
        logoutButton = findViewById(R.id.logoutbutton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    // ...
                    case R.id.logoutbutton:
                        signOut();
                        break;
                    // ...
                }
            }
        });*/
        loginButton = findViewById(R.id.loginbutton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });
        final Intent intent = new Intent(this, RegisterActivity.class);
        registerButton = findViewById(R.id.toregisterbutton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });
        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("1", R.drawable.unimon_1);
        editor.putInt("2", R.drawable.unimon_2);
        editor.putInt("3", R.drawable.unimon_3);
        editor.putInt("4", R.drawable.unimon_4);
        editor.putInt("5", R.drawable.unimon_5);
        editor.commit();

    }

    private void login(){
        //inloggen
        int result=410;
        AsyncTask<String, Void, Integer> r =new LoginTask().execute();
        try {
            result= r.get();
            System.out.println(result+" DIT IS HET RESULT");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(result==200){
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        }
        else if(result == 409){
            CharSequence message = "Wrong password or email adres";

            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            CharSequence message = "Something went wrong. Error code: "+result;

            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            //signinlabel.setText(account.getDisplayName());
        } else {
            //signinlabel.setText("no user logged in");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            System.out.println("\na\na\na\na\na\na\na\na\na\na\na\na\na\na");
            String idToken = account.getIdToken();
            email = account.getEmail();
            Log.e("token", idToken);
            new TaskGoogleLogin().execute(idToken);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("sign-in", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    public class TaskGoogleLogin extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("logintask", "processing login");
        }

        @Override
        protected Integer doInBackground(String... strings) {
            Map<String, String> param = new HashMap<>();
            param.put("token", strings[0]);

            JSONObject tokenObje = new JSONObject(param);
            try {
                URL url = new URL(ip2+"google_login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");

                OutputStream os = conn.getOutputStream();
                os.write(tokenObje.toString().getBytes("UTF-8"));
                os.close();
                StringBuilder sb = new StringBuilder();
                int HttpResult = conn.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    Log.e("loginTask", "response " + sb.toString());

                    if(sb.toString().substring(0,6).equals("200 OK")){
                        Log.e("loginTask", "response )))))))))))))))))))))))))))))))))))))" + conn.getResponseMessage());
                        String[] string = sb.toString().substring(6, sb.length()).split("-");
                        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("userid", Integer.parseInt(string[0]));
                        editor.putString("sprite", string[1]);
                        editor.commit();

                        Log.e("hai", "correct");
                        return 3;

                    }
                    else if(sb.toString().equals("404 NO USER")){
                        Log.e("loginTask", "response ))))))))))))))))))))))))))))))))))))))))" + conn.getResponseMessage());


                        return 2;
                    }
                    else{
                        Log.e("loginTask", "response ))))))))))))))))))))))))))))))))))))))))))" + conn.getResponseMessage());
                    }
                } else {
                    Log.e("loginTask", "response )))))))))))))))))))))))))))))))))))))))))))))))" + conn.getResponseMessage());
                    System.out.println(conn.getResponseCode());

                }
                return 0;
            } catch (Exception e) {
                Log.e("loginTask", "something went wrong", e);
                return 1;
            }

        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 0) {
                Log.e("loginTask", "succes");
            } else if(result == 1) {
                Log.e("loginTask", "failure");
            } else if (result == 2) {
                Intent intent = new Intent(getApplicationContext(), RegisterGoogleActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
            } else if (result == 3) {
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
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

    public class LoginTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {

            TextView userfield = findViewById(R.id.username_field);
            String username = ""+ userfield.getText();

            byte[] salt =getSaltFromServer(username);
            System.out.println(new String(salt));

            TextView passwordfield = findViewById((R.id.password_field));
            String password = ""+ passwordfield.getText();
            String hashedPassword = getSecurePassword(password,salt);

            System.out.println(hashedPassword+ " is het hashed password");

            Map<String, String> param = new HashMap<>();
            param.put("email", username);
            param.put("password", hashedPassword);

            JSONObject credObject = new JSONObject(param);
            try {
                URL url = new URL(ip2+"login");
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
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    Log.e("loginTask", "response1 " + sb.toString());
                    Gson gson = new Gson();
                    JSONObject object = new JSONObject(sb.toString());
                    Log.e("json",""+ object.getInt("userid"));
                    SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("userid", object.getInt("userid"));
                    editor.putString("sprite", object.getString("sprite"));
                    editor.commit();
                    return 200;
                } else {
                    Log.e("loginTask", "response2 " + conn.getResponseMessage());
                    return conn.getResponseCode();
                }

            } catch (Exception e) {
                Log.e("loginTask", "something went wrong", e);
                return 410;
            }
        }
    }

    private byte[] getSaltFromServer(String username) {

        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(ip2).build();
        apiCallsInterface = retrofit.create(ApiCallsInterface.class);

        Map<String, String> param = new HashMap<>();
        param.put("email", username);

        JSONObject credObject = new JSONObject(param);

        try {
            URL url = new URL(ip2+"getsalt");
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
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                Log.e("loginTask", "response " + sb.toString());
                System.out.println(sb.toString()+ " DIT IS DEN SB JA WAT");
                String s =sb.toString();
                return s.getBytes();

            } else {
                String s = conn.getResponseMessage();
                Log.e("saltTask", "iepresponse " + s);
                return s.getBytes();
            }
        } catch (Exception e) {
            Log.e("loginTask", "something went wrong", e);
            return new byte[0];
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(broadCastReceiver);
    }
    @Override
    protected void onResume() {

        super.onResume();
        this.registerReceiver(broadCastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onBackPressed() {
        finish();

    }
}
