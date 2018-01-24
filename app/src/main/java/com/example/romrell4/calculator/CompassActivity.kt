package com.example.romrell4.calculator

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.android.gms.location.*

class CompassActivity : AppCompatActivity() {
    private var latitude: TextView? = null
    private var longitude: TextView? = null
    private var altitude: TextView? = null
    private var accuracy: TextView? = null
    private var speed: TextView? = null
    private var provider: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass)

        latitude = findViewById(R.id.latitude)
        longitude = findViewById(R.id.longitude)
        altitude = findViewById(R.id.altitude)
        accuracy = findViewById(R.id.accuracy)
        speed = findViewById(R.id.speed)
        provider = findViewById(R.id.provider)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        val request = LocationRequest()
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        request.interval = 5000
        request.fastestInterval = 1000
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(request, object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                print(p0?.locations)
                onLocationChanged(p0?.locations?.last())
            }
        }, null)
    }

    fun onLocationChanged(location: Location?) {
        latitude?.text = location?.latitude.toString()
        longitude?.text = location?.longitude.toString()
        altitude?.text = getString(R.string.altitude_format, location?.altitude.toString())
        accuracy?.text = getString(R.string.accuracy_format, location?.accuracy.toString())
        speed?.text = getString(R.string.speed_format, location?.speed.toString())
        provider?.text = location?.provider
    }

}
