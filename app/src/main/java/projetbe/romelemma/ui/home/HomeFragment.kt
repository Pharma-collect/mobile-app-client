package projetbe.romelemma.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import projetbe.romelemma.R
import projetbe.romelemma.ui.shop.AccueilShopFragment

class HomeFragment : Fragment(){

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.tvWelcomeBack)
        homeViewModel.welcomeText.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val button_shop: Button = root.findViewById(R.id.btnShop)

        button_shop.setOnClickListener {
            //shop.visibility = View.INVISIBLE;
            val fragment1: Fragment =
                AccueilShopFragment();
            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.fr_home, fragment1)
            transaction.commit()        }
        return root
    }
}