package com.ikspl.masturischool;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText E1,E2;
    Spinner spinner2;
    String login_id=null,paswrd=null;
    UserSessionManagerLogin session;
    String fcmtokenid,sTUid;
    String username,pwd,db,data_source;
    Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new UserSessionManagerLogin(getApplicationContext());
        username = getString(R.string.username);
        pwd = getString(R.string.password);
        db = getString(R.string.db);
        data_source=getString(R.string.data_source);
        //stopService(new Intent(getBaseContext(), Service1.class));
        E1=(EditText)findViewById(R.id.editText1);
        E2=(EditText)findViewById(R.id.editText2);
        spinner2 = (Spinner) findViewById(R.id.Spin1);
        addItemsOnSpinner2();

        FirebaseApp.initializeApp(this);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.e("MyTag", "getInstanceId failed", task.getException());
                    return;
                }

                // Get new Instance ID token
                fcmtokenid = task.getResult().getToken();

                // Log and toast
                //String msg = getString(R.string.msg_token_fmt, token);
                Log.e("MyTag", "kkkfcmtokenidd= "+fcmtokenid);
            }
        });



    }
    // add items into spinner dynamically
    public void addItemsOnSpinner2() {


        List<String> list = new ArrayList<>();
        //  list.add("list 1");
        // list.add("list 2");
        // list.add("list 3");

        list =getConnection();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }

    public List getConnection()
    {
        List<String> list = new ArrayList<String>();

        String username = getString(R.string.username);
        String pwd = getString(R.string.password);
        String db = getString(R.string.db);
        String data_source=getString(R.string.data_source);

        Statement st;
        Connection connect = ConnectionHelper(username, pwd, db, data_source);

        try {
            st = connect.createStatement();
            String Sql = "SELECT sessionyr_id, session_desc" +
                    " FROM    session_mst where active='"+"Active"+"' order by sessionyr_id DESC";
            ResultSet rs = st.executeQuery(Sql);
            while (rs.next())
            {
                String Cat_ID=rs.getString("session_desc");
                list.add(Cat_ID);

            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;

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
    public void Home(View v) {
        Intent intent=new Intent(getApplicationContext(), Dashboard.class);
        startActivity(intent);
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
            E2.setHint("Please Enter Password");
            E2.setHintTextColor(Color.RED);
            Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_LONG).show();
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
                String Sql = "SELECT     AdmissionNo, userid, password "+
                        " FROM dbo.Course_Student_Info "+
                        " WHERE     (userid = N'"+E1.getText().toString()+"') AND (password = N'"+E2.getText().toString()+"')";
                ResultSet rs = st.executeQuery(Sql);

                if(rs.next())
                {
                    sTUid=rs.getString("AdmissionNo");
                    String SessionName=String.valueOf(spinner2.getSelectedItem());
                    E1.setText("");
                    E2.setText("");
                    Saveedata(sTUid,GetSessionID(SessionName));

                    SaveToken stt = new SaveToken();
                    stt.execute("");
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"You Entered Wrong Credentials. !!!",Toast.LENGTH_LONG).show();
                    E1.setText("");
                    E2.setText("");
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }
    public void Saveedata(String StuID,String SessiD)
    {
        session.createUserLoginSession(SessiD, StuID);

        SQLiteDatabase sq=openOrCreateDatabase("EduSoftErp", MODE_PRIVATE, null);
        /*Cursor c1424 = sq.rawQuery("SELECT * FROM  StudentDetails where id=1",null);

        if(c1424.moveToFirst()){


            do{
                ContentValues cr4=new ContentValues();
                cr4.put("StuID",StuID);
                cr4.put("SessionId",SessiD);
                sq.insert("StudentDetails",null,cr4);
                sq.update("StudentDetails",cr4,"id='1'",null);
                // Toast.makeText(getApplicationContext(), "UPDATE", Toast.LENGTH_LONG).show();

                Intent intent=new Intent(getApplicationContext(), WebPages.class);
                intent.putExtra("StuID", "LOGIN");
                startActivity(intent);

            }while(c1424.moveToNext());
        }
        else*/
        //{
        ContentValues cr4=new ContentValues();
        cr4.put("StuID",StuID);
        cr4.put("SessionId",SessiD);
        sq.insert("StudentDetails",null,cr4);

        // Toast.makeText(getApplicationContext(), "INSERT", Toast.LENGTH_LONG).show();

        Intent intent=new Intent(getApplicationContext(), UsersList.class);
        startActivity(intent);
        //}
    }
    public String GetSessionID(String SessionName)
    {
        String SeesionID="";

        String username = getString(R.string.username);
        String pwd = getString(R.string.password);
        String db = getString(R.string.db);
        String data_source=getString(R.string.data_source);

        Statement st;
        Connection connect = ConnectionHelper(username, pwd, db, data_source);

        try {
            st = connect.createStatement();
            String Sql = "SELECT sessionyr_id FROM dbo.session_mst WHERE (session_desc = '"+SessionName+"')";
            ResultSet rs = st.executeQuery(Sql);
            while (rs.next())
            {
                SeesionID=rs.getString("sessionyr_id");


            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return SeesionID;
    }

    public class SaveToken extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute(){


            Log.e("MyTag","kkkbtnclick7");
        }

        @Override
        protected void onPostExecute(String r)
        {
            Log.e("MyTag","kkkbtnclick8");
        }
        @Override
        protected String doInBackground(String... params)
        {

            try
            {
                con = ConnectionHelper(username, pwd, db, data_source);        // Connect to database
                if (con == null)
                {
                    z = "Check Your Internet Access!";
                }
                else
                {
                    Log.e("MyTag","kkkbtnclick10");
                    // Change below query according to your own database.
                    String query = "insert into Fcm_Token (AdmissionNo,fcmtoken) values('"+sTUid+"','"+fcmtokenid+"')";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    Log.e("MyTag","kkkbtnclick11");
                    Log.e("MyTag","kkkresult = "+rs.next());

                }
            }
            catch (Exception ex)
            {
                isSuccess = false;
                z = ex.getMessage();
            }

            return z;
        }
    }
}
