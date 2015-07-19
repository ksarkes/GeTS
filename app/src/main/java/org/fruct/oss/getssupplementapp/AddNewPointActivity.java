package org.fruct.oss.getssupplementapp;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;

import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.MapEventsOverlay;
import com.mapbox.mapboxsdk.overlay.MapEventsReceiver;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;

/**
 * Created by Andrey on 18.07.2015.
 */
public class AddNewPointActivity extends Activity {

    RatingBar rbRating;

    Button btCategory;

    ImageButton btLocation;
    ImageButton btZoomIn;
    ImageButton btZoomOut;

    private MapView mMap;

    public Marker getChoosedLocation() {
        return choosedLocation;
    }

    public void setChoosedLocation(Marker _choosedLocation) { this.choosedLocation = _choosedLocation; }

    Marker choosedLocation = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewpoint);

        rbRating = (RatingBar) findViewById(R.id.activity_addpoint_ratingbar);

        btCategory = (Button) findViewById(R.id.activity_addpoint_category);
        btCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CategoryActivity.class);
                startActivityForResult(i, Const.INTENT_RESULT_CATEGORY);
            }
        });

        btLocation = (ImageButton) findViewById(R.id.activity_addpoint_location);
        btLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MapActivity.getLocation() != null) {
                    LatLng myLocation = new LatLng(MapActivity.getLocation().getLatitude(), MapActivity.getLocation().getLongitude());
                    mMap.getController().animateTo(myLocation);
                }
            }
        });

        btZoomIn = (ImageButton) findViewById(R.id.activity_addpoint_zoom_in);
        btZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.zoomIn();
            }
        });

        btZoomOut = (ImageButton) findViewById(R.id.activity_addpoint_zoom_out);
        btZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.zoomOut();
            }
        });

        prepareMap();
    }
    private void prepareMap() {
        mMap = (MapView) findViewById(R.id.activity_addpoint_mapview);

        mMap.setClickable(true);
        mMap.setUserLocationEnabled(true);

        int optimalZoom = 16;
        if (mMap.getMaxZoomLevel() < optimalZoom)
            mMap.getController().setZoom(mMap.getMaxZoomLevel());
        else
            mMap.getController().setZoom(optimalZoom);

        mMap.setUseDataConnection(true);

        if (MapActivity.getLocation() != null) {
            double latitude =  MapActivity.getLocation().getLatitude();
            double longitude =  MapActivity.getLocation().getLongitude();

            LatLng myLocation = new LatLng(latitude, longitude);

            mMap.setCenter(myLocation);

            addMaker(myLocation);
        }

        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapUpHelper(ILatLng iLatLng) {
                addMaker(new LatLng(iLatLng.getLatitude(), iLatLng.getLongitude()));
                //Toast.makeText(getApplicationContext(), "Single tap " + iLatLng, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean longPressHelper(ILatLng iLatLng) {
                //Toast.makeText(getApplicationContext(), "Long press", Toast.LENGTH_SHORT).show();
                return false;
            }
        };

        mMap.addOverlay(new MapEventsOverlay(getApplicationContext(), mapEventsReceiver));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addnewpoint, menu);
        return true;
    }


    private void addMaker(LatLng position) {

        //if (getChoosedLocation() != null)
        //    setChoosedLocation(null);
        if (choosedLocation != null)
            choosedLocation.setPoint(position);
        else {
            setChoosedLocation(new Marker(mMap, "", "", position));
            getChoosedLocation().setIcon(new Icon(getApplicationContext(), Icon.Size.LARGE, "marker-stroked", "000000"));
            mMap.addMarker(getChoosedLocation());
        }
    }












    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent = new Intent();

        if (id == R.id.action_done){
            //TODO: handler
            setResult(Const.INTENT_RESULT_CODE_OK, intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO: handler
    }
}
