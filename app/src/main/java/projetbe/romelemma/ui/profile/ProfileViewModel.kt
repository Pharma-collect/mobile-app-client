package projetbe.romelemma.ui.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import projetbe.romelemma.dataClass.User
import projetbe.romelemma.repository.MyRepository
import projetbe.romelemma.services.FileService

class ProfileViewModel(
        application: Application
) : AndroidViewModel(application), CoroutineScope by MainScope() {

    private val _mail = MutableLiveData<String>()
    val mail: LiveData<String> = _mail

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _lastname = MutableLiveData<String>()
    val lastname: LiveData<String> = _lastname

    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> = _phoneNumber

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    val repo: MyRepository = MyRepository()
    var user: User = User()
    val fileService: FileService = FileService()
    private val context = getApplication<Application>().applicationContext

    var result:MutableMap<String,String> = java.util.HashMap()

    init {
        launch {
            user = fileService.getData(context)
            result = repo.getUserInformations(context, user)
            while (result.isEmpty())
                delay(20)
            displayInfo(result)
        }
    }

    private fun displayInfo(mutableMap: MutableMap<String,String>) {
        Log.d("Profile", "$mutableMap")
        _username.value = "Hello ${mutableMap["username"]}"
        _name.value = mutableMap["name"]
        _lastname.value = mutableMap["lastname"]
        _mail.value = mutableMap["mail"]
        _phoneNumber.value = mutableMap["phone"]
    }
}