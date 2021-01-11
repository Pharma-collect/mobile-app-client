package projetbe.romelemma.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import projetbe.romelemma.R

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.profile_fragment, container, false)
        val tvName: TextView = root.findViewById(R.id.tvNameValue)
        val tvlastName: TextView = root.findViewById(R.id.tvLastNameValue)
        val tvMail: TextView = root.findViewById(R.id.tvMail)

        profileViewModel.name.observe(viewLifecycleOwner, Observer {
            tvName.text = it
        })
        profileViewModel.lastName.observe(viewLifecycleOwner, Observer {
            tvlastName.text = it
        })
        profileViewModel.mail.observe(viewLifecycleOwner, Observer {
            tvMail.text = it
        })
        return root
    }

}