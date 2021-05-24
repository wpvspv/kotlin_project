package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myapplication.databinding.ActivityMainBinding
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


val PERMISSIONS_REQUEST_CODE = 100
var REQUIRED_PERMISSIONS = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION)

class MainActivity : AppCompatActivity() {
    //private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main) as ActivityMainBinding

        val mapView = MapView(this)
        binding.mapContainer.addView(mapView)

        binding.mapPageLocationBtn.setOnClickListener {
            val permissionCheck =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                val lm: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
                try {
                    val userNowLocation: Location =
                        lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    val uLatitude = userNowLocation.latitude
                    val uLongitude = userNowLocation.longitude
                    val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)
                    mapView.setMapCenterPoint(uNowPosition, true)
                } catch (e: NullPointerException) {
                    Log.e("LOCATION_ERROR", e.toString())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ActivityCompat.finishAffinity(this)
                    } else {
                        ActivityCompat.finishAffinity(this)
                    }

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    System.exit(0)
                }
            } else {
                Toast.makeText(this, "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }

    }
}