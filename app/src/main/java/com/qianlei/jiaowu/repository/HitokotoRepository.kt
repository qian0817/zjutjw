package com.qianlei.jiaowu.repository

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.qianlei.jiaowu.net.HitokotoClient

object HitokotoRepository {
    val hitokotoLiveData = MutableLiveData<String>()

    class GetPoemTask : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            var ret: String
            do {
                ret = HitokotoClient.getHitokoto()
                //说明可能发生错误,立即返回
                if (ret.isEmpty()) {
                    return ""
                }
            } while (ret.length > 15)
            return ret
        }

        override fun onPostExecute(result: String?) {
            hitokotoLiveData.value = result
        }
    }

}