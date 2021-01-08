package projetbe.romelemma

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_login.*
import projetbe.romelemma.repository.MyRepository
import projetbe.romelemma.services.EnableHttps.handleSSLHandshake

class LoginActivity : AppCompatActivity() {

    val repo: MyRepository = MyRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btt = AnimationUtils.loadAnimation(this, R.anim.btt2)
        val layout = findViewById<ConstraintLayout>(R.id.layout)
        layout.startAnimation(btt)
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
}