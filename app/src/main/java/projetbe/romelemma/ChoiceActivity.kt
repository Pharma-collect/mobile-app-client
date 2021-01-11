package projetbe.romelemma

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import projetbe.romelemma.ui.shop.AccueilShopFragment

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
            //shop.visibility = View.INVISIBLE;
            val fragment1: Fragment =
                AccueilShopFragment();
            supportFragmentManager.beginTransaction().add(R.id.act_choice, fragment1, "1").commit()
        }

    }
}