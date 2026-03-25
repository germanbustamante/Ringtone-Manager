package com.germandebustamante.ringtonemanager.core.model.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class TestDispatcherProvider : DispatcherProvider {
    override val io: CoroutineDispatcher = UnconfinedTestDispatcher()
    override val main: CoroutineDispatcher = UnconfinedTestDispatcher()
    override val default: CoroutineDispatcher = UnconfinedTestDispatcher()
}
