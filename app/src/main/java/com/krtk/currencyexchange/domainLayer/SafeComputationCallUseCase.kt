package com.krtk.currencyexchange.domainLayer

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
//SafeComputationCallUseCase that appears to be related to handling asynchronous computation calls in a safe
//The computation is executed on the Dispatchers.Default context to avoid blocking the main thread.
// Any exceptions thrown during the computation are captured and returned as a Result object,
// either as a successful result with the computed value or as a failure result with the caught exception.
interface SafeComputationCallUseCase {
    suspend fun <T> computationCall(
        call: suspend () -> T
    ): Result<T> = runCatching {
        withContext(Dispatchers.Default) {
            call.invoke()
        }
    }
}