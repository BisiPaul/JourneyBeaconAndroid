package com.fluffydevs.journeybeacon.common.structure

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    private val _navigateBack = MutableLiveData<Event<Unit>>()
    val navigateBack: LiveData<Event<Unit>>
        get() = _navigateBack

    open fun onNavigateBackClicked() {
        _navigateBack.value = Event(Unit)
    }
}