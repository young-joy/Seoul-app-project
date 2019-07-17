package edu.skku.jonadan.hangangmongttang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
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

public class MapActivity extends AppCompatActivity {

    @BindView(R.id.map_back_btn)
    ImageButton backBtn;
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.map_container)
    ConstraintLayout mapLayout;
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

    private final int DEFAULT_ZOOM_LEVEL = 3;
    private final static ArrayList<Location> PARK_LIST = new ArrayList<>(
            Arrays.asList(
                    new Location(0, "광나루 한강공원", 37.548844, 127.120029),
                    new Location(1, "잠실 한강공원", 37.517993, 127.081944),
                    new Location(2, "뚝섬 한강공원", 37.529422, 127.073980),
                    new Location(3, "잠원 한강공원", 37.520729, 127.012251),
                    new Location(4, "반포 한강공원", 37.509815, 126.994755),
                    new Location(5, "이촌 한강공원", 37.516026, 126.975832),
                    new Location(6, "여의도 한강공원", 37.526461, 126.933682),
                    new Location(7, "망원 한강공원", 37.555045, 126.895960),
                    new Location(8, "난지 한강공원", 37.566202, 126.876319),
                    new Location(9, "강서 한강공원", 37.588136, 126.815235),
                    new Location(10, "양화 한강공원", 37.538334, 126.902265)
            )
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        initFabs();
        initMap(PARK_LIST.get(0));

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
        setMarker(PARK_LIST);
    }

    private void setMap(Location location) {
        double latitude, longitude;
        latitude = location.getLat();
        longitude = location.getLng();
        mapView.setMapCenterPointAndZoomLevel(
                MapPoint.mapPointWithGeoCoord(latitude, longitude), DEFAULT_ZOOM_LEVEL, true);
    }

    private void setMarker(ArrayList<Location> locationArrayList) {
        double latitude, longitude;
        for (Location location : locationArrayList) {
            latitude = location.getLat();
            longitude = location.getLng();
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(location.getName());
            marker.setTag(location.getObjectId());
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

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

        fabList.add(menuToiletBtn);
        fabList.add(menuShopBtn);
        fabList.add(menuWaterBtn);
        fabList.add(menuEntertainBtn);
        fabList.add(menuAthleticBtn);

        constraintSet.clone(mapLayout);
        for (FloatingActionButton fab : fabList) {
            constraintSet.connect(
                    fab.getId(), ConstraintSet.BOTTOM, menuBtn.getId(), ConstraintSet.BOTTOM);
        }
    }
}
