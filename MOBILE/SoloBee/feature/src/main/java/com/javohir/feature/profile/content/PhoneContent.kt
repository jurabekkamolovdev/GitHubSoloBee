package com.javohir.feature.profile.content
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javohir.feature.R
import com.javohir.feature.profile.ProfileIntent
import com.javohir.feature.profile.ProfileState
import com.javohir.feature.profile.content.common.ProfileAvatarCard
import com.javohir.feature.profile.content.common.ProfileSettingsMenu
import com.javohir.ui.theme.SoloBeeColors

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.feature.profile.content
 * Description: profile/TabletContent: qurilma o'lchami bo'yicha Compose kontent.
 */

@Composable
fun PhoneContent(
    onAction: (ProfileIntent) -> Unit,
    onBackClick: () -> Unit,
    state: ProfileState,
    paddingValues: PaddingValues
){
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(all = 16.dp)
        .padding(top = paddingValues.calculateTopPadding())
        .padding(bottom = paddingValues.calculateBottomPadding())
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(56.dp)
                    .padding(end = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_btn),
                    contentDescription = "Back Button",
                    modifier = Modifier.size(56.dp),
                    tint = Color.Unspecified
                )
            }
            Text(
                text = stringResource(id = R.string.profile),
                style = TextStyle(
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_reguler)),
                    color = SoloBeeColors.Black
                ),
                modifier = Modifier
                    .height(24.dp)
                    .fillMaxWidth(0.35f)
            )
        }

        ProfileAvatarCard(
            userInitial = state.userInitial,
            fullName = state.fullName,
            isLoadingProfile = state.isLoadingProfile,
            onChangeAvatarClick = { onAction(ProfileIntent.ChangeAvatarClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
        )
        ProfileSettingsMenu(onAction = onAction)
    }
}

@Composable
@Preview(showBackground = true)
fun PhonePreview(){
    PhoneContent(
        onAction = {},
        onBackClick = {},
        state = ProfileState(),
        paddingValues = PaddingValues(all = 12.dp)
    )
}