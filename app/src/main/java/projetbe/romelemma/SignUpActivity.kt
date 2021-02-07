package projetbe.romelemma

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import at.favre.lib.crypto.bcrypt.BCrypt
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import projetbe.romelemma.services.EnableHttps.handleSSLHandshake
import java.util.*
import java.util.regex.Pattern


class SignUpActivity : AppCompatActivity(), OnDateSetListener {

    private var prenom: EditText? = null
    private var nom: EditText? = null
    private var mail: EditText? = null
    private var phone: EditText? = null
    private var pswd: EditText? = null
    private var sConfirm: EditText? = null
    private var username: EditText? = null
    private lateinit var dateOfBirth: TextView
    private var str_firstname: String? = null
    private var str_lastname: String? = null
    private var str_dob: String? = null
    private var str_mail: String? = null
    private var str_phone: String? = null
    private var str_pswd: String? = null
    private var str_confirm: String? = null
    private var str_username: String? = null
    private var pattern: String = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$"
    var backUrl = "https://88-122-235-110.traefik.me:61001/api/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        prenom = findViewById<View>(R.id.prenom) as EditText
        nom = findViewById<View>(R.id.nom) as EditText
        dateOfBirth = findViewById(R.id.birth)
        mail = findViewById<View>(R.id.mail) as EditText
        phone = findViewById<View>(R.id.telephone) as EditText
        pswd = findViewById<View>(R.id.password) as EditText
        sConfirm = findViewById<View>(R.id.password_confirm) as EditText
        username = findViewById<View>(R.id.username) as EditText
        dateOfBirth.setText("Your date of birth")

        val btt = AnimationUtils.loadAnimation(this, R.anim.btt2)
        val formulaire = findViewById<ConstraintLayout>(R.id.layout)
        formulaire.startAnimation(btt)

        val signup = findViewById<Button>(R.id.button_test)
        val picker_view = findViewById<Button>(R.id.button_picker)
        picker_view.setOnClickListener { showDatePickerDialog() }
    }

    private fun checkField(
        s1: String,
        s2: String,
        s3: String,
        s4: String,
        s5: String,
        s6: String,
        s7: String,
        s8: String
    ) {
        var password_hash = ""
        if (s1 == "" || s2 == "" || s3 == "" || s4 == "" || s5 == "" || s6 == "" || s7 == "") {
            Toast.makeText(
                this@SignUpActivity,
                "Every Field must be fill ! ",
                Toast.LENGTH_LONG
            ).show()
        } else if (!Pattern.matches(pattern,s6)){
            Toast.makeText(
                this@SignUpActivity,
                "Password must contain : \n   * More than 8 characters \n   * At least one number \n   * At least one lower and upper character \n   * At least one special character \n   * No space or tab ",
                Toast.LENGTH_LONG
            ).show()
        } else if (s6 != s7) {
            Toast.makeText(
                this@SignUpActivity,
                "Password and its confirmation must be the same",
                Toast.LENGTH_LONG
            ).show()
        } else {
            password_hash = hash_bcrypt(s6)
            handleSSLHandshake()
            registerClient(s1, s2, s3, s4, s5, password_hash, s8)
        }
    }

    private fun hash_bcrypt(password: String): String {
        return BCrypt.withDefaults().hashToString(10, password.toCharArray())
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            this,
            Calendar.getInstance()[Calendar.YEAR],
            Calendar.getInstance()[Calendar.MONTH],
            Calendar.getInstance()[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val month_ok = month + 1
        val date = "$month_ok/$dayOfMonth/$year"
        dateOfBirth.text = date
    }

    private fun moveToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    //My request
    private fun registerClient(
        name: String,
        lastname: String,
        birth: String,
        mail: String,
        phone: String,
        pswd: String,
        username: String
    ) {
        val requestQueue = Volley.newRequestQueue(this)
        val url = backUrl + "user_client/registerClient"
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                var jsonResponse: JSONObject? = null
                var myObjAsString: String? = null
                try {
                    jsonResponse = JSONObject(response)
                    myObjAsString = jsonResponse.getString("success")
                    if (myObjAsString == "true") {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Account successfully created",
                            Toast.LENGTH_LONG
                        ).show()
                        moveToLogin()
                    } else {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Error : ${jsonResponse.getString("error")}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("CreateAccError",response)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                println("\n \n$error\n \n")
                Toast.makeText(
                    this@SignUpActivity,
                    "Failed to create account, please try again",
                    Toast.LENGTH_LONG
                ).show()
            }) {
            /*
             * Header of the request
             */
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Host"] = "node"
                return params
            }

            /*
             * Parameters of the request
             */
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["name"] = name
                params["lastname"] = lastname
                params["birth"] = birth
                params["mail"] = mail
                params["phone"] = phone
                params["password"] = pswd
                params["username"] = username
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    fun onSignUpClicked(view: View){
        str_firstname = prenom!!.text.toString()
        str_lastname = nom!!.text.toString()
        str_dob = dateOfBirth.getText().toString()
        str_mail = mail!!.text.toString()
        str_phone = phone!!.text.toString()
        str_pswd = pswd!!.text.toString()
        str_confirm = sConfirm!!.text.toString()
        str_username = username!!.text.toString()
        checkField(
            str_firstname!!,
            str_lastname!!,
            str_dob!!, str_mail!!, str_phone!!, str_pswd!!, str_confirm!!, str_username!!
        )
    }
}


