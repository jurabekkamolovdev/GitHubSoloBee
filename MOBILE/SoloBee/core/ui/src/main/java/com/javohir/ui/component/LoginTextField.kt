package com.javohir.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import com.javohir.ui.R
import com.javohir.ui.theme.SoloBeeColors

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.ui.component
 * Description: Custom Text Field
 */
@Composable
fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hintText: String,
    hintFont: FontFamily,
    modifier: Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onTogglePasswordVisibility: (() -> Unit)? = null
){

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = hintText,
                fontSize = 18.sp,
                fontFamily = hintFont,
                color = SoloBeeColors.TextFieldHint,
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(32.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        visualTransformation = if (isPassword && !isPasswordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = if (isPassword && onTogglePasswordVisibility != null) {
            {
                IconButton(onClick = onTogglePasswordVisibility) {
                    Icon(
                        painter = painterResource(
                            id = if (isPasswordVisible) R.drawable.ic_eye else R.drawable.ic_eye_off
                        ),
                        contentDescription = null,
                        tint = SoloBeeColors.TextFieldHint,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        } else {
            null
        },
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SoloBeeColors.TextFieldFocusedBorder,
            unfocusedBorderColor = SoloBeeColors.TextFieldUnfocusedBorder,
            focusedContainerColor = SoloBeeColors.White,
            unfocusedContainerColor = SoloBeeColors.White,
            cursorColor = SoloBeeColors.TextFieldText,
            focusedTextColor = SoloBeeColors.TextFieldText,
            unfocusedTextColor = SoloBeeColors.TextFieldText
        )
    )
}