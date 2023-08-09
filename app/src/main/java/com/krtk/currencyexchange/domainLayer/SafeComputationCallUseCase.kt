package com.krtk.currencyexchange.domainLayer

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface SafeComputationCallUseCase {
    suspend fun <T> computationCall(
        call: suspend () -> T
    ): Result<T> = runCatching {
        withContext(Dispatchers.Default) {
            call.invoke()
        }
    }
}