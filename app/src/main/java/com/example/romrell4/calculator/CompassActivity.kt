package com.example.romrell4.calculator

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.TextView

class CompassActivity : AppCompatActivity(), LocationListener {
    var latitude: TextView? = null
    var longitude: TextView? = null
    var altitude: TextView? = null
    var accuracy: TextView? = null
    var speed: TextView? = null
    var provider: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass)

        latitude = findViewById(R.id.latitude)
        longitude = findViewById(R.id.longitude)
        altitude = findViewById(R.id.altitude)
        accuracy = findViewById(R.id.accuracy)
        speed = findViewById(R.id.speed)
        provider = findViewById(R.id.provider)

        val locationService = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationService.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
        }
    }

    override fun onLocationChanged(location: Location?) {
        latitude?.text = location?.latitude.toString()
        longitude?.text = location?.longitude.toString()
        altitude?.text = getString(R.string.altitude_format, location?.altitude.toString())
        accuracy?.text = getString(R.string.accuracy_format, location?.accuracy.toString())
        speed?.text = getString(R.string.speed_format, location?.speed.toString())
        provider?.text = location?.provider
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String?) {}

    override fun onProviderDisabled(provider: String?) {}
}
