package com.icdominguez.spendless.presentation.designsystem.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.icdominguez.spendless.R
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography

@Composable
fun EnterPinAgainText(
    hasMoreAttempts: Boolean,
    secondsString: String,
) {
    val text = if(hasMoreAttempts) {
        stringResource(R.string.enter_your_pin)
    } else {
        stringResource(R.string.try_your_pin_again, secondsString)
    }

    val annotatedText = buildAnnotatedString {
        val startIndex = text.indexOf(secondsString)
        append(text)
        if(startIndex >= 0) {
            addStyle(
                style = SpanStyle(fontWeight = FontWeight.Bold),
                start = startIndex,
                end = startIndex + secondsString.length
            )
        }
    }

    Text(
        text = annotatedText,
        style = LocalSpendlessTypography.current.bodyMedium,
    )
}

@Preview(showBackground = true)
@Composable
private fun EnterPinAgainTextPreview() {

}