package com.example.absensid.ui.permission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.absensid.core.Event
import org.koin.core.KoinComponent

class PermissionVm: ViewModel(), KoinComponent {
    /*private val repo by inject<FarmRepo> { parametersOf(viewModelScope) }*/

    val showLoading = MutableLiveData<Boolean>()

    private var _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    private var _showError = MutableLiveData<Event<String?>>()
    val showError: LiveData<Event<String?>>
        get() = _showError

    private var _permissionsGranted = MutableLiveData<List<String>>()
    val permissionsGranted: LiveData<List<String>>
        get() = _permissionsGranted

    private var _locationGranted = MutableLiveData<Boolean>()
    val locationGranted: LiveData<Boolean>
        get() = _locationGranted

    fun addPermissionsGranted(permissions: List<String>) {
        _permissionsGranted.value = permissions
    }
}