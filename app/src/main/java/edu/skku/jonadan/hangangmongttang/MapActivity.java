package edu.skku.jonadan.hangangmongttang;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import net.daum.mf.map.api.MapView;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.map_view);
    }
}
