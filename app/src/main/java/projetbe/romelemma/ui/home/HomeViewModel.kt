package projetbe.romelemma.ui.home

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import projetbe.romelemma.data.User
import projetbe.romelemma.services.MyRepository

class HomeViewModel(
        application: Application
) : AndroidViewModel(application), CoroutineScope by MainScope() {

    private val _welcomeText = MutableLiveData<String>()
    val welcomeText: LiveData<String> = _welcomeText

    var user: User = User()
    private val repository: MyRepository = MyRepository()
    private val context = getApplication<Application>().applicationContext

    init {
        launch{
            Log.d("HomeVM", "launched new Coroutine")
            repository.getUserInformations("38", context, user)
            _welcomeText.value = "Welcome Back ${user.username}"
            Log.d("HomeVM", "Welcome Back ${user.username}")
        }
    }

    fun onButtonClicked(){
        Log.d("HomeVM", "button was clicked")
    }
}