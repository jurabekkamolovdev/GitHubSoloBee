package com.javohir.solobee.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.solobee.navigation
 * Description: Navigatsiya kengaytmalari.
 */

/**
 * Tugma tez-tez bosilganda bir xil ekran back stack'ga bir necha marta tushib qolmasligi uchun.
 *
 * Birinchi `navigate()` dan keyin joriy entry RESUMED holatidan chiqadi — shu paytgacha
 * navbatga tushib ulgurgan bosishlar e'tiborsiz qoldiriladi. `launchSingleTop` esa
 * bir xil marshrut tepada takrorlanishini qo'shimcha ravishda to'sadi.
 */
fun NavHostController.navigateSafely(
    route: String,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    val isResumed = currentBackStackEntry
        ?.lifecycle
        ?.currentState
        ?.isAtLeast(Lifecycle.State.RESUMED) == true

    if (!isResumed) return

    navigate(route) {
        launchSingleTop = true
        builder()
    }
}

/**
 * Orqaga tugmasi tez ketma-ket bosilganda bir nechta ekran birdan yopilib ketmasligi uchun.
 */
fun NavHostController.popBackStackSafely() {
    val isResumed = currentBackStackEntry
        ?.lifecycle
        ?.currentState
        ?.isAtLeast(Lifecycle.State.RESUMED) == true

    if (!isResumed) return

    popBackStack()
}