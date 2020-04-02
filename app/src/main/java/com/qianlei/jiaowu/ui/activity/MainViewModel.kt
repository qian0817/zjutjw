package com.qianlei.jiaowu.ui.activity

import android.app.Application
import android.os.Handler
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.qianlei.jiaowu.net.HitokotoApi

class MainViewModel(app: Application) : AndroidViewModel(app) {
    val subTitleLiveData = MutableLiveData<String>()
    private val handler = Handler()

    fun getSubTitle() {
        Thread {
            var subTitle: String
            do {
                subTitle = HitokotoApi.getHitokoto()
                //说明可能发生错误,立即返回
                if (subTitle.isEmpty()) {
                    return@Thread
                }
            } while (subTitle.length > 15)
            handler.post {
                subTitleLiveData.value = subTitle
            }
        }.start()
    }
}