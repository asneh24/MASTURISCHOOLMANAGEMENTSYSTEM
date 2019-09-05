package com.ikspl.masturischool;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ChangePassword extends AppCompatActivity {

    EditText E1,E2,E3;
    Spinner spinner2;
    String login_id=null,paswrd=null;
    Button btnchange;
    String username,pwd,db,data_source;
    Connection con;
    UserSessionManagerLogin session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        session = new UserSessionManagerLogin(getApplicationContext());
        //stopService(new Intent(getBaseContext(), Service1.class));
        E1=(EditText)findViewById(R.id.editText1);
        E2=(EditText)findViewById(R.id.editText2);
        E3=(EditText)findViewById(R.id.editText3);
        spinner2 = (Spinner) findViewById(R.id.Spin1);
        btnchange = findViewById(R.id.btnchangepassword);
        addItemsOnSpinner2();

        username = getString(R.string.username);
        pwd = getString(R.string.password);
        db = getString(R.string.db);
        data_source=getString(R.string.data_source);



        btnchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MyTag","kkkbtnclick1");
                changepasswordd();
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

        username = getString(R.string.username);
        pwd = getString(R.string.password);
        db = getString(R.string.db);
        data_source=getString(R.string.data_source);

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

    public void changepasswordd()
    {
        Log.e("MyTag","kkkbtnclick2");

        if(E1.getText().toString().equalsIgnoreCase(""))
        {
            Log.e("MyTag","kkkbtnclick3");
            E1.setHint("Please Enter User-ID");
            E1.setHintTextColor(Color.RED);
            Toast.makeText(getApplicationContext(), "Please Enter User-ID", Toast.LENGTH_LONG).show();
        }
        else if(E2.getText().toString().equalsIgnoreCase(""))
        {
            Log.e("MyTag","kkkbtnclick4");
            E2.setHint("Please Enter Old Password");
            E2.setHintTextColor(Color.RED);
            Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_LONG).show();
        }
        else if(E3.getText().toString().equalsIgnoreCase(""))
        {
            Log.e("MyTag","kkkbtnclick5");
            E3.setHint("Please Enter New Password");
            E3.setHintTextColor(Color.RED);
            Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_LONG).show();
        }
        else
        {
            Log.e("MyTag","kkkbtnclick6");
            Getdata getdata = new Getdata();
            getdata.execute("");

        }

    }

    public class Getdata extends AsyncTask<String,String,String>
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
            E1.setText("");
            E2.setText("");
            E3.setText("");
            Toast.makeText(ChangePassword.this,"Password Change Successfully",Toast.LENGTH_SHORT).show();
            Cleardata();
            finish();


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
                    String query = "update Course_Student_Info set password = '" + E3.getText().toString() + "' where userid = '" + E1.getText().toString() + "' and password = '" + E2.getText().toString() + "' ";
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

    public void Cleardata() {

        SQLiteDatabase sq = openOrCreateDatabase("EduSoftErp", MODE_PRIVATE, null);
        sq.delete("StudentDetails", "id='1'", null);
        Toast.makeText(getApplicationContext(), "Successfully Logout", Toast.LENGTH_LONG).show();
        session.logoutUser();

    }
}
