package com.developer.coroutineviewmodeljetpack.model

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.developer.coroutineviewmodeljetpack.network.getDataUser
import kotlinx.coroutines.*
import java.util.logging.Logger

class ViewModelUser: ViewModel() {
    var _nameUser = mutableStateOf("LOADING..")

    val nameUser
        get() = _nameUser.value

    init {
        val scope = CoroutineScope(Dispatchers.IO)
        val job1 = scope.launch {
            getDataUser()
            Log.d("SSS", "${Thread.currentThread()}")

            val deferredNameUser = async {
                Thread.sleep(5000)
                getDataUser()
            }
            withContext(Dispatchers.Main){
                _nameUser.value = deferredNameUser.await()
            }

            deferredNameUser.cancel()

            deferredNameUser.invokeOnCompletion { handler ->
                if (handler == null) {
                    Log.i("SSS", "deferredNameUser Success!")
                }else {
                    Log.i("SSS", "deferredNameUser Fail!")
                }
            }
        }
    }
}
