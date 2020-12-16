package projetbe.romelemma.ui.map

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.map_fragment.*
import projetbe.romelemma.R
import projetbe.romelemma.dataClass.RootObject
import projetbe.romelemma.services.GoogleApiService
import projetbe.romelemma.services.IGoogleApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapFragment : Fragment(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap
    private lateinit var mLastLocation: Location

    private var latitude: Double=0.toDouble()
    private var longitude: Double=0.toDouble()

    private var mMarker: Marker?=null

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    lateinit var mService: IGoogleApiService

    internal lateinit var currentPlace:RootObject

    private lateinit var mapViewModel: MapViewModel
    private var mapReady = false



    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapViewModel =
            ViewModelProvider(this).get(MapViewModel::class.java)
        val root = inflater.inflate(R.layout.map_fragment, container, false)


        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mService = GoogleApiService.googleApiService

        buildLocationRequest()
        buildLocationCallback()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

        nearbyPlace()

        return root
    }

    fun nearbyPlace(){
        //mMap.clear()
        val url = getUrl(latitude, longitude)
        Log.d("URL", url)
        mService.getNearbyPlaces(url)
            .enqueue(object : Callback<RootObject> {
                override fun onResponse(call: Call<RootObject>?, response: Response<RootObject>?) {
                    currentPlace = response!!.body()!!
                    if(response!!.isSuccessful){
                        for(i in 0 until response.body()!!.results!!.size){
                            val markerOptions = MarkerOptions()
                            val googlePlace = response.body()!!.results!![i]
                            val lat = googlePlace.geometry!!.location!!.lat
                            val lng = googlePlace.geometry!!.location!!.lng
                            val placeName = googlePlace.name
                            val latLng = LatLng(lat, lng)

                            markerOptions.position(latLng)
                            markerOptions.title(placeName)
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            markerOptions.snippet(i.toString())

                            mMap.addMarker(markerOptions)
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(13f))
                        }
                    }
                }

                override fun onFailure(call: Call<RootObject>, t: Throwable) {
                }
            })
    }

    fun getUrl(latitude: Double, longitude: Double): String {
        Log.d("Latitude getUrl", latitude.toString())
        Log.d("Longitude getUrl", longitude.toString())
        var googlePlaceUrl = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
        googlePlaceUrl.append("?location=$latitude,$longitude")
        googlePlaceUrl.append("&radius=10000&type=pharmacy")
        googlePlaceUrl.append("&key=AIzaSyAOgLYcZxKFUyjrFvv58zNg6_AViWAFwpc")
        return googlePlaceUrl.toString()
    }

    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                mLastLocation = p0!!.locations[p0.locations.size-1] //Last location

                if(mMarker != null){
                    mMarker!!.remove()
                }

                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude

                val latLng = LatLng(latitude,longitude)
                val markerOptions = MarkerOptions().position(latLng).title("Your position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                mMarker = mMap.addMarker(markerOptions)

                Log.d("Latitude buildLocation", latitude.toString())
                Log.d("Longitude buildLocation", longitude.toString())

                val url = getUrl(latitude, longitude)
                Log.d("URL", url)
                mService.getNearbyPlaces(url)
                    .enqueue(object : Callback<RootObject> {
                        override fun onResponse(call: Call<RootObject>?, response: Response<RootObject>?) {
                            currentPlace = response!!.body()!!
                            if(response!!.isSuccessful){
                                for(i in 0 until response.body()!!.results!!.size){
                                    val markerOptionss = MarkerOptions()
                                    val googlePlace = response.body()!!.results!![i]
                                    val lat = googlePlace.geometry!!.location!!.lat
                                    val lng = googlePlace.geometry!!.location!!.lng
                                    val placeName = googlePlace.name
                                    val latiLng = LatLng(lat, lng)

                                    markerOptionss.position(latiLng)
                                    markerOptionss.title(placeName)
                                    markerOptionss.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                    markerOptionss.snippet(i.toString())

                                    mMap.addMarker(markerOptionss)
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latiLng))
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13f))
                                }
                            }
                        }

                        override fun onFailure(call: Call<RootObject>, t: Throwable) {
                        }
                    })

                //Move camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
            }
        }
    }


    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 15f
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
    }

}