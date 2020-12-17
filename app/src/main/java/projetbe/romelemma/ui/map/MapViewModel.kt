package projetbe.romelemma.ui.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Map Fragment"

    }
}