package net.daverix.ajvm

import kotlinx.coroutines.CoroutineScope

expect fun runBlockingTest(block: suspend CoroutineScope.()-> Unit)
