package com.example.romrell4.calculator

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_compass2.*
import kotlinx.android.synthetic.main.list_item.*

class CompassActivity : AppCompatActivity() {
    private lateinit var adapter: InfoRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass2)

        val list = listOf(Pair("Latitude", "Test"))
        adapter = InfoRecyclerAdapter(list)

        recyclerView.adapter = adapter

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        adapter.list = listOf(
                Pair("Latitude", location?.latitude.toString()),
                Pair("Longitude", location?.longitude.toString())
        )

//        latitude.text = location?.latitude.toString()
//        longitude.text = location?.longitude.toString()
//        altitude.text = getString(R.string.altitude_format, location?.altitude.toString())
//        accuracy.text = getString(R.string.accuracy_format, location?.accuracy.toString())
//        speed.text = getString(R.string.speed_format, location?.speed.toString())
//        provider.text = location?.provider
    }

    inner class InfoRecyclerAdapter(list: List<Pair<String, String>>) : RecyclerView.Adapter<InfoRecyclerAdapter.InfoViewHolder>() {
        var list: List<Pair<String, String>> = list
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun getItemCount() = list.size

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): InfoViewHolder {
            return InfoViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.list_item, parent, false))
        }

        override fun onBindViewHolder(holder: InfoViewHolder?, position: Int) {
            holder?.bind(list.get(position))
        }

        inner class InfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            fun bind(pair: Pair<String, String>) {
                textView.text = pair.first
                detailTextView.text = pair.second
            }
        }
    }
}
