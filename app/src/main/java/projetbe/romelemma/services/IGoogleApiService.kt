package projetbe.romelemma.services

import projetbe.romelemma.dataClass.PlaceDetail
import projetbe.romelemma.dataClass.RootObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

public interface IGoogleApiService {
    @GET
    fun getNearbyPlaces(@Url url:String): Call<RootObject>

    @GET
    fun getDetailPlaces(@Url url:String): Call<PlaceDetail>

}