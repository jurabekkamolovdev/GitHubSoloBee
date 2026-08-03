package com.javohir.domain.model

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.domain.model
 * Description: Login API Result
 */

data class LoginResult(
   val tokens: Tokens
)
data class Tokens(
   val accessToken: String,
   val refreshToken: String
)
