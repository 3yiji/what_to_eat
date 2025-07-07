package com.example.myapplication

//import com.google.mlkit.nl.translate.*
//
//fun translateEnToZhSimple(
//    text: String,
//    onResult: (String) -> Unit
//) {
//    val options = TranslatorOptions.Builder()
//        .setSourceLanguage(TranslateLanguage.ENGLISH)
//        .setTargetLanguage(TranslateLanguage.CHINESE)
//        .build()
//
//    val translator = Translation.getClient(options)
//
//    translator.downloadModelIfNeeded()
//        .addOnSuccessListener {
//            translator.translate(text)
//                .addOnSuccessListener { translatedText ->
//                    onResult(translatedText)
//                }
//                .addOnFailureListener {
//                    onResult(text)  // 失败返回原文
//                }
//        }
//        .addOnFailureListener {
//            onResult(text)      // 失败返回原文
//        }
//}
//

import android.content.Context
import com.google.mlkit.nl.translate.*
import kotlinx.coroutines.tasks.await

suspend fun translateEnToZhSuspend(text: String): String {
    val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.CHINESE)
        .build()

    val translator = Translation.getClient(options)

    return try {
        translator.downloadModelIfNeeded().await()
        translator.translate(text).await()
    } catch (e: Exception) {
        text // 失败返回原文
    }
}