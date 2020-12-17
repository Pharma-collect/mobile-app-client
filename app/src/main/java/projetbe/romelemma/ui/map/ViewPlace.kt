package projetbe.romelemma.ui.map

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_view_place.*
import projetbe.romelemma.R
import projetbe.romelemma.dataClass.PlaceDetail
import projetbe.romelemma.services.GoogleApiService
import projetbe.romelemma.services.IGoogleApiService
import retrofit2.Call
import retrofit2.Response

class ViewPlace : AppCompatActivity() {

    internal lateinit var mService: IGoogleApiService
    var mPlace : PlaceDetail?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_place)

        mService = GoogleApiService.googleApiService

        place_name.text = ""
        place_address.text = ""
        place_hours.text = ""


        btn_show_on_map.setOnClickListener {
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mPlace!!.result!!.url))
            startActivity(mapIntent)
        }


        if(GoogleApiService.currentResult!!.photos != null && GoogleApiService.currentResult!!.photos!!.isNotEmpty()){
            Picasso.get()
                .load(getPhotoOfPlace(GoogleApiService.currentResult!!.photos!![0].photo_reference!!, 1000))
                .into(photo)
        }

        if(GoogleApiService.currentResult!!.rating != null){
            rating_bar.rating = GoogleApiService.currentResult!!.rating.toFloat()
        }else{
            rating_bar.visibility= View.GONE
        }

        if(GoogleApiService.currentResult!!.opening_hours != null){
            place_hours.text = "Open now : " + GoogleApiService.currentResult!!.opening_hours!!.open_now
        }else{
            place_hours.visibility = View.GONE
        }

        mService.getDetailPlaces(getDetailPlaceUrl(GoogleApiService.currentResult!!.place_id!!))
            .enqueue(object: retrofit2.Callback<PlaceDetail>{
                override fun onFailure(call: Call<PlaceDetail>, t: Throwable) {
                    Toast.makeText(baseContext,"" + t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<PlaceDetail>, response: Response<PlaceDetail>) {
                    mPlace = response.body()
                    place_address.text = mPlace!!.result!!.formatted_address
                    place_name.text = mPlace!!.result!!.name
                }
            })
    }

    private fun getDetailPlaceUrl(placeId: String): String {
        val url = StringBuilder("https://maps.googleapis.com/maps/api/place/details/json")
        url.append("?place_id=$placeId")
        url.append("&key=AIzaSyAOgLYcZxKFUyjrFvv58zNg6_AViWAFwpc")
        return url.toString()
    }

    private fun getPhotoOfPlace(photo_reference: String, maxWidth: Int): String {
        val url = StringBuilder("https://maps.googleapis.com/maps/api/place/photo")
        url.append("?maxwidth=$maxWidth")
        url.append("&photoreference=$photo_reference")
        url.append("&key=AIzaSyAOgLYcZxKFUyjrFvv58zNg6_AViWAFwpc")
        return url.toString()
    }
}