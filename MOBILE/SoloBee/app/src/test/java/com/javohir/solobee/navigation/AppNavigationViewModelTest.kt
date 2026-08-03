package com.javohir.solobee.navigation

import com.javohir.domain.model.SplashDestination
import com.javohir.domain.repository.SessionRepository
import com.javohir.domain.repository.SplashRepository
import com.javohir.domain.useCase.ObserveSessionExpiredUseCase
import com.javohir.domain.useCase.ResolveSplashDestinationUseCase
import com.javohir.solobee.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppNavigationViewModelTest {

    @get:Rule
    val mainDispatcherRule: MainDispatcherRule = MainDispatcherRule()

    @Test
    fun sessionExpired_emitsWelcome_whenOnboardingCompleted() = runTest {
        val sessionEvents: MutableSharedFlow<Unit> = MutableSharedFlow(extraBufferCapacity = 1)
        val viewModel: AppNavigationViewModel = createViewModel(
            sessionEvents = sessionEvents,
            hasAccessToken = false,
            hasCompletedOnboarding = true,
        )
        val destinations: MutableList<SplashDestination> = mutableListOf()
        val collectJob = launch {
            viewModel.authDestination.collect { destination ->
                destinations.add(destination)
            }
        }
        advanceUntilIdle()
        sessionEvents.emit(Unit)
        advanceUntilIdle()
        assertEquals(SplashDestination.Welcome, destinations.single())
        collectJob.cancel()
    }

    @Test
    fun sessionExpired_emitsOnBoarding_whenOnboardingNotCompleted() = runTest {
        val sessionEvents: MutableSharedFlow<Unit> = MutableSharedFlow(extraBufferCapacity = 1)
        val viewModel: AppNavigationViewModel = createViewModel(
            sessionEvents = sessionEvents,
            hasAccessToken = false,
            hasCompletedOnboarding = false,
        )
        val destinations: MutableList<SplashDestination> = mutableListOf()
        val collectJob = launch {
            viewModel.authDestination.collect { destination ->
                destinations.add(destination)
            }
        }
        advanceUntilIdle()
        sessionEvents.emit(Unit)
        advanceUntilIdle()
        assertEquals(SplashDestination.OnBoarding, destinations.single())
        collectJob.cancel()
    }

    @Test
    fun sessionExpired_doesNotEmit_whenDestinationIsHome() = runTest {
        val sessionEvents: MutableSharedFlow<Unit> = MutableSharedFlow(extraBufferCapacity = 1)
        val viewModel: AppNavigationViewModel = createViewModel(
            sessionEvents = sessionEvents,
            hasAccessToken = true,
            hasCompletedOnboarding = true,
        )
        val destinations: MutableList<SplashDestination> = mutableListOf()
        val collectJob = launch {
            viewModel.authDestination.collect { destination ->
                destinations.add(destination)
            }
        }
        advanceUntilIdle()
        sessionEvents.emit(Unit)
        advanceUntilIdle()
        assertTrue(destinations.isEmpty())
        collectJob.cancel()
    }

    private fun createViewModel(
        sessionEvents: Flow<Unit>,
        hasAccessToken: Boolean,
        hasCompletedOnboarding: Boolean,
    ): AppNavigationViewModel {
        val sessionRepository: SessionRepository = object : SessionRepository {
            override fun observeSessionExpired(): Flow<Unit> = sessionEvents
            override suspend fun clearSessionAndNotify() = Unit
        }
        val splashRepository: SplashRepository = object : SplashRepository {
            override suspend fun hasAccessToken(): Flow<Boolean> = flowOf(hasAccessToken)
            override suspend fun hasUserId(): Flow<Boolean> = flowOf(hasCompletedOnboarding)
            override suspend fun markOnboardingCompleted() = Unit
        }
        return AppNavigationViewModel(
            observeSessionExpiredUseCase = ObserveSessionExpiredUseCase(sessionRepository),
            resolveSplashDestinationUseCase = ResolveSplashDestinationUseCase(splashRepository),
        )
    }
}
