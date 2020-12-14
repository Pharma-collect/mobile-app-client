package projetbe.romelemma.ui.prescriptions

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import projetbe.romelemma.R
import projetbe.romelemma.ui.map.MapViewModel

class PrescriptionsFragment : Fragment() {

    private lateinit var prescriptionsViewModel: PrescriptionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prescriptionsViewModel =
            ViewModelProvider(this).get(PrescriptionsViewModel::class.java)
        val root = inflater.inflate(R.layout.prescriptions_fragment, container, false)
        val textView: TextView = root.findViewById(R.id.tvHelloPrescription)
        prescriptionsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }


}