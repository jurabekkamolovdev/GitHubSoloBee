package com.javohir.feature.register.content.common

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.register.content.common
 * Description: Sarlavha + matn maydoni juftligi — bosqichlarda takrorlanadi.
 */

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javohir.feature.R
import com.javohir.ui.component.LoginTextField
import com.javohir.ui.theme.SoloBeeColors

@Composable
internal fun RegisterFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    hintText: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onTogglePasswordVisibility: (() -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.nunito_semibold)),
                color = SoloBeeColors.Black
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        LoginTextField(
            value = value,
            onValueChange = onValueChange,
            hintText = hintText,
            hintFont = FontFamily(Font(resId = R.font.nunito_reguler)),
            modifier = Modifier.padding(horizontal = 16.dp),
            keyboardType = keyboardType,
            imeAction = imeAction,
            isPassword = isPassword,
            isPasswordVisible = isPasswordVisible,
            onTogglePasswordVisibility = onTogglePasswordVisibility
        )
    }
}