package com.example.u1arlyncotradoejercicio2tema5;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

public class Servicio extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String NOTIFICATION_CHANNEL_ID = "1000";
    public static final String NOTIFICATION_CHANNEL_NAME = "BAntivirus";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override //notificacion
    public int onStartCommand(Intent intenc, int flags, int idArranque) {

        NotificationCompat.Builder notific = new NotificationCompat.Builder(this)
                .setContentTitle(Html.fromHtml("<b>App Lugares Cercanos</b>"))
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_light_normal);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //si es mayor a android oreo=8
            NotificationChannel notificationChannel =
                    new NotificationChannel(
                            NOTIFICATION_CHANNEL_ID,
                            NOTIFICATION_CHANNEL_NAME,
                            NotificationManager.IMPORTANCE_LOW);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(R.color.colorAccent);
            notificationManager.createNotificationChannel(notificationChannel);
            notific.setChannelId(NOTIFICATION_CHANNEL_ID);

        }

        startForeground(101,notific.build());
        return START_STICKY;


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
