package com.icdominguez.spendless.presentation.model

import com.icdominguez.spendless.R

enum class ExportOption(val stringId: Int) {
    ALL_DATA(stringId = R.string.export_all_data),
    LAST_THREE_MONTHS(stringId = R.string.export_last_three_months),
    LAST_MONTH(stringId = R.string.export_last_month),
    CURRENT_MOTH(stringId = R.string.export_current_month)
    //SPECIFIC_MONTH(stringId = R.string.export_specific_month),
}

data class ExportOptionItem(
    val name: String,
    val stringId: Int,
    val isSelected: Boolean = false,
)

fun getExportOptionItems(): List<ExportOptionItem> {
    return ExportOption.entries.map { exportOption ->
        ExportOptionItem(
            name = exportOption.name,
            stringId = exportOption.stringId,
            isSelected = exportOption == ExportOption.LAST_THREE_MONTHS,
        )
    }
}