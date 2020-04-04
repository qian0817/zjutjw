package com.qianlei.jiaowu.repository

import androidx.lifecycle.MutableLiveData
import com.qianlei.jiaowu.net.HitokotoClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object HitokotoRepository {
    val hitokotoLiveData = MutableLiveData<String>()

    fun getPoem() {
        GlobalScope.launch {
            val hitokoto = HitokotoClient.getHitokoto()
            withContext(Dispatchers.Main) {
                hitokotoLiveData.value = hitokoto
            }
        }
    }

}