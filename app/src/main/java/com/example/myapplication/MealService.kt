package com.example.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


// 创建 Retrofit 服务接口
interface MealService {
    @GET("random.php")
    suspend fun randomMeal(): MealResponse

    companion object {
        val api: MealService by lazy {
            Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MealService::class.java)
        }
    }

}

// 定义数据类
data class MealResponse(val meals: List<Meal>)
data class Meal(val strMeal: String, val strMealThumb: String, val strInstructions: String)