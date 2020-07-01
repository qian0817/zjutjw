package com.qianlei.jiaowu.ui.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.qianlei.jiaowu.repository.HitokotoRepository
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    val subTitleLiveData = HitokotoRepository.hitokotoLiveData

    fun getSubTitle() {
        viewModelScope.launch {
            HitokotoRepository.getPoem()
        }
    }
}