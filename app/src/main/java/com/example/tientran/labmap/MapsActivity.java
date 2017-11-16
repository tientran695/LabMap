package com.example.tientran.labmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MapsActivity extends FragmentActivity implements
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback {

    private Bitmap bitmap = null;
    private GoogleMap mMap;
    private String ip_socket = "http://192.168.95.5:3000";
    private Socket mSocket;

    Emitter.Listener onNewImage;
    {
        try {
            mSocket = IO.socket(ip_socket);
        } catch (Exception e) {
            e.printStackTrace();
        }

        onNewImage = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                handleNewImage(args[0]);
            }
        };
    }

    private void handleNewImage(final Object arg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                byte[] imageArray = (byte[]) arg;
                bitmap = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mSocket.connect();
        mSocket.on("SERVER", onNewImage);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng BACHKHOA = new LatLng(21.005796, 105.842064);
        LatLng XAYDUNG = new LatLng(21.004202, 105.842068);

        MarkerOptions bachkhoa = new MarkerOptions();
        bachkhoa.title("Viện Điện Tử và Truyền Thông");
        bachkhoa.snippet("C9 Đại Học Bách Khoa Hà Nội");
        bachkhoa.position(BACHKHOA);

        MarkerOptions xaydung = new MarkerOptions();
        xaydung.title("Khoa Kiến trúc và Quy hoạch");
        xaydung.snippet("Đại học xây dựng Hà Nội");
        xaydung.position(XAYDUNG);

        Marker currentMarker = mMap.addMarker(bachkhoa);
        Marker haylam = mMap.addMarker(xaydung);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BACHKHOA, 13));
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mSocket.emit("GETIMAGE");
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(this, bitmap));
        return false;
    }
}
