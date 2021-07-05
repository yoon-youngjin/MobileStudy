package com.example.mygmap

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.mygmap.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions

//implementation 'com.google.android.gms:play-services-location:18.0.0'-> fusedlocationproviderclient
// -> fusedlocationproviderclient 사용하면 사용자 위치 정보 활용가능
//    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
// -> gps를 사용해야지 위치정보를 가져올수 있는 권한
// implementation 'com.google.android.gms:play-services-maps:17.0.1' -> googlemap


//<fragment
//android:id="@+id/map"
//android:layout_width="match_parent"
//android:layout_height="match_parent"
//android:name = "com.google.android.gms.maps.SupportMapFragment"/>


class MainActivity : AppCompatActivity() /*,OnMapReadyCallback*/{
    lateinit var binding:ActivityMainBinding
    lateinit var googleMap:GoogleMap
    var loc = LatLng(37.554752,126.970631)
    val arrLoc = ArrayList<LatLng>()
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest:LocationRequest
    lateinit var locationCallback: LocationCallback
    var startupdate = false // 위치 정보 갱신 start 확인용

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initmap()
        initSpinner()
    }
//초기화
    fun initLocation() {
        //현재 위치 -> 최근 위치 접근 -> 현재 위치를 콜백으로 받음
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // 위치 요청 설정
        locationRequest = LocationRequest.create().apply {
            interval = 10000 // 10초 업데이트 수신 속도
           fastestInterval = 5000 // 가장 빠른 속도
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // 우선순위 설정
        }

        locationCallback = object : LocationCallback() {
            //위치 갱신 마다 함수 호출
            override fun onLocationResult(location: LocationResult) {
                if(location.locations.size==0) return // fail
                //locations.size-1 : 가장 마지막 추가된 위치 정보
                loc = LatLng(location.locations[location.locations.size-1].latitude,
                        location.locations[location.locations.size-1].longitude)
                //위치에 마커찍기
                setCurrentLocation(loc)
                Log.i("location","LocationCallback()")
            }
        }
    }
    private fun stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        startupdate = false
        Log.i("location","stopLocationUpdate()")
    }

    override fun onResume() {
        super.onResume()
        Log.i("location","onResume()")
        if(!startupdate)
            startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        Log.i("location","onPause()")
        stopLocationUpdate()
    }

    private fun setCurrentLocation(loc: LatLng) {
        val option = MarkerOptions()
        option.position(loc)
        option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        googleMap.addMarker(option)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,16.0f))
    }

    private fun initSpinner() {
        //초기화 및 이벤트 처리
        // spinner는 어댑터 필요

        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,ArrayList<String>())
        //layout에 array데이터 맵핑시켜 show

        //맵 타일 유형 4가지 추가
        adapter.add("Hybrid")
        adapter.add("Normal")
        adapter.add("Satellite")
        adapter.add("Terrian")

        binding.apply {
            spinner.adapter = adapter
            //스피너 초기갑 지정
            spinner.setSelection(1)

            //이벤트 처리
            spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    //지도 바꾸는 처리
                    when(position) {
                        0 -> {//hybrid
                            googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                        }
                        1 -> {
                            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                        }
                        2 -> {
                            googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                        }
                        3 -> {
                            googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }



    }
// lastLocation정보를 get하기 위해서는 권한정보 필요 -> 권한체크 작업
    fun getLastLocation() {
    // requesPermission -> onRequestPermissionsResult
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, ), 100)
        }
        else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                if(it!=null) {
                    loc = LatLng(it.latitude,it.longitude)
                    setCurrentLocation(loc)
                }
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==100) {
            // 승인
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates() //권한 허용된경우 위치정보 갱신작업
            }
            // 거부
            else {

                Toast.makeText(this,"위치정보를 제공하셔야 합니다.",Toast.LENGTH_SHORT).show()
                setCurrentLocation(loc) // default값
            }
        }
    }
//locationupdate 요청하는 기능담당 -> 권한체크 필요
    private fun startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100) }
        else {  // 권한 허용상태
            if(!checkLoactionServicesStatus()) { // 핸드폰 gps꺼진 상태
                showLocationServicesSetting()
            }
            else { // gps켜진상태
                startupdate = true
                // 위치 갱신 요청 ,(위치업데이트정보 , 위치 업데이트를 위한 콜백함수 , 콜백 메세지를 수신할 수 있는 메시지 큐를 가진 looper객체)
               fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,Looper.getMainLooper())
                Log.i("location","startLocationUpdates()")

            }
        }
    }

    private fun showLocationServicesSetting() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다. \n "+
        "위치 설정을 허용하겠습니까?")
        builder.setPositiveButton("설정",DialogInterface.OnClickListener {
            dialog, id ->
            val GpsSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(GpsSettingIntent,1000)
        })
        builder.setNegativeButton("취소",DialogInterface.OnClickListener {
            dialog, id -> dialog.dismiss()
            setCurrentLocation(loc)
        })
        builder.create().show()
    }

    // 설정 시 결과값 onActivityResult로 전달

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            1000 -> {
                if(checkLoactionServicesStatus()) {
                    Toast.makeText(this,"GPS 활성화 되었음",Toast.LENGTH_SHORT).show()
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

    private fun initmap() {
        initLocation()
        // findFragmentByid -> fragment반환 -> as 형변환
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
        // 맵은 비동기식 사용
        mapFragment.getMapAsync {
            // 바로 googlemap정보 in , onMapready함수 포함
            googleMap = it // googlemap초가화

//            위치로 이동 (위치, 줌레벨)
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,16.0f))
//            // 최소 크기 지정
//            googleMap.setMinZoomPreference(10.0f)
//            // 최대 크기 지정
//            googleMap.setMaxZoomPreference(18.0f)
//
//            val option = MarkerOptions()
//            option.position(loc)
//            option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//            //option.icon(BitmapDescriptorFactory.fromResource(R.drawable.common_full_open_on_phone))
//            option.title("역")
//            option.snippet("서울역")
//            val mk1 = googleMap.addMarker(option)
//
//            mk1.showInfoWindow()

            // 지도 클릭시 위도,경도 정보 get
            initMapListener()


        }
        //    override fun onMapReady(p0: GoogleMap) {
//        googleMap = p0 // 구글맵 초기화
//    }
    }

    private fun initMapListener() {
        googleMap.setOnMapClickListener {
            // 클릭 마다 위치 배열에 저장
            arrLoc.add(it)
            //googleMap의 그려놓은 정보 지움
            googleMap.clear()
            // 클릭시 마커 표시
            val option = MarkerOptions()
            option.position(it)
            option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            googleMap.addMarker(option)

            // 배열안에 들어있는 위도,경도정보 모두를 가지고 라인을 그림림
//           val option2 = PolylineOptions().color(Color.GREEN).addAll(arrLoc)
//            googleMap.addPolyline(option2)

            //argb -> 투명도 지정 가능
            val option2 = PolygonOptions().fillColor(Color.argb(100,255,255,0))
                .strokeColor(Color.GREEN).addAll(arrLoc)
            googleMap.addPolygon(option2)

        }
    }


}