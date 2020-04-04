package com.qianlei.jiaowu.repository

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.qianlei.jiaowu.net.HitokotoClient

object HitokotoRepository {
    val hitokotoLiveData = MutableLiveData<String>()

    class GetPoemTask : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            return HitokotoClient.getHitokoto()
        }

        override fun onPostExecute(result: String?) {
            hitokotoLiveData.value = result
        }
    }

}