package com.javohir.solobee.navigation
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.javohir.domain.model.SplashDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.javohir.feature.category.CategoryScreen
import com.javohir.feature.home.HomeScreen
import com.javohir.feature.login.LoginScreen
import com.javohir.feature.learn.LearnScreen
import com.javohir.feature.onBoarding.OnBoardingScreen
import com.javohir.feature.picquest.PicQuestScreen
import com.javohir.feature.profile.ProfileScreen
import com.javohir.feature.register.RegisterScreen
import com.javohir.feature.splash.SplashScreen
import com.javohir.feature.welcome.WelcomeScreen
import com.javohir.feature.topic.TopicScreen
import com.javohir.feature.wordHunt.WordHuntScreen
import com.javohir.feature.test.TestScreen
import com.javohir.feature.writing.WritingScreen

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.solobee
 * Description: App Navigation.
 */
@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    paddingValues: PaddingValues,
    isTablet: Boolean,
    appNavigationViewModel: AppNavigationViewModel = hiltViewModel(),
) {
    LaunchedEffect(appNavigationViewModel) {
        appNavigationViewModel.authDestination.collect { destination ->
            when (destination) {
                SplashDestination.OnBoarding -> {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
                SplashDestination.Welcome -> {
                    navController.navigate(Routes.WELCOME) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
                SplashDestination.Home -> Unit
            }
        }
    }
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
    ) {
        composable(
            route = Routes.SPLASH,
            enterTransition = NavTransitions.enter,
            exitTransition = NavTransitions.exit,
            popEnterTransition = NavTransitions.popEnter,
            popExitTransition = NavTransitions.popExit,
        ) {
            SplashScreen(
                paddingValues = paddingValues,
                navigateToOnBoarding = {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
                navigateToHome = {
                    navController.navigate(Routes.HOME){
                        popUpTo(Routes.SPLASH){inclusive = true}
                    }
                },
                navigateToWelcome = {
                    navController.navigate(Routes.WELCOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.ONBOARDING,
            enterTransition = NavTransitions.enter,
            exitTransition = NavTransitions.exit,
            popEnterTransition = NavTransitions.popEnter,
            popExitTransition = NavTransitions.popExit,
        ) {
            OnBoardingScreen(
                paddingValues = paddingValues,
                isTablet = isTablet,
                navigateToWelcome = {
                    navController.navigateSafely(Routes.WELCOME){
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Routes.WELCOME,
            enterTransition = NavTransitions.enter,
            exitTransition = NavTransitions.exit,
            popEnterTransition = NavTransitions.popEnter,
            popExitTransition = NavTransitions.popExit,
        ) {
            WelcomeScreen(
                paddingValues = paddingValues,
                isTablet = isTablet,
                navigateToLogin = {
                    navController.navigateSafely(Routes.LOGIN)
                },
                navigateToRegister = {
                    navController.navigateSafely(Routes.REGISTER)
                }
            )
        }
        composable(
            route = Routes.REGISTER,
            enterTransition = NavTransitions.enter,
            exitTransition = NavTransitions.exit,
            popEnterTransition = NavTransitions.popEnter,
            popExitTransition = NavTransitions.popExit,
        ) {
            RegisterScreen(
                paddingValues = paddingValues,
                isTablet = isTablet,
                navigateToLogin = {
                    navController.navigateSafely(Routes.LOGIN){
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                navigateBack = {
                    navController.popBackStackSafely()
                }
            )
        }
        composable(
            route = Routes.LOGIN,
            enterTransition = NavTransitions.enter,
            exitTransition = NavTransitions.exit,
            popEnterTransition = NavTransitions.popEnter,
            popExitTransition = NavTransitions.popExit,
        ) {
            LoginScreen(
                paddingValues = paddingValues,
                isTablet = isTablet,
                navigateToHome = {
                    navController.navigateSafely(Routes.HOME){
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Routes.HOME,
            enterTransition = NavTransitions.enter,
            exitTransition = NavTransitions.exit,
            popEnterTransition = NavTransitions.popEnter,
            popExitTransition = NavTransitions.popExit,
        ) {
            HomeScreen(
                paddingValues = paddingValues,
                isTablet = isTablet,
                navigateToCategory = { categoryId ->
                    navController.navigateSafely(Routes.categories(categoryId = categoryId)){
                        popUpTo(Routes.HOME){inclusive = false}
                    }
                },
                navigateToProfile = {
                    navController.navigateSafely(Routes.PROFILE){
                        popUpTo ( Routes.HOME ){inclusive = false}
                    }
                }
            )
        }

        composable(
            route = Routes.CATEGORIES,
            arguments = listOf(
                navArgument(name = Routes.CATEGORY_ID_ARG) { type = NavType.StringType }
            ),
            enterTransition = NavTransitions.enter,
            exitTransition = NavTransitions.exit,
            popEnterTransition = NavTransitions.popEnter,
            popExitTransition = NavTransitions.popExit,
        ) {
            CategoryScreen(
                paddingValues = paddingValues,
                isTablet = isTablet,
                onOpenTopics = { subCategoryId, subCategoryName ->
                    navController.navigateSafely(Routes.topics(subCategoryId, subCategoryName))
                },
                navigateToProfile = {
                    navController.navigateSafely(Routes.PROFILE)
                }
            )
        }

        composable(
            route = Routes.TOPICS,
            arguments = listOf(
                navArgument(name = Routes.SUB_CATEGORY_ID_ARG) { type = NavType.StringType },
                navArgument(name = Routes.SUB_CATEGORY_NAME_ARG) { type = NavType.StringType },
            ),
            enterTransition = NavTransitions.enter,
            exitTransition = NavTransitions.exit,
            popEnterTransition = NavTransitions.popEnter,
            popExitTransition = NavTransitions.popExit,
        ){
            TopicScreen(
                paddingValues = paddingValues,
                isTablet = isTablet,
                onOpenLearn = { topicId, subCategoryName ->
                    navController.navigateSafely(Routes.learn(topicId, subCategoryName))
                },
                onBackClick = {
                    navController.popBackStackSafely()
                },
            )
        }

        composable(
            route = Routes.LEARN,
            arguments = listOf(
                navArgument(name = Routes.TOPIC_ID_ARG) { type = NavType.StringType },
                navArgument(name = Routes.SUB_CATEGORY_NAME_ARG) { type = NavType.StringType },
            ),
            enterTransition = NavTransitions.enter,
            exitTransition = NavTransitions.exit,
            popEnterTransition = NavTransitions.popEnter,
            popExitTransition = NavTransitions.popExit,
        ) {
            LearnScreen(
                paddingValues = paddingValues,
                isTablet = isTablet,
                onOpenWriting = { topicId, subCategoryName ->
                    navController.navigateSafely(Routes.writing(topicId, subCategoryName))
                },
                onOpenTrace = { topicId, subCategoryName ->
                    navController.navigateSafely(Routes.trace(topicId, subCategoryName))
                },
                onOpenWordHunt = { topicId, subCategoryName ->
                    navController.navigateSafely(Routes.wordHunt(topicId, subCategoryName))
                },
                onBackClick = {
                    navController.popBackStackSafely()
                },
            )
        }

        composable(
            route = Routes.WRITING,
            arguments = listOf(
                navArgument(name = Routes.TOPIC_ID_ARG) { type = NavType.StringType },
                navArgument(name = Routes.SUB_CATEGORY_NAME_ARG) { type = NavType.StringType },
            ),
            enterTransition = NavTransitions.enter,
            exitTransition = NavTransitions.exit,
            popEnterTransition = NavTransitions.popEnter,
            popExitTransition = NavTransitions.popExit,
        ) {
            TestScreen(
                paddingValues = paddingValues,
                isTablet = isTablet,
                onOpenWordHunt = { topicId, subCategoryName ->
                    navController.navigateSafely(Routes.wordHunt(topicId, subCategoryName))
                },
                onBackClick = {
                    navController.popBackStackSafely()
                },
            )
        }

        composable(
            route = Routes.TRACE,
            arguments = listOf(
                navArgument(name = Routes.TOPIC_ID_ARG) { type = NavType.StringType },
                navArgument(name = Routes.SUB_CATEGORY_NAME_ARG) { type = NavType.StringType },
            ),
            enterTransition = NavTransitions.enter,
            exitTransition = NavTransitions.exit,
            popEnterTransition = NavTransitions.popEnter,
            popExitTransition = NavTransitions.popExit,
        ) {
            WritingScreen(
                paddingValues = paddingValues,
                isTablet = isTablet,
                onOpenWordHunt = { topicId, subCategoryName ->
                    navController.navigateSafely(Routes.wordHunt(topicId, subCategoryName))
                },
                onBackClick = {
                    navController.popBackStackSafely()
                },
            )
        }

        composable(
            route = Routes.WORD_HUNT,
            arguments = listOf(
                navArgument(name = Routes.TOPIC_ID_ARG) { type = NavType.StringType },
                navArgument(name = Routes.SUB_CATEGORY_NAME_ARG) { type = NavType.StringType },
            ),
        ) {
            WordHuntScreen(
                paddingValues = paddingValues,
                isTablet = isTablet,
                onBackClick = {
                    navController.popBackStackSafely()
                },
                onOpenPicQuest = { topicId, subCategoryName ->
                    navController.navigateSafely(Routes.picQuest(topicId, subCategoryName))
                },
            )
        }

        composable(
            route = Routes.PIC_QUEST,
            arguments = listOf(
                navArgument(name = Routes.TOPIC_ID_ARG) { type = NavType.StringType },
                navArgument(name = Routes.SUB_CATEGORY_NAME_ARG) { type = NavType.StringType },
            ),
        ) {
            PicQuestScreen(
                paddingValues = paddingValues,
                isTablet = isTablet,
                onBackClick = { navController.popBackStackSafely() },
                onNavigateToTopic = { _, _ ->
                    navController.popBackStack(route = Routes.TOPICS, inclusive = false)
                },
            )
        }
        composable(
            route = Routes.PROFILE,
            enterTransition = NavTransitions.enter,
            exitTransition = NavTransitions.exit,
            popEnterTransition = NavTransitions.popEnter,
            popExitTransition = NavTransitions.popExit,
        ) {
            ProfileScreen(
                paddingValues = paddingValues,
                isTablet = isTablet,
                onBackClick = { navController.popBackStackSafely() },
            )
        }
    }
}