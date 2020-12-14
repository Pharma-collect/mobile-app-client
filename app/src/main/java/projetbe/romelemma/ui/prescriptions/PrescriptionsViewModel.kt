package projetbe.romelemma.ui.prescriptions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PrescriptionsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Prescription Fragment"
    }
    val text: LiveData<String> = _text
}