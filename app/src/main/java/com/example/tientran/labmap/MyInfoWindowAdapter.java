package com.example.tientran.labmap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by TienTran on 11/13/2017.
 */

public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Activity context;
    private Bitmap bitmap;


    public MyInfoWindowAdapter(Activity context, Bitmap bitmap)
    {
        this.context = context;
        this.bitmap = bitmap;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View v = this.context.getLayoutInflater().inflate(R.layout.custom_info_window, null);
        //lay vi tri tu maker
        LatLng latLng = marker.getPosition();

        ImageView imageView = (ImageView)v.findViewById(R.id.imageView);
        TextView tvLat = (TextView)v.findViewById(R.id.tv_lat);
        TextView tvLng = (TextView)v.findViewById(R.id.tv_lng);
        TextView tvTitle = (TextView)v.findViewById(R.id.tv_title);
        TextView tvSippet = (TextView)v.findViewById(R.id.tv_snippet);

        //set
        imageView.setImageBitmap(bitmap);
        tvLat.setText("Latitude: " + latLng.latitude);
        tvLng.setText("Longitude: " + latLng.longitude);
        tvTitle.setText(marker.getTitle());
        tvSippet.setText(marker.getSnippet());

        return v;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
}
