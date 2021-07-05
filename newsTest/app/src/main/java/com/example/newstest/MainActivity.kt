package com.example.newstest

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.newstest.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions

class MainActivity : AppCompatActivity() {
    lateinit var  binding: ActivityMainBinding
    lateinit var googleMap: GoogleMap
    var loc = LatLng(37.554752,126.970631)
    val arrLoc = ArrayList<LatLng>()

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    var startupdate = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initMap()
        initSpinner()

    }

    private fun initSpinner() {
        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line)

        adapter.add("Hybrid")
        adapter.add("Normal")
        adapter.add("Satellite")
        adapter.add("Terrian")

        binding.apply {
            spinner.adapter = adapter

            spinner.setSelection(1)

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    when(position) {
                        0-> {
                            googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID

                        }
                        1-> {
                            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

                        }
                        2-> {
                            googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE

                        }
                        3-> {
                            googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN

                        }



                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }



    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdate()
    }

    private fun stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        startupdate = false
    }

    private fun initMap() {
        initLocation()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync {
            googleMap = it

//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,16.0f))
//            googleMap.setMinZoomPreference(10.0f)
//            googleMap.setMaxZoomPreference(18.0f)
//
//            val option = MarkerOptions()
//
//            option.position(loc)
//            option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//            option.title("역")
//            option.snippet("서울역")
//            val mk1 = googleMap.addMarker(option)
//
//            mk1.showInfoWindow()

            initMapListener()




        }
    }

    override fun onResume() {
        super.onResume()
        if(!startupdate)
            startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),100)
        else {  // 권한 허용상태
            if(!checkLoactionServicesStatus()) {
                showLocationServicesSetting()
            }
            else {
                startupdate = true
                fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
            }

            }


    }

    private fun showLocationServicesSetting() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다. \n "+
                "위치 설정을 허용하겠습니까?")
        builder.setPositiveButton("설정", DialogInterface.OnClickListener {
            dialog, id ->
            val GpsSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(GpsSettingIntent,1000)
        })
        builder.setNegativeButton("취소", DialogInterface.OnClickListener {
            dialog, id -> dialog.dismiss()
            setCurrentLocation(loc)
        })
        builder.create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            1000 -> {
                if(checkLoactionServicesStatus()) {
                     startLocationUpdates()
                }
            }
        }
    }

    private fun checkLoactionServicesStatus(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        //locationManager.isProviderEnabled((LocationManager.NETWORK_PROVIDER))
        return (locationManager.isProviderEnabled((LocationManager.GPS_PROVIDER)))
        //network_provider

    }

    private fun initLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // 우선순위 설정
        }

        locationCallback = object :LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                if(p0.locations.size==0) return

                loc = LatLng(p0.locations[p0.locations.size-1].latitude,
                        p0.locations[p0.locations.size-1].longitude)
                setCurrentLocation(loc)
            }
        }
    }

    private fun setCurrentLocation(loc: LatLng) {
        val option = MarkerOptions()
        option.position(loc)
        option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        googleMap.addMarker(option)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,16.0f))
    }

    private fun initMapListener() {
        googleMap.setOnMapClickListener {
            arrLoc.add(it)
            googleMap.clear()

            val options = MarkerOptions()
            options.position(it)
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

            googleMap.addMarker(options)

            //           val option2 = PolylineOptions().color(Color.GREEN).addAll(arrLoc)
//            googleMap.addPolyline(option2)

            //argb -> 투명도 지정 가능
            val option2 = PolygonOptions().fillColor(Color.argb(100,255,255,0))
                    .strokeColor(Color.GREEN).addAll(arrLoc)
            googleMap.addPolygon(option2)


        }
    }


}