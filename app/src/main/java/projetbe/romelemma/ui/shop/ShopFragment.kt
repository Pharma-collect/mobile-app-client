package projetbe.romelemma.ui.shop

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import projetbe.romelemma.R
import projetbe.romelemma.ui.prescriptions.PrescriptionsViewModel

class ShopFragment : Fragment() {

    private lateinit var shopViewModel: ShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        shopViewModel =
            ViewModelProvider(this).get(ShopViewModel::class.java)
        val root = inflater.inflate(R.layout.shop_fragment, container, false)
        val textView: TextView = root.findViewById(R.id.tvHelloShop)
        shopViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

}