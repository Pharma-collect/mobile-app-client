package projetbe.romelemma

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    var charSequence: CharSequence? = null
    var index = 0
    var delay: Long = 200
    var handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Get windows size fully fill
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Switching Activity at the end
        Handler().postDelayed({
            startActivity(
                Intent(this@SplashActivity, ChoiceActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
//            customType(this@SplashActivity, "bottom-to-up")
            finish()
        }, 4000)
    }

    var runnable: Runnable = object : Runnable {
        override fun run() {
            if (index < charSequence!!.length) {
                handler.postDelayed(this, delay)
            }
        }
    }
}