package projetbe.romelemma.services

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import projetbe.romelemma.data.User

 class MyRepository {

    fun getUserInformations(
        id: String,
        context: Context,
        userData: User
    ){
        val requestQueue = Volley.newRequestQueue(context)
        val url = "https://88-122-235-110.traefik.me:61001/api/user_client/getClientById"
        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.GET, url, object : Response.Listener<String> {
                override fun onResponse(response: String) {
                    Log.d("RequestSuccessfull", response)
                    var jsonResponse: JSONObject = JSONObject(response)
                    try {
                        userData.name = jsonResponse.getString("name")
                        userData.lastname = jsonResponse.getString("lastname")
                        userData.username = jsonResponse.getString("username")
                    } catch (e: JSONException) {
                        e.printStackTrace();
                    }
                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    Log.d("RequestError", error.toString())
                }
            }) {
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Host"] = "node"
                    return params
                }
                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = java.util.HashMap()
                    params["id"] = id
                    return params
                }
            }
        requestQueue.add(stringRequest)
    }

}

