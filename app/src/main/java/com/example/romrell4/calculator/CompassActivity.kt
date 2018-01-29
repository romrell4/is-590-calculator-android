package com.example.romrell4.calculator

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_compass.*

private const val LOCATION_REQUEST_CODE = 1

class CompassActivity : AppCompatActivity() {
    private var adapter = InfoRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        //This will add the "Loading" messages
        adapter.updateLocation(null)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
        } else {
            requestLocationUpdates()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        val request = LocationRequest()
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        request.interval = 5000
        request.fastestInterval = 1000
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(request, object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                adapter.updateLocation(p0?.locations?.last())
            }
        }, null)
    }

    inner class InfoRecyclerAdapter : RecyclerView.Adapter<InfoRecyclerAdapter.InfoViewHolder>() {
        private var list: List<Pair<String, String?>> = emptyList()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        fun updateLocation(location: Location?) {
            list = listOf(
                    Pair(getString(R.string.latitude_label), location?.latitude?.toString() ?: getString(R.string.loading)),
                    Pair(getString(R.string.longitude_label), location?.longitude?.toString() ?: getString(R.string.loading)),
                    Pair(getString(R.string.altitude_label), if (location != null) getString(R.string.altitude_format, location.altitude.toString()) else getString(R.string.loading)),
                    Pair(getString(R.string.accuracy_label), if (location != null) getString(R.string.accuracy_format, location.accuracy.toString()) else getString(R.string.loading)),
                    Pair(getString(R.string.speed_label), if (location != null) getString(R.string.speed_format, location.speed.toString()) else getString(R.string.loading)),
                    Pair(getString(R.string.provider_label), location?.provider ?: getString(R.string.loading))
            )
        }

        override fun getItemCount() = list.size

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): InfoViewHolder {
            return InfoViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.list_item, parent, false))
        }

        override fun onBindViewHolder(holder: InfoViewHolder?, position: Int) {
            holder?.bind(list.get(position))
        }

        inner class InfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val textView = view.findViewById<TextView>(R.id.textView)
            private val detailTextView = view.findViewById<TextView>(R.id.detailTextView)

            fun bind(pair: Pair<String, String?>) {
                textView.text = pair.first
                detailTextView.text = pair.second
            }
        }
    }
}
