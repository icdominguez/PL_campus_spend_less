package com.icdominguez.spendless.presentation.model

import com.icdominguez.spendless.R

enum class Category(val icon: String, val stringId: Int) {
    HOME(icon = "\uD83C\uDFE0", stringId = R.string.category_home),
    FOOD_AND_GROCERIES(icon = "\uD83C\uDF55", stringId = R.string.category_food_and_groceries),
    ENTERTAINMENT(icon = "\uD83D\uDCBB", stringId = R.string.category_entertainment),
    CLOTHING_AND_ACCESSORIES(icon = "\uD83D\uDC54", stringId = R.string.category_clothing_and_accessories),
    HEALTH_AND_WELLNESS(icon = "❤\uFE0F", stringId = R.string.category_health_and_wellness),
    PERSONAL_CARE(icon = "\uD83D\uDEC1", stringId = R.string.category_personal_care),
    TRANSPORTATION(icon = "\uD83D\uDE97", stringId = R.string.category_transportation),
    EDUCATION(icon = "\uD83C\uDF93", stringId = R.string.category_education),
    SAVING_AND_INVESTMENTS(icon = "\uD83D\uDC8E", stringId = R.string.category_saving_and_investments),
    OTHER(icon = "⚙\uFE0F", stringId = R.string.category_others)
}

data class CategoryItem(
    val name: String,
    val icon: String,
    val stringId: Int,
    val isSelected: Boolean = false
)

fun getCategoryItems(): List<CategoryItem> {
    return Category.entries.map {
        CategoryItem(
            name = it.name,
            icon = it.icon,
            stringId = it.stringId,
        )
    }
}