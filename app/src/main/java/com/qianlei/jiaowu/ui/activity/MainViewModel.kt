package com.qianlei.jiaowu.ui.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.qianlei.jiaowu.repository.HitokotoRepository

class MainViewModel(app: Application) : AndroidViewModel(app) {
    val subTitleLiveData = HitokotoRepository.hitokotoLiveData

    fun getSubTitle() {
        val task = HitokotoRepository.GetPoemTask()
        task.execute()
    }
}