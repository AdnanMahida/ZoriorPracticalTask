package com.ad.zoriorpracticaltask.ui.shared

import androidx.lifecycle.ViewModel
import com.ad.zoriorpracticaltask.data.repository.BaseRepository

abstract class BaseViewModel(
    private val baseRepository: BaseRepository
) : ViewModel() {
    //todo: common code separate here

}