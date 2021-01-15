package projetbe.romelemma

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_login.*
import projetbe.romelemma.dataClass.User
import projetbe.romelemma.repository.MyRepository
import projetbe.romelemma.services.EnableHttps.handleSSLHandshake


class LoginActivity : AppCompatActivity() {

    val user = User()

    val repo: MyRepository = MyRepository()

    val REQUEST_EXTERNAL_STORAGE: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btt = AnimationUtils.loadAnimation(this, R.anim.btt2)
        val layout = findViewById<ConstraintLayout>(R.id.layout)
        layout.startAnimation(btt)
        verifyStoragePermissions(this)
    }

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

    fun onValidateClicked(view: View) {
        handleSSLHandshake()
        repo.logRequest(etMail.text.toString(), etPassword.text.toString(), this)
    }

    fun onSignUpClick(view: View){
        intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun onTestClicked(view: View){
        Toast.makeText(
            this,
            "button Clicked",
            Toast.LENGTH_LONG
        ).show()
        handleSSLHandshake()
        repo.pushPhoto(user, "/storage/self/primary/DCIM/Restored/FB_IMG_1505473671837.jpg", this)
    }

    fun verifyStoragePermissions(activity: Activity?) {
        val permission = ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }
}