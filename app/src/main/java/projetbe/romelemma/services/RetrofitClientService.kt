package projetbe.romelemma.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientService {
    private var retrofit:Retrofit?=null

    fun getClient(baseUrll:String):Retrofit{
        if (retrofit == null){
            retrofit=Retrofit.Builder().baseUrl(baseUrll).addConverterFactory(GsonConverterFactory.create()).build()
        }
        return retrofit!!
    }
}
