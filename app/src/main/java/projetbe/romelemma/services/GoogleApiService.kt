package projetbe.romelemma.services

import projetbe.romelemma.dataClass.Results

object GoogleApiService {
    private val GOOGLE_API_URL = "https://maps.googleapis.com/"

    var currentResult: Results?=null

    val googleApiService : IGoogleApiService
        get() = RetrofitClientService.getClient(GOOGLE_API_URL).create((IGoogleApiService::class.java))

}