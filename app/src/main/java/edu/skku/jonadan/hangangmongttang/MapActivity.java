package edu.skku.jonadan.hangangmongttang;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindAnim;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity {

    @BindView(R.id.map_back_btn)
    ImageButton backBtn;
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.map_container)
    ConstraintLayout mapLayout;
    @BindView(R.id.map_fab_parking)
    FloatingActionButton menuParkingBtn;
    @BindView(R.id.map_fab_toilet)
    FloatingActionButton menuToiletBtn;
    @BindView(R.id.map_fab_shop)
    FloatingActionButton menuShopBtn;
    @BindView(R.id.map_fab_water)
    FloatingActionButton menuWaterBtn;
    @BindView(R.id.map_fab_entertain)
    FloatingActionButton menuEntertainBtn;
    @BindView(R.id.map_fab_athletic)
    FloatingActionButton menuAthleticBtn;
    @BindView(R.id.map_fab_menu)
    FloatingActionButton menuBtn;

    @BindAnim(R.anim.fab_open)
    Animation fabOpen;
    @BindAnim(R.anim.fab_close)
    Animation fabClose;

    @BindDimen(R.dimen.fab_margin) int fabMargin;

    private MapView.MapViewEventListener mapEventListener;
    private MapView.POIItemEventListener markerListener;
    private ArrayList<MapPOIItem> markerList;
    private CalloutBalloonAdapter balloonAdapter;

    private ArrayList<FloatingActionButton> fabList;
    private ConstraintSet constraintSet;
    private Boolean isFabOpen = false;

    private ArrayList<ParkingLot> parkingLotList;
    private ArrayList<Location> toiletList;
    private ArrayList<Location> shopList;
    private ArrayList<Location> waterList;
    private ArrayList<Location> entertainList;
    private ArrayList<Location> athleticList;

    private SeoulApiProvider apiProvider;
    private ArrayList<Callback<SeoulApiResult>> callbacks;

    private int selectedParkId;
    private Location refLocation;

    private final int DEFAULT_ZOOM_LEVEL = 3;
    private final double MARKING_SCOPE = 1.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        // Get from intent
        selectedParkId = ConstantPark.HANGANG_PARKS.GWANGNARU.ordinal();
        refLocation = ConstantPark.PARK_LIST.get(selectedParkId);

        initFabs();
        initMap(refLocation);

        apiProvider = new SeoulApiProvider();
        initCallbacks();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFabOpen) {
                    for (FloatingActionButton fab : fabList) {
                        fab.startAnimation(fabClose);
                        constraintSet.connect(
                                fab.getId(), ConstraintSet.BOTTOM,
                                menuBtn.getId(), ConstraintSet.BOTTOM);
                        constraintSet.setMargin(fab.getId(), ConstraintSet.BOTTOM, 0);
                        fab.setClickable(false);
                    }
                } else {
                    for (int i = fabList.size()-1; i >= 0; i--) {
                        fabList.get(i).startAnimation(fabOpen);
                        if (i == fabList.size()-1) {
                            constraintSet.connect(
                                    fabList.get(i).getId(), ConstraintSet.BOTTOM,
                                    menuBtn.getId(), ConstraintSet.TOP);
                        } else {
                            constraintSet.connect(
                                    fabList.get(i).getId(), ConstraintSet.BOTTOM,
                                    fabList.get(i + 1).getId(), ConstraintSet.TOP);
                        }
                        constraintSet.setMargin(
                                fabList.get(i).getId(), ConstraintSet.BOTTOM, fabMargin);
                        fabList.get(i).setClickable(true);
                    }
                }
                AutoTransition transition = new AutoTransition();
                transition.setDuration(300);
                transition.setInterpolator(new AccelerateDecelerateInterpolator());
                TransitionManager.beginDelayedTransition(mapLayout, transition);
                constraintSet.applyTo(mapLayout);

                isFabOpen = !isFabOpen;
            }
        });
    }

    private void initMap(Location location) {
        setMap(location);
        mapEventListener = new MapView.MapViewEventListener() {
            @Override
            public void onMapViewInitialized(MapView mapView) {

            }

            @Override
            public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

            }

            @Override
            public void onMapViewZoomLevelChanged(MapView mapView, int i) {

            }

            @Override
            public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

            }

            @Override
            public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

            }

            @Override
            public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

            }

            @Override
            public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

            }

            @Override
            public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

            }

            @Override
            public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

            }
        };
        mapView.setMapViewEventListener(mapEventListener);

        markerListener = new MapView.POIItemEventListener() {
            @Override
            public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

            }

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
                int id = mapPOIItem.getTag();
                Intent intent = new Intent(MapActivity.this, InfoActivity.class);
                intent.putExtra("location_id", id);
                startActivity(intent);
            }

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

            }

            @Override
            public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

            }
        };
        mapView.setPOIItemEventListener(markerListener);

        balloonAdapter = new CustomBalloonAdapter(this);
        mapView.setCalloutBalloonAdapter(balloonAdapter);

        markerList = new ArrayList<>();
        setMarker(ConstantPark.PARK_LIST);
    }

    private void setMap(Location location) {
        double latitude, longitude;
        latitude = location.getLat();
        longitude = location.getLng();
        mapView.setMapCenterPointAndZoomLevel(
                MapPoint.mapPointWithGeoCoord(latitude, longitude), DEFAULT_ZOOM_LEVEL, true);
    }

    private <T extends Location> void setMarker(ArrayList<T> locationArrayList) {
        double latitude, longitude;
        removeAllMarkers();
        for (Location location : locationArrayList) {
            latitude = location.getLat();
            longitude = location.getLng();
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(location.getName());
            marker.setTag(location.getObjectId());
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

//            For custom marker
//
//            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
//            marker.setCustomImageResourceId();
//            marker.setCustomImageAutoscale(false);
//            marker.setCustomImageAnchor();

            markerList.add(marker);
            mapView.addPOIItem(marker);
        }
    }

    private void removeAllMarkers() {
        for (MapPOIItem marker : markerList) {
            mapView.removePOIItem(marker);
        }
        markerList.clear();
    }

    private void initFabs() {
        fabList = new ArrayList<>();
        constraintSet = new ConstraintSet();

        fabList.add(menuParkingBtn);
        fabList.add(menuToiletBtn);
        fabList.add(menuShopBtn);
        fabList.add(menuWaterBtn);
        fabList.add(menuEntertainBtn);
        fabList.add(menuAthleticBtn);

        toiletList = new ArrayList<>();

        constraintSet.clone(mapLayout);
        for (FloatingActionButton fab : fabList) {
            constraintSet.connect(
                    fab.getId(), ConstraintSet.BOTTOM, menuBtn.getId(), ConstraintSet.BOTTOM);
        }

        menuParkingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parkingLotList = ConstantPark.PARK_LIST.get(selectedParkId).getParkingLots();
                setMarker(parkingLotList);
            }
        });

        menuToiletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 6; i++) {
                    Call<SeoulApiResult> call = apiProvider.callToilet(1000*i + 1);
                    call.enqueue(callbacks.get(0));
                }
            }
        });

        menuShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<SeoulApiResult> call = apiProvider.callShop();
                call.enqueue(callbacks.get(1));
            }
        });

        menuWaterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<SeoulApiResult> call = apiProvider.callWater();
                call.enqueue(callbacks.get(2));
            }
        });
    }

    private void initCallbacks() {
        callbacks = new ArrayList<>();
        callbacks.add(new Callback<SeoulApiResult>() {
            @Override
            public void onResponse(Call<SeoulApiResult> call, Response<SeoulApiResult> response) {
                if (!response.isSuccessful()) {
                    Log.d("Callback", "Response fail");
                    return;
                }
                SeoulApiResult result = response.body();
                ArrayList<Location> toilets = new ArrayList<Location>(result.getRow());
                for (Location toilet: toilets) {
                    if (getDistance(refLocation, toilet) < MARKING_SCOPE) {
                        toiletList.add(toilet);
                    }
                }
                setMarker(toiletList);
            }

            @Override
            public void onFailure(Call<SeoulApiResult> call, Throwable t) {
                Log.d("Callback", "" + t);
            }
        });
        callbacks.add(new Callback<SeoulApiResult>() {
            @Override
            public void onResponse(Call<SeoulApiResult> call, Response<SeoulApiResult> response) {
                if (!response.isSuccessful()) {
                    Log.d("Callback", "Response fail");
                    return;
                }
                SeoulApiResult result = response.body();
                shopList = new ArrayList<Location>(result.getRow());
                setMarker(shopList);
            }

            @Override
            public void onFailure(Call<SeoulApiResult> call, Throwable t) {
                Log.d("Callback", "" + t);
            }
        });
        callbacks.add(new Callback<SeoulApiResult>() {
            @Override
            public void onResponse(Call<SeoulApiResult> call, Response<SeoulApiResult> response) {
                if (!response.isSuccessful()) {
                    Log.d("Callback", "Response fail");
                    return;
                }
                SeoulApiResult result = response.body();
                waterList = new ArrayList<Location>(result.getRow());
                setMarker(waterList);
            }

            @Override
            public void onFailure(Call<SeoulApiResult> call, Throwable t) {
                Log.d("Callback", "" + t);
            }
        });
    }

    // Get distance using Haversine formula
    private double getDistance(Location refPoint, Location targetPoint) {
        int R = 6371;
        
        double dLat = deg2rad(refPoint.getLat() - targetPoint.getLat());
        double dLng = deg2rad(refPoint.getLng() - targetPoint.getLng());
        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(deg2rad(refPoint.getLat())) * Math.cos(deg2rad(targetPoint.getLat()))
                                                       * Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c;

        return distance;
    }

    private double deg2rad(double degree) {
        return degree * Math.PI / 180;
    }
}
