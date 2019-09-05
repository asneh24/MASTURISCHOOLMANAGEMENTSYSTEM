package com.ikspl.masturischool;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FMessageService extends FirebaseMessagingService {

    private NotificationManager notifManager;
    String title,body;
    String newfcmtokenid;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        title = remoteMessage.getNotification().getTitle();
        body = remoteMessage.getNotification().getBody();
        WebPages wp = new WebPages();

        String admissionno = wp.stuidd;
        Log.e("MyTag","kkkadmissionnoinfcmservice = "+admissionno);
        int NOTIFY_ID =(int)System.currentTimeMillis();
        String id = "11"; // default_channel_id
        String title = "CHNANLNAME"; // Default Channel
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent;

        PendingIntent pendingIntent;
        // NotificationCompat.Builder builder;
        NotificationCompat.Builder builder;

        if (notifManager == null) {
            notifManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.BLUE);
                mChannel.setSound(alarmSound,null);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }

            builder = new NotificationCompat.Builder(this, id);
            //builder = new NotificationCompat.Builder(context);
            intent = new Intent(this, WebPages.class);
            intent.putExtra("StuID", title);
            intent.putExtra("NotID", admissionno);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, NOTIFY_ID, intent, 0);
            builder.setContentTitle(remoteMessage.getNotification().getTitle())                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                            R.mipmap.ic_launcher))
                    .setContentText(remoteMessage.getNotification().getBody()) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setSound(alarmSound)
                    .setColor(this.getColor(R.color.colorPrimary))
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        else {
            builder = new NotificationCompat.Builder(this, id);
            //builder = new NotificationCompat.Builder(context);
            intent = new Intent(this, WebPages.class);
            intent.putExtra("StuID", title);
            intent.putExtra("NotID", admissionno);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, NOTIFY_ID, intent, 0);
            builder.setContentTitle(remoteMessage.getNotification().getTitle())                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                            R.mipmap.ic_launcher))
                    .setContentText(remoteMessage.getNotification().getBody()) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setSound(alarmSound)
                    .setColor(Color.GREEN)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
        //startForeground(NOTIFY_ID, notification);
    }

    @Override
    public void onNewToken(String s) {
        newfcmtokenid = s;
        Log.e("MyTag","kkk new Fcm Token = "+newfcmtokenid);
        try{
            sendRegistrationToServer();
        }    catch (Exception e)
        {
            Log.e("MyTag","kkkerrorinservice = "+ e);
        }

    }

    public void sendRegistrationToServer()
    {
        String username = getString(R.string.username);
        String pwd = getString(R.string.password);
        String db = getString(R.string.db);
        String data_source=getString(R.string.data_source);

        WebPages wp = new WebPages();

        String admissionno = wp.stuidd;
        Log.e("MyTag","kkkonrefreshtokenadmissionid = "+admissionno);

        Statement st;
        Connection connect = ConnectionHelper(username, pwd, db, data_source);

        try {
            st = connect.createStatement();
            String Sql = "update Fcm_token set fcmtoken = '" + newfcmtokenid + "' where Admission = '" + admissionno + "' ";
            ResultSet rs = st.executeQuery(Sql);

            if(rs.next())
            {

            }
            else
            {

            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
}
