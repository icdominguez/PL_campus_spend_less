package com.icdominguez.spendless.presentation.designsystem.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    textStyle: TextStyle,
) {
    BasicTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        cursorBrush = SolidColor(LocalSpendlessColorsPalette.current.primary),
        textStyle = textStyle,
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = label,
                    style = LocalSpendlessTypography.current.bodyMedium.copy(
                        color = LocalSpendlessColorsPalette.current.onSurface,
                        textAlign = TextAlign.Center,
                    )
                )
            }
            innerTextField()
        }
    )
}

@Preview
@Composable
private fun CustomTextfieldPreview() {
}