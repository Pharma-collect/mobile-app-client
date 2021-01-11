package projetbe.romelemma.ui.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import projetbe.romelemma.dataClass.User
import projetbe.romelemma.repository.MyRepository
import projetbe.romelemma.services.FileService
import java.io.File

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application), CoroutineScope by MainScope() {

    private var user: User = User()
    private val userDataRetrieved: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _lastName = MutableLiveData<String>()
    val lastName: LiveData<String> = _lastName

    private val _mail = MutableLiveData<String>()
    val mail: LiveData<String> = _mail

    val fileService: FileService = FileService()
    val repo: MyRepository = MyRepository()
    private val context = getApplication<Application>().applicationContext

    init {
        launch {
            user = fileService.getData(context)
        }
        launch {
            _name.value = user.name
            _lastName.value = user.lastname
            _mail.value = user.email
        }
        launch {
            repo.getUserInformations(context, user)
        }
    }
}