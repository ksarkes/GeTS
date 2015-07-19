package org.fruct.oss.getssupplementapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;

import org.fruct.oss.getssupplementapp.Api.CategoriesGet;
import org.fruct.oss.getssupplementapp.Database.GetsDbHelper;
import org.fruct.oss.getssupplementapp.Model.CategoriesResponse;
import org.fruct.oss.getssupplementapp.Model.DatabaseType;


public class MapActivity extends Activity implements LocationListener{

    static Context context;
    public MapView mMapView;
    public static Location getLocation() {
        return sLocation;
    }

    private static void setLocation(Location sLocation) {
        MapActivity.sLocation = sLocation;
    }

    private static Location sLocation;

    public Marker getCurrentSelectedMarker() {
        return currentSelectedMarker;
    }

    public void setCurrentSelectedMarker(Marker currentSelectedMarker) {
        this.currentSelectedMarker = currentSelectedMarker;
    }

    Marker currentSelectedMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setUpLocation();
        context = getApplicationContext();
        mMapView = (MapView) findViewById(R.id.activity_map_mapview);
        setUpMapView();
        if (!isAuthorized()) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivityForResult(i, Const.INTENT_RESULT_TOKEN);
            // TODO: make intent for result (load points after authorization)
        } else {
            Log.d(Const.TAG, "Authorized, downloading categories");
            loadPoints();
        }
    }

    public static boolean isAuthorized() {
        return Settings.getToken(context) != null;
    }
    private void setUpMapView() {
        mMapView.setClickable(true);
        mMapView.getController().setZoom(17);
        mMapView.setUseDataConnection(true);
        mMapView.setUserLocationEnabled(true);
        mMapView.setDiskCacheEnabled(true);

        findViewById(R.id.activity_map_my_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getLocation() == null)
                    return;

                mMapView.getController().animateTo(
                        new LatLng(
                                getLocation().getLatitude(),
                                getLocation().getLongitude())
                );
                Toast.makeText(getApplicationContext(), Settings.getToken(context), Toast.LENGTH_SHORT).show();
            }
        });
        if (sLocation != null)
            mMapView.getController().setCenter(new LatLng(getLocation().getLatitude(), getLocation().getLongitude()));
        else
            mMapView.getController().setZoom(3);
    }

    private void setUpLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationProvider gpsProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        LocationProvider networkProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);

        if (gpsProvider != null) {
            locationManager.requestLocationUpdates(gpsProvider.getName(), 1000, 50, this);
            Location gpsLocation = locationManager.getLastKnownLocation(gpsProvider.getName());
            // If gps isn't connected yet, try to obtain network location
            if (gpsLocation == null)
                setLocation(locationManager.getLastKnownLocation(networkProvider.getName()));
            return;
        }

        if (networkProvider != null) {
            locationManager.requestLocationUpdates(networkProvider.getName(), 1000, 100, this);
            setLocation(locationManager.getLastKnownLocation(networkProvider.getName()));
            return;
        }

        Toast.makeText(this, "Can't determine location", Toast.LENGTH_SHORT).show();
    }
    private void loadPoints() {
        CategoriesGet categoriesGet = new CategoriesGet(Settings.getToken(getApplicationContext()));

        categoriesGet.execute();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(this, AddNewPointActivity.class);
            startActivityForResult(intent, Const.INTENT_RESULT_NEW_POINT);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onLocationChanged(Location location) {
        sLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
