package projetbe.romelemma.ui.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import projetbe.romelemma.dataClass.User
import projetbe.romelemma.services.FileService

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application), CoroutineScope by MainScope() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Profile Fragment"
    }
    val text: LiveData<String> = _text

    var user: User = User()
    val fileService: FileService = FileService()
    private val context = getApplication<Application>().applicationContext

    init {
        launch{
            user = fileService.getData(context)
            _text.value = "You are logged in as ${user.name} ${user.lastname}"
        }
    }
}