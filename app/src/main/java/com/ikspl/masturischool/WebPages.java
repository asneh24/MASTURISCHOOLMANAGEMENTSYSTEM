package com.ikspl.masturischool;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class WebPages extends AppCompatActivity {

    WebView myWebView;
    ProgressDialog progressDialog;
    UserSessionManager session;
    UserSessionManagerLogin sessionlogin;
    String datetime;
    String stuidd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_pages);
        session = new UserSessionManager(getApplicationContext());
        sessionlogin = new UserSessionManagerLogin(getApplicationContext());

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        datetime = dateformat.format(c.getTime());

        CreateDatabase();

        myWebView = (WebView) findViewById(R.id.help_webview);
        progressDialog = new ProgressDialog(WebPages.this);
        progressDialog.setMessage("Please wait Loading...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        myWebView.getSettings().setLoadsImagesAutomatically(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.setWebViewClient(new MyWebViewClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("MyTag","kkk12");
                Log.v("MyTag","Permission is granted");
                myWebView.setDownloadListener(new DownloadListener() {

                    @Override
                    public void onDownloadStart(String url, String userAgent,
                                                String contentDisposition, String mimetype,
                                                long contentLength) {

                        DownloadManager.Request request = new DownloadManager.Request(
                                Uri.parse(url));
                        Log.e("MyTag","kkkurl"+url.toString());
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Files from Teachers");
                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(request);
                        Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                                Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();


                    }
                });
                // downloadDialog(url,userAgent,contentDisposition,mimetype);

            } else {
                Log.e("MyTag","kkk13");

                Log.v("MyTag","Permission is revoked");

            }
        }
        else {
            Log.e("MyTag","kkk14");
            //Code for devices below API 23 or Marshmallow
            Log.v("MyTag","Permission is granted");
            myWebView.setDownloadListener(new DownloadListener() {

                @Override
                public void onDownloadStart(String url, String userAgent,
                                            String contentDisposition, String mimetype,
                                            long contentLength) {

                    DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse(url));
                    Log.e("MyTag","kkkurl"+url.toString());
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Files From Teachers");
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                            Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();

                }
            });

        }

        Intent intent = getIntent();

        if(intent.getStringExtra("StuID").toString().equalsIgnoreCase("About.aspx"))
        {
            myWebView.loadUrl(getString(R.string.SiteName)+"About.aspx");
        }else  if(intent.getStringExtra("StuID").toString().equalsIgnoreCase("Academic.aspx"))
        {
            myWebView.loadUrl(getString(R.string.SiteName)+"Academic.aspx");
        }else  if(intent.getStringExtra("StuID").toString().equalsIgnoreCase("Gallery.aspx"))
        {
            myWebView.loadUrl(getString(R.string.SiteName)+"Gallery.aspx");
        }else  if(intent.getStringExtra("StuID").toString().equalsIgnoreCase("VideoGallery.aspx"))
        {
            myWebView.loadUrl(getString(R.string.SiteName)+"VideoGallery.aspx");
        }else  if(intent.getStringExtra("StuID").toString().equalsIgnoreCase("Enquiry.aspx"))
        {
            myWebView.loadUrl(getString(R.string.SiteName)+"Enquiry.aspx");
        }else  if(intent.getStringExtra("StuID").toString().equalsIgnoreCase("Enquiry_job.aspx"))
        {
            myWebView.loadUrl(getString(R.string.SiteName)+"Enquiry_job.aspx");
        }else  if(intent.getStringExtra("StuID").toString().equalsIgnoreCase("SelfLogin"))
        {
            CheckLoginSelfcare();
        } else  if(intent.getStringExtra("StuID").toString().equalsIgnoreCase("Contactus.aspx"))
        {
            myWebView.loadUrl(getString(R.string.SiteName)+"Contactus.aspx");
        }
        else if (intent.getStringExtra("StuID").toString().equalsIgnoreCase("LOGIN")) {    //Toast.makeText(getApplicationContext(),intent.getStringExtra("StuID").toString(), Toast.LENGTH_SHORT).show();
            //myWebView.loadUrl(getString(R.string.SiteName)+"STUDENT_PORTAL/Default.aspx?UI=LKG/2018-19/131/10001&SI=1");
            Log.e("MyTag","kkk111");
            GetHomePage(intent.getStringExtra("SNo").toString());
        } else if (intent.getStringExtra("StuID").toString().equalsIgnoreCase("NOTICE")) {
            //myWebView.loadUrl(getString(R.string.SiteName)+"STUDENT_PORTAL/NoticeMst.aspx?UI=LKG/2018-19/131/10001&SI=1");
            //Open Notice Tab
            //myWebView.loadUrl(getString(R.string.SiteName)+"STUDENT_PORTAL/NoticeMst.aspx?UI=LKG/2018-19/131/10001&SI=1&NO="+intent.getStringExtra("NotID").toString());
            GetNoticePage(intent.getStringExtra("NotID").toString());
        } else if (intent.getStringExtra("StuID").toString().equalsIgnoreCase("HOMEWORK")) {
            GetHomeworkPage(intent.getStringExtra("NotID").toString());
        } else if (intent.getStringExtra("StuID").toString().equalsIgnoreCase("EVENT")) {
            GetEventPage(intent.getStringExtra("NotID").toString());
        } else if (intent.getStringExtra("StuID").toString().equalsIgnoreCase("ATTENDANCE")) {
            GetAttendancePage(intent.getStringExtra("NotID").toString());
        } else {
            Toast.makeText(getApplicationContext(), intent.getStringExtra("StuID").toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {// back to page
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.myWebView.canGoBack()) {
            this.myWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
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

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            System.out.println("on finish");
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

        }
    }

    public void GetNoticePage(String NoticeID) {
        SQLiteDatabase sq = openOrCreateDatabase("EduSoftErp", MODE_PRIVATE, null);
        Cursor c1424 = sq.rawQuery("SELECT * FROM  StudentDetails where StuID='"+NoticeID+"'", null);

        if (c1424.moveToFirst()) {

            do {

                String Stuid = c1424.getString(1);
                String Sesionid = c1424.getString(2);

                myWebView.loadUrl(getString(R.string.SiteName) + "STUDENT_PORTAL/NoticeMst.aspx?UI=" + Stuid + "&SI=" + Sesionid + "&NO=" + NoticeID);

            } while (c1424.moveToNext());
        } else {

        }
    }

    public void GetHomeworkPage(String NoticeID) {
        SQLiteDatabase sq = openOrCreateDatabase("EduSoftErp", MODE_PRIVATE, null);
        Cursor c1424 = sq.rawQuery("SELECT * FROM  StudentDetails where StuID='"+NoticeID+"'", null);

        if (c1424.moveToFirst()) {

            do {

                String Stuid = c1424.getString(1);
                String Sesionid = c1424.getString(2);

                myWebView.loadUrl(getString(R.string.SiteName) + "STUDENT_PORTAL/Homework.aspx?UI=" + Stuid + "&SI=" + Sesionid + "&NO=" + NoticeID);

            } while (c1424.moveToNext());
        } else {

        }
    }

    public void GetEventPage(String NoticeID) {
        SQLiteDatabase sq = openOrCreateDatabase("EduSoftErp", MODE_PRIVATE, null);
        Cursor c1424 = sq.rawQuery("SELECT * FROM  StudentDetails where StuID='"+NoticeID+"'", null);

        if (c1424.moveToFirst()) {

            do {

                String Stuid = c1424.getString(1);
                String Sesionid = c1424.getString(2);

                myWebView.loadUrl(getString(R.string.SiteName) + "STUDENT_PORTAL/Event.aspx?UI=" + Stuid + "&SI=" + Sesionid + "&NO=" + NoticeID);

            } while (c1424.moveToNext());
        } else {

        }
    }

    public void GetAttendancePage(String NoticeID) {
        SQLiteDatabase sq = openOrCreateDatabase("EduSoftErp", MODE_PRIVATE, null);
        Cursor c1424 = sq.rawQuery("SELECT * FROM  StudentDetails where StuID='"+NoticeID+"'", null);

        if (c1424.moveToFirst()) {

            do {

                String Stuid = c1424.getString(1);
                String Sesionid = c1424.getString(2);

                myWebView.loadUrl(getString(R.string.SiteName) + "STUDENT_PORTAL/Attendance.aspx?UI=" + Stuid + "&SI=" + Sesionid + "&NO=" + NoticeID);

            } while (c1424.moveToNext());
        } else {

        }
    }

    public void GetHomePage(String snoo) {
        Log.e("MyTag","kkk222");
        Log.e("MyTag","kkkserialno = "+snoo);
        SQLiteDatabase sq = openOrCreateDatabase("EduSoftErp", MODE_PRIVATE, null);
        Cursor c1424 = sq.rawQuery("SELECT * FROM  StudentDetails where StuID='"+snoo+"'", null);
        Log.e("MyTag","kkk333");
        if (c1424.moveToFirst()) {

            do {
                Log.e("MyTag","kkk444");
                String Stuid = c1424.getString(1);
                stuidd = Stuid;
                String Sesionid = c1424.getString(2);
                Log.e("MyTag","kkkhomepageadmissionid = "+stuidd);
                myWebView.loadUrl(getString(R.string.SiteName) + "STUDENT_PORTAL/Default.aspx?UI=" + Stuid + "&SI=" + Sesionid);

            } while (c1424.moveToNext());
        } else {

        }
    }

    public void CheckLoginSelfcare() {
        SQLiteDatabase sq = openOrCreateDatabase("EduSoftErp", MODE_PRIVATE, null);
        Cursor c1424 = sq.rawQuery("SELECT * FROM  StudentDetails where id=1", null);

        if (c1424.moveToFirst()) {


            do {

                String Stuid = c1424.getString(1);
                String Sesionid = c1424.getString(2);
                stuidd = Stuid;
                Log.e("MyTag","kkkcheckloginadmissionid = "+stuidd);
                myWebView.loadUrl(getString(R.string.SiteName) + "STUDENT_PORTAL/Default.aspx?UI=" + Stuid + "&SI=" + Sesionid + "");

            } while (c1424.moveToNext());
        } else {
            startActivity(new Intent(WebPages.this, LoginActivity.class));
        }
    }

    public void Cleardata() {

        SQLiteDatabase sq = openOrCreateDatabase("EduSoftErp", MODE_PRIVATE, null);
        sq.delete("StudentDetails", "StuID='"+stuidd+"'", null);
        Toast.makeText(getApplicationContext(), "Successfully Logout", Toast.LENGTH_LONG).show();
        sessionlogin.logoutUser();

    }

    public void CreateDatabase() {
        SQLiteDatabase sq = openOrCreateDatabase("EduSoftErp", MODE_PRIVATE, null);
        String FP = "CREATE TABLE IF NOT EXISTS StudentDetails(id INTEGER PRIMARY KEY AUTOINCREMENT,StuID nvarchar,SessionId INTEGER)";
        //table created
        sq.execSQL(FP);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.switchuser) {
            Intent ii = new Intent(WebPages.this,UsersList.class);
            startActivity(ii);

        }else if (id == R.id.changepassword) {
            Intent ii = new Intent(WebPages.this,ChangePassword.class);
            startActivity(ii);

        }else
        if (id == R.id.forgetpassword) {
            Intent ii = new Intent(WebPages.this,ForgetPassword.class);
            startActivity(ii);
        }else
        if (id == R.id.logout) {
            //return true;
            Cleardata();
            logoutt();
            finish();
        }

        return super.onOptionsItemSelected(item);
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

    public void logoutt()
    {
        Log.e("MyTag","kkklogoutadmissionno = "+stuidd);
        String username = getString(R.string.username);
        String pwd = getString(R.string.password);
        String db = getString(R.string.db);
        String data_source=getString(R.string.data_source);

        Statement st;
        Connection connect = ConnectionHelper(username, pwd, db, data_source);

        try {
            st = connect.createStatement();
            String Sql = "delete from Fcm_token where AdmissionNo = '"+stuidd+"'";
            ResultSet rs = st.executeQuery(Sql);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
