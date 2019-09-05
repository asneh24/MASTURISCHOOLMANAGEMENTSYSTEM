package com.ikspl.masturischool;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    AlertDialog.Builder builder;
    String phonenum;
    Statement st;
    PreparedStatement preparedStatement;
    Connection con;
    String un,pass,db,ip;
    String datetime;
    Integer idd;
    UserSessionManager session;
    UserSessionManagerLogin sessionlogin;
    private static final int PERMISSION_READ_STATE = 0;
    Integer showw;

    CardView cardabout,cardacademics,cardgallery,cardvideogallery,cardadmission,cardcareer,cardselfcare,cardcontactus;
    ImageView imgabout,imgacademics,imggallery,imgvideogallery,imgadmission,imgcareer,imgselfcare,imgcontactus;
    TextView txtabout,txtacademics,txtgallery,txtvideogallery,txtadmission,txtcareer,txtselfcare,txtcontactus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //requesting permissions.
        ActivityCompat.requestPermissions(Dashboard.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_READ_STATE);

        un = getString(R.string.username);
        pass = getString(R.string.password);
        db = getString(R.string.db);
        ip = getString(R.string.data_source);

        session = new UserSessionManager(getApplicationContext());
        sessionlogin = new UserSessionManagerLogin(getApplicationContext());

        cardabout = findViewById(R.id.cardabout);
        cardacademics = findViewById(R.id.cardacademics);
        cardgallery = findViewById(R.id.cardgallery);
        cardvideogallery = findViewById(R.id.cardvideogallery);
        cardadmission = findViewById(R.id.cardadmission);
        cardcareer = findViewById(R.id.cardcareer);
        cardselfcare = findViewById(R.id.cardselfcare);
        cardcontactus = findViewById(R.id.cardcontactus);
        imgabout = findViewById(R.id.imgabout);
        imgacademics = findViewById(R.id.imgacademics);
        imggallery = findViewById(R.id.imggallery);
        imgvideogallery = findViewById(R.id.imgvideogallery);
        imgadmission = findViewById(R.id.imgadmission);
        imgcareer = findViewById(R.id.imgcareer);
        imgselfcare = findViewById(R.id.imgselfcare);
        imgcontactus = findViewById(R.id.imgcontactus);
        txtabout = findViewById(R.id.txtabout);
        txtacademics = findViewById(R.id.txtacademics);
        txtgallery = findViewById(R.id.txtgallery);
        txtvideogallery = findViewById(R.id.txtvideogallery);
        txtadmission = findViewById(R.id.txtadmission);
        txtcareer = findViewById(R.id.txtcareer);
        txtselfcare = findViewById(R.id.txtselfcare);
        txtcontactus = findViewById(R.id.txtcontactus);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        datetime = dateformat.format(c.getTime());

        final Loginid loginid = new Loginid();
        loginid.execute("");

        if(session.checkLogin())
        {
            builder = new AlertDialog.Builder(this);
            builder.setMessage("Please Enter Mobile Number");
            final EditText input = new EditText(Dashboard.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            input.setInputType(InputType.TYPE_CLASS_PHONE);
            builder.setView(input); // uncomment this line
            builder.setCancelable(true)
                    .setPositiveButton("Register", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(input.getText().toString() == "")
                            {
                                Toast.makeText(Dashboard.this,"Please Enter Phone Number",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                phonenum = input.getText().toString();
                                SaveNumber savenumber = new SaveNumber();
                                savenumber.execute("");
                            }
                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Registration");
            alert.show();
        }
        CreateDatabase();

        cardabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "About.aspx");
                startActivity(ii);
            }
        });
        imgabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "About.aspx");
                startActivity(ii);
            }
        });
        txtabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "About.aspx");
                startActivity(ii);
            }
        });

        cardacademics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "Academic.aspx");
                startActivity(ii);
            }
        });
        imgacademics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "Academic.aspx");
                startActivity(ii);
            }
        });
        txtacademics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "Academic.aspx");
                startActivity(ii);
            }
        });

        cardgallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "Gallery.aspx");
                startActivity(ii);
            }
        });
        imggallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "Gallery.aspx");
                startActivity(ii);
            }
        });
        txtgallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "Gallery.aspx");
                startActivity(ii);
            }
        });

        cardvideogallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "VideoGallery.aspx");
                startActivity(ii);
            }
        });
        imgvideogallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "VideoGallery.aspx");
                startActivity(ii);
            }
        });
        txtvideogallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "VideoGallery.aspx");
                startActivity(ii);
            }
        });

        cardadmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "Enquiry.aspx");
                startActivity(ii);
            }
        });
        imgadmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "Enquiry.aspx");
                startActivity(ii);
            }
        });
        txtadmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "Enquiry.aspx");
                startActivity(ii);
            }
        });

        cardcareer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "Enquiry_job.aspx");
                startActivity(ii);
            }
        });
        imgcareer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "Enquiry_job.aspx");
                startActivity(ii);
            }
        });
        txtcareer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "Enquiry_job.aspx");
                startActivity(ii);
            }
        });

        cardselfcare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,UsersList.class);
                startActivity(ii);

                /* Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "SelfLogin");
                startActivity(ii);*/
            }
        });
        imgselfcare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,UsersList.class);
                startActivity(ii);

                /* Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "SelfLogin");
                startActivity(ii);*/
            }
        });
        txtselfcare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,UsersList.class);
                startActivity(ii);

                /* Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "SelfLogin");
                startActivity(ii);*/
            }
        });

        cardcontactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "Contactus.aspx");
                startActivity(ii);
            }
        });
        imgcontactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "Contactus.aspx");
                startActivity(ii);
            }
        });
        txtcontactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Dashboard.this,WebPages.class);
                ii.putExtra("StuID", "Contactus.aspx");
                startActivity(ii);
            }
        });




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void addAutoStartup() {

        try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() > 0) {
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("exc", String.valueOf(e));
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_About) {
            Intent ii = new Intent(Dashboard.this,WebPages.class);
            ii.putExtra("StuID", "About.aspx");
            startActivity(ii);

        } else if (id == R.id.nav_Academic) {
            Intent ii = new Intent(Dashboard.this,WebPages.class);
            ii.putExtra("StuID", "Academic.aspx");
            startActivity(ii);

        } else if (id == R.id.nav_gallery) {
            Intent ii = new Intent(Dashboard.this,WebPages.class);
            ii.putExtra("StuID", "Gallery.aspx");
            startActivity(ii);

        } else if (id == R.id.nav_Videogallery) {
            Intent ii = new Intent(Dashboard.this,WebPages.class);
            ii.putExtra("StuID", "VideoGallery.aspx");
            startActivity(ii);

        } else if (id == R.id.nav_Enquiry) {
            Intent ii = new Intent(Dashboard.this,WebPages.class);
            ii.putExtra("StuID", "Enquiry.aspx");
            startActivity(ii);

        } else if (id == R.id.nav_job) {
            Intent ii = new Intent(Dashboard.this,WebPages.class);
            ii.putExtra("StuID", "Enquiry_job.aspx");
            startActivity(ii);

        } else if (id == R.id.nav_Contact) {
            Intent ii = new Intent(Dashboard.this,WebPages.class);
            ii.putExtra("StuID", "Contactus.aspx");
            startActivity(ii);

        } else if (id == R.id.nav_send) {
            addAutoStartup();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void CreateDatabase() {
        SQLiteDatabase sq = openOrCreateDatabase("EduSoftErp", MODE_PRIVATE, null);
        String FP = "CREATE TABLE IF NOT EXISTS StudentDetails(id INTEGER PRIMARY KEY AUTOINCREMENT,StuID nvarchar,SessionId INTEGER)";
        //table created
        sq.execSQL(FP);
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

    public class SaveNumber extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute(){

            Log.e("MyTag","kkkx== 2");
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r)
        {
            Log.e("MyTag","kkkx== 7");
            //progressBar.setVisibility(View.GONE);
            Toast.makeText(Dashboard.this, r, Toast.LENGTH_SHORT).show();
            Log.e("MyTag","kkkisAfterSuccess"+isSuccess);
            if(isSuccess)
            {
                session.createUserLoginSession(phonenum);

                Log.e("MyTag","kkkx== 8");
                Toast.makeText(Dashboard.this , "Registration Successfully" , Toast.LENGTH_LONG).show();
            }

        }
        @Override
        protected String doInBackground(String... params)
        {

            try
            {
                con = connectionclass(un, pass, db, ip);        // Connect to database
                if (con == null)
                {
                    z = "Check Your Internet Access!";
                }
                else
                {
                    Log.e("MyTag","kkkx== 5");
                    Log.e("MyTag","kkkisBeforeSuccess"+isSuccess);

                    st = con.createStatement();
                    preparedStatement = con.prepareStatement("insert into AppInstalled (TableID,phonenum,dateandtime) values('"+idd+"','"+phonenum+"','"+datetime+"');");
                    isSuccess = true;
                    Log.e("MyTag","kkkx== 6");
                    z = "Added Successfully";
                    preparedStatement.executeUpdate();
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

    public class Loginid extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected String doInBackground(String... strings) {
            try
            {
                con = connectionclass(un, pass, db, ip);        // Connect to database
                if (con == null)
                {
                    z = "Check Your Internet Access!";

                }
                else
                {
                    // Change below query according to your own database.
                    String query = "select  isnull(Max(TableID),0)+1  as Colomn from AppInstalled;";
                    //String query = "select * from Online_Enquiry;";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if(rs.next())
                    {
                        idd = rs.getInt(1);
                        isSuccess=true;
                        con.close();
                    }
                    else
                    {
                        z = "Invalid Credentials!";
                        isSuccess = false;
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

        }

        @Override
        protected void onPostExecute(String r)
        {

        }
    }

}

