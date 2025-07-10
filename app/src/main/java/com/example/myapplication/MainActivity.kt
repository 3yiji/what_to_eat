// MainActivity.kt
package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                LunchSuggester()
            }
        }
    }
}


@Composable
fun LunchSuggester() {
    var mealName by remember { mutableStateOf("") }
    var mealName_trans by remember { mutableStateOf("") }
    var mealThumb by remember { mutableStateOf<String?>("") }
    var mealInstructions by remember { mutableStateOf<String?>("") }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 顶部图片
        if (mealThumb != null) {
            Image(
                painter = rememberAsyncImagePainter(mealThumb),
                contentDescription = mealName,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .aspectRatio(1f / 1f) // 或 4f / 3f、1f 等，根据你需要的比例

            )
        }

        // 撑开中间空间
        Spacer(modifier = Modifier.weight(0.001f))

        // 食物名称文本
        Text(
            text = mealName,
            style = MaterialTheme.typography.titleMedium
        )
        // 翻译后的食物名称文本
        Text(
            text = mealName_trans,
            style = MaterialTheme.typography.titleMedium
        )

        // 显示做法
        mealInstructions?.let {
            Box(
                modifier = Modifier
                    .weight(1f) // ✅ 自动填满剩余空间
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
            ) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
        // 按钮
        Button(
            onClick = {
                scope.launch {
                    if (!isActive) return@launch
                    loading = true
                    try {
                        val response = MealService.api.randomMeal()
                        if (response.meals.isNotEmpty()) {
                            val meal = response.meals.first()
                            mealName = meal.strMeal
                            mealName_trans = translateEnToZhSuspend(meal.strMeal)
                            mealThumb = meal.strMealThumb
                            mealInstructions = translateEnToZhSuspend(meal.strInstructions)
                        }
                    } catch (e: Exception) {
                        mealName = "获取失败，请重试"
                    } finally {
                        loading = false
                    }
                }
            },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth(),


        ) {
            Text(if (loading) "加载中..." else "吃点啥？")
        }
    }
}
