package com.ikspl.masturischool;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UsersList extends AppCompatActivity {

    RecyclerView recycleruserlist;
    Button btnanotherlogin;
    String un,pass,db,ip;
    Connection con;
    String stuid,sesionid;
    ArrayList<UserListModel> arraylist;
    Integer sqllistno;
    ArrayList<String> arrayadmission;
    ProgressDialog progressDialog;
    TextView txtmsg;

    UserListAdapter ula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        un = getString(R.string.username);
        pass = getString(R.string.password);
        db = getString(R.string.db);
        ip = getString(R.string.data_source);
        arraylist = new ArrayList<UserListModel>();
        txtmsg = findViewById(R.id.txtmsg);

        recycleruserlist = findViewById(R.id.recycleruserlists);
        recycleruserlist.hasFixedSize();
        RecyclerView.LayoutManager layoutManager =new LinearLayoutManager( UsersList.this,LinearLayoutManager.VERTICAL,false )  ;//3
        recycleruserlist.setLayoutManager(layoutManager);
        arrayadmission = new ArrayList<>();

        CreateDatabase();

        ShowDetails sd = new ShowDetails();
        sd.execute("");

        btnanotherlogin = findViewById(R.id.btnanotheraccount);
        btnanotherlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(UsersList.this,LoginActivity.class);
                startActivity(ii);
                finish();
            }
        });
    }

    public void CreateDatabase() {
        SQLiteDatabase sq = openOrCreateDatabase("EduSoftErp", MODE_PRIVATE, null);
        String FP = "CREATE TABLE IF NOT EXISTS StudentDetails(id INTEGER PRIMARY KEY AUTOINCREMENT,StuID nvarchar,SessionId INTEGER)";
        //table created
        sq.execSQL(FP);
    }

    public void getdata()
    {
        SQLiteDatabase sq = openOrCreateDatabase("EduSoftErp", MODE_PRIVATE, null);
        Cursor c1424 = sq.rawQuery("SELECT * FROM  StudentDetails", null);

        if (c1424.moveToFirst()) {


            do {

                stuid = c1424.getString(1);
                sesionid = c1424.getString(2);
                Log.e("MyTag","kkkstdidd = "+stuid);
                arrayadmission.add(stuid);

                Log.e("MyTag","kkkinner1");



            } while (c1424.moveToNext());
        }
    }

    public class ShowDetails extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected String doInBackground(String... strings) {
            try
            {
                if (arrayadmission.size() == 0)
                {
                    txtmsg.setVisibility(View.VISIBLE);
                }
                else
                {
                    for (int i = 0; i <= arrayadmission.size(); i++) {
                        Log.e("MyTag", "kkkarrayadmission = " + arrayadmission.get(i));
                        Log.e("MyTag", "kkkinner2");

                        con = connectionclass(un, pass, db, ip);        // Connect to database
                        if (con == null)
                        {
                            z = "Check Your Internet Access!";

                        }
                        else {


                            String query = "select AdmissionNo,first_name,fatherName,motherName,photo_upload from Course_Student_Info where AdmissionNo = '" + arrayadmission.get(i) + "' ";

                            Statement stmt = con.createStatement();
                            ResultSet rs = stmt.executeQuery(query);
                            if (rs.next()) {
                                Log.e("MyTag", "kkkinner3");
                                UserListModel ulm = new UserListModel();
                                ulm.setStdadmisionid(rs.getString(1));
                                ulm.setStdname(rs.getString(2));
                                ulm.setStdfathername(rs.getString(3));
                                ulm.setStdmothername(rs.getString(4));
                                ulm.setPic(rs.getBlob(5));
                                arraylist.add(ulm);
                                isSuccess = true;
                                con.close();
                            } else {
                                z = "Invalid Credentials!";
                                isSuccess = false;
                            }


                            // Change below query according to your own database.


                        }
                    }
                }

            }
            catch (Exception ex)
            {
                isSuccess = false;
                z = ex.getMessage();
            }

            return z;
        }

        @Override
        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(UsersList.this);
            progressDialog.setMessage("Please wait Loading...");
            progressDialog.setCancelable(true);
            progressDialog.show();
            getdata();
        }

        @Override
        protected void onPostExecute(String r)
        {

            Log.e("MyTag","kkkarraylist = "+arraylist.toString());
            ula = new UserListAdapter(UsersList.this,arraylist);
            recycleruserlist.setAdapter(ula);
            progressDialog.dismiss();



        }
    }

    @SuppressLint("NewApi")
    public Connection connectionclass(String user, String password, String database, String server)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://"+server+"/"+database+";user="+user+";password="+password;
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
}
