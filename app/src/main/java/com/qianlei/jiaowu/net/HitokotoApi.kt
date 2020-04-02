package com.qianlei.jiaowu.net

import android.util.Log
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * @author qianlei
 */
object HitokotoApi {
    private val client = OkHttpClient()

    /**
     *  获取诗词
     */
    fun getHitokoto(): String {
        return try {
            val request = Request.Builder()
                    .url("https://v1.hitokoto.cn?c=i")
                    .get()
                    .build()
            val response = client.newCall(request).execute()
            println("response$response")
            val json = JsonParser.parseString(response.body?.string()).asJsonObject
            val hitokoto = json.get("hitokoto").asString
            //去除句号
            hitokoto.substring(0, hitokoto.length - 1)
        } catch (e: Exception) {
            Log.e("HitokotoApi", "获取诗词信息出错", e)
            ""
        }
    }
}