package projetbe.romelemma

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import projetbe.romelemma.services.EnableHttps.handleSSLHandshake
import org.json.JSONObject
import projetbe.romelemma.services.MyRepository

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    private val repository: MyRepository = MyRepository()

    private fun forgotPassword(username: EditText){
        if (username.text.toString().isEmpty()){
            Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()){
            Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()
            return
        }
    }

    private fun logRequest(
            username: String,
            password: String
    ){
        val requestQueue = Volley.newRequestQueue(this)
        val url = "https://88-122-235-110.traefik.me:61001/api/user_client/loginClient"
        val stringRequest: StringRequest =
                object : StringRequest(Request.Method.POST, url, object : Response.Listener<String?> {
                    override fun onResponse(response: String?) {
                        var jsonSuccess = JSONObject(response)
                        var jsonResponse = JSONObject(jsonSuccess.get("result").toString())
                        if (jsonSuccess["success"] == true) {
                            Log.d("Request Response", jsonSuccess.toString())
                            Log.d("Request Response", jsonResponse.toString())
                            intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("id", jsonResponse["id"].toString())
                            startActivity(intent)
                        }else{
                            Toast.makeText(this@LoginActivity, "Wrong username or password", Toast.LENGTH_LONG).show()
                        }
                    }
                }, object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        Toast.makeText(this@LoginActivity, error.toString(), Toast.LENGTH_LONG)
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

    fun onValidateClicked(view: View) {
        handleSSLHandshake()
        logRequest(etMail.text.toString(), etPassword.text.toString())
    }

    fun onSignUpClick(view: View){
        intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}