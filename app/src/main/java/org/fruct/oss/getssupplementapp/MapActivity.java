package org.fruct.oss.getssupplementapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;


public class MapActivity extends Activity {

    static Context context;
    private MapView mMapView;
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

        context = getApplicationContext();
        mMapView = (MapView) findViewById(R.id.activity_map_mapview);
        setUpMapView();
    }


    private void setUpMapView() {
        mMapView.setClickable(true);
        mMapView.getController().setZoom(17);
        mMapView.setUseDataConnection(true);
        mMapView.setUserLocationEnabled(true);
        mMapView.setDiskCacheEnabled(true);

        if (sLocation != null)
            mMapView.getController().setCenter(new LatLng(getLocation().getLatitude(), getLocation().getLongitude()));
        else
            mMapView.getController().setZoom(3);
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
}
