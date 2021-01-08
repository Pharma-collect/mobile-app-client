package projetbe.romelemma.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import projetbe.romelemma.dataClass.User
import projetbe.romelemma.repository.MyRepository
import projetbe.romelemma.services.FileService

class HomeViewModel(
        application: Application
) : AndroidViewModel(application), CoroutineScope by MainScope() {

    private val _welcomeText = MutableLiveData<String>()
    val welcomeText: LiveData<String> = _welcomeText

    var user: User = User()
    val fileService: FileService = FileService()
    private val context = getApplication<Application>().applicationContext

    init {
        launch{
            user = fileService.getData(context)
            _welcomeText.value = "Welcome Back ${user.name} ${user.lastname}"
            Log.d("HomeVM", "Welcome Back ${user.username}")
        }
    }

    fun onButtonClicked(){
        Log.d("HomeVM", "button was clicked")
    }
}