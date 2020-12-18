package projetbe.romelemma

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class ChoiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice)

        val logo_mypharma = findViewById<ImageView>(R.id.logo_my_pharma)
        val logo_shop = findViewById<ImageView>(R.id.logo_e_shop)

        val btt2 = AnimationUtils.loadAnimation(this, R.anim.btt2)
        val layout = findViewById<ConstraintLayout>(R.id.layout)
        layout.startAnimation(btt2)

        logo_mypharma.setOnClickListener {
            intent = Intent(this@ChoiceActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        logo_shop.setOnClickListener {
            // Shop Emma
        }

    }
}