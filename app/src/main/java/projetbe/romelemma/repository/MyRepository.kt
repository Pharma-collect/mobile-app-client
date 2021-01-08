package projetbe.romelemma.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import projetbe.romelemma.MainActivity
import projetbe.romelemma.dataClass.User
import projetbe.romelemma.services.FileService

class MyRepository {

    private val fileService: FileService =
        FileService()

    fun getUserInformations(
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
                        userData.name = jsonResponse["name"].toString()
                        userData.lastname = jsonResponse["lastname"].toString()
                        userData.username = jsonResponse["username"].toString()
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
                    params["Authorization"] = userData.token.toString()
                    return params
                }
                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = java.util.HashMap()
                    params["id"] = userData.id.toString()
                    return params
                }
            }
        requestQueue.add(stringRequest)
    }

    fun logRequest(
        username: String,
        password: String,
        context: Context
    ){
        val requestQueue = Volley.newRequestQueue(context)
        val url = "https://88-122-235-110.traefik.me:61001/api/user_client/loginClient"
        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, url, object : Response.Listener<String?> {
                override fun onResponse(response: String?) {
                    var jsonSuccess = JSONObject(response)
                    var jsonResponse = JSONObject(jsonSuccess["result"].toString())
                    if (jsonSuccess["success"] == true) {
                        fileService.saveData(
                            jsonResponse["id"].toString(),
                            jsonResponse["name"].toString(),
                            jsonResponse["lastname"].toString(),
                            jsonResponse["username"].toString(),
                            jsonResponse["token"].toString(),
                            context
                        )
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    }else{
                        Toast.makeText(context, "Wrong username or password", Toast.LENGTH_LONG).show()
                    }
                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG)
                        .show()
                }
            }) {
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Host"] = "node"
                    return params
                }

                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["username"] = username
                    params["password"] = password
                    return params
                }
            }
        requestQueue.add(stringRequest)
    }

}