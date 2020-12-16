package projetbe.romelemma.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import projetbe.romelemma.R
import projetbe.romelemma.dataClass.RootObject
import projetbe.romelemma.services.IGoogleApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Map Fragment"

    }

    val text: LiveData<String> = _text
    private lateinit var mMap: GoogleMap

    private var latitude: Double=0.toDouble()
    private var longitude: Double=0.toDouble()

    lateinit var mService: IGoogleApiService

    internal lateinit var currentPlace:RootObject




}