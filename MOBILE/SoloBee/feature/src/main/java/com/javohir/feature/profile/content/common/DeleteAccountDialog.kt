package com.javohir.feature.profile.content.common
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.javohir.feature.R
import com.javohir.ui.theme.SoloBeeColors

@Composable
internal fun DeleteAccountDialog(
    isDeletingAccount: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            if (!isDeletingAccount) {
                onDismiss()
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.delete_account_dialog_title),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_semibold)),
                    color = SoloBeeColors.Black,
                ),
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.delete_account_dialog_message),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_reguler)),
                    color = SoloBeeColors.TextMuted,
                ),
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isDeletingAccount,
            ) {
                Text(
                    text = stringResource(id = R.string.delete_account_dialog_confirm),
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.nunito_semibold)),
                        color = SoloBeeColors.Black,
                    ),
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isDeletingAccount,
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.nunito_reguler)),
                        color = SoloBeeColors.TextMuted,
                    ),
                )
            }
        },
    )
}
