package projetbe.romelemma.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.profile_fragment.view.*
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
        val tvUsername = root.tvUsername
        val tvFirstNameValue = root.tvFirstNameValue
        val tvLastNameValue = root.tvLastNameValue
        val tvMailValue = root.tvEmailValue
        val tvPhoneValue = root.tvPhoneValue

        profileViewModel.username.observe(viewLifecycleOwner, Observer {
            tvUsername.text = it
        })
        profileViewModel.name.observe(viewLifecycleOwner, Observer {
            tvFirstNameValue.text = it
        })
        profileViewModel.lastname.observe(viewLifecycleOwner, Observer {
            tvLastNameValue.text = it
        })
        profileViewModel.mail.observe(viewLifecycleOwner, Observer {
            tvMailValue.text = it
        })
        profileViewModel.phoneNumber.observe(viewLifecycleOwner, Observer {
            tvPhoneValue.text = it
        })

        return root
    }

}