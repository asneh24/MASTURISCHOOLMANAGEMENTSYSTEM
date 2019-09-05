package com.ikspl.masturischool;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SplashScreen extends AppCompatActivity {

    String mPhoneNumber;
    private static final int REQUEST = 112;
    String stdid,notid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startService(new Intent(getBaseContext(),Service1.class));//Service Start
        //stopService(new Intent(getBaseContext(), Service1.class));

        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(SplashScreen.this,R.color.colorPrimary));
        }

        Intent ii = getIntent();
        stdid = ii.getStringExtra("StuID");
        notid = ii.getStringExtra("NotID");
        Log.e("MyTag","kkkstdid = "+stdid);



        ConnectivityManager cn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();
        if(nf != null && nf.isConnected()==true )
        {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent ii = new Intent(SplashScreen.this,Dashboard.class);
                    ii.putExtra("StuID", stdid);
                    ii.putExtra("NotID", notid);
                    startActivity(ii);
                    finish();


                }
            },2000);

        }
        else
        {
            new AlertDialog.Builder(SplashScreen.this)
                    .setTitle("Not Connected")
                    .setMessage("Network not avilable")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @SuppressLint("InlinedApi")
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            finish();
                            dialog.cancel();

                        }
                    })
                    .show();
        }



    }
    @SuppressLint("NewApi")
    private Connection ConnectionHelper(String user, String password,
                                        String database, String server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + server + ";"
                    + "databaseName=" + database + ";user=" + user
                    + ";password=" + password + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException se) {

        } catch (ClassNotFoundException e) {

        } catch (Exception e) {

        }
        return connection;
    }

    public void inserted(View v)
    {
        String username = getString(R.string.username);
        String pwd = getString(R.string.password);
        String db = getString(R.string.db);
        String data_source=getString(R.string.data_source);

        Statement st;
        Connection connect = ConnectionHelper(username, pwd, db, data_source);

        try {
            st = connect.createStatement();
            String Sql = "";//"select * from AppInstalled where phonenum= '"+abcd+"'";
            ResultSet rs = st.executeQuery(Sql);
            while (rs.next())
            {


            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Do here
                } else {
                    Toast.makeText(SplashScreen.this, "The app was not allowed to write to your storage.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}

