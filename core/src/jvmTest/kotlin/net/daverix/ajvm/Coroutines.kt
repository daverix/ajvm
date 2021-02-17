package net.daverix.ajvm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
actual fun runBlockingTest(block: suspend CoroutineScope.() -> Unit) =
        kotlinx.coroutines.test.runBlockingTest(testBody = block)
