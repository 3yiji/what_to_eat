package com.example.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

// Retrofit 接口示例
interface LibreTranslateService {
    @POST("translate")
    @FormUrlEncoded
    suspend fun translate(
        @Field("q") text: String,
        @Field("source") sourceLang: String = "en",
        @Field("target") targetLang: String = "zh",
        @Field("format") format: String = "text",
        @Field("api_key") apiKey: String = ""  // 官方API通常无需密钥
    ): TranslateResponse
}

// 数据类
data class TranslateResponse(val translatedText: String)


// 新增翻译函数（独立方法）
suspend fun translateToChinese(text: String): String {
    val translateApi = Retrofit.Builder()
        .baseUrl("https://libretranslate.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(LibreTranslateService::class.java)

    return try {
        translateApi.translate(text).translatedText
    } catch (e: Exception) {
        text // 翻译失败时返回原文
    }
}
//// 使用示例
//val service = Retrofit.Builder()
//    .baseUrl("https://libretranslate.com/")
//    .build()
//    .create(LibreTranslateService::class.java)
//
//val translatedText = service.translate("Hello").translatedText  // 输出: "你好"