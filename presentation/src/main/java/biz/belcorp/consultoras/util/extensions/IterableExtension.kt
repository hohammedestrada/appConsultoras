package biz.belcorp.consultoras.util.extensions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


suspend fun <A, B> Iterable<A>.parallel(f: suspend (A) -> B): List<B> = runBlocking {
    map { async(Dispatchers.IO) { f(it) } }.map { it.await() }
}

