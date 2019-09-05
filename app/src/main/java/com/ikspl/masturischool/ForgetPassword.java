package com.ikspl.masturischool;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ForgetPassword extends AppCompatActivity {

    EditText E1,E2;
    String login_id=null,paswrd=null;
    UserSessionManagerLogin session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        session = new UserSessionManagerLogin(getApplicationContext());
        //stopService(new Intent(getBaseContext(), Service1.class));
        E1=(EditText)findViewById(R.id.editText1);
        E2=(EditText)findViewById(R.id.editText2);

        String username = getString(R.string.username);
        String pwd = getString(R.string.password);
        String db = getString(R.string.db);
        String data_source=getString(R.string.data_source);
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
    public void Login(View v)
    {

        if(E1.getText().toString().equalsIgnoreCase(""))
        {
            E1.setHint("Please Enter User-ID");
            E1.setHintTextColor(Color.RED);
            Toast.makeText(getApplicationContext(), "Please Enter User-ID", Toast.LENGTH_LONG).show();
        }
        else if(E2.getText().toString().equalsIgnoreCase(""))
        {
            E2.setHint("Please Enter Phone Number");
            E2.setHintTextColor(Color.RED);
            Toast.makeText(getApplicationContext(), "Please Enter Phone Number", Toast.LENGTH_LONG).show();
        }
        else
        {
            String username = getString(R.string.username);
            String pwd = getString(R.string.password);
            String db = getString(R.string.db);
            String data_source=getString(R.string.data_source);

            Statement st;
            Connection connect = ConnectionHelper(username, pwd, db, data_source);

            try {
                st = connect.createStatement();
                String Sql = "SELECT first_name,userid,password "+
                        " FROM dbo.Course_Student_Info "+
                        " WHERE (userid = N'"+E1.getText().toString()+"') AND (presStudentphone = N'"+E2.getText().toString()+"') OR (permStudentPhone = N'"+E2.getText().toString()+"')";
                ResultSet rs = st.executeQuery(Sql);

                if(rs.next())
                {
                    String password=rs.getString("password");
                    String userid = rs.getString("userid");



                    String msg = "You can request for password. Your Userid is :- "+userid.toString()+"\n Your Password is "+password.toString();
                    sendSMS(E2.getText().toString(),msg);
                    E1.setText("");
                    E2.setText("");

                }
                else
                {
                    Toast.makeText(ForgetPassword.this,"You Entered Wrong Credentials. !!!",Toast.LENGTH_LONG).show();
                    E1.setText("");
                    E2.setText("");
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    public void sendSMS(String phoneNo, String msg) {
        Log.e("MyTag","phone num = "+phoneNo);
        Log.e("MyTag","msg is = "+msg);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(msg);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}

