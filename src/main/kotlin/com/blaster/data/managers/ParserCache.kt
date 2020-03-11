package com.blaster.data.managers

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Parser

private data class CacheEntry<T : Parser>(var parser: T? = null, val mutex: Mutex = Mutex())

abstract class ParserCache<K, T : Parser> {
    private val cache = mutableMapOf<K, CacheEntry<T>>()
    fun useParser(key: K, action: (parser: T, stream: CommonTokenStream) -> Unit) {
        var entry: CacheEntry<T>
        synchronized(cache) {
            if (!cache.containsKey(key)) {
                cache[key] = CacheEntry()
            }
            entry = cache[key]!!
        }
        return runBlocking {
            entry.mutex.withLock {
                if (entry.parser == null) {
                    entry.parser = createParser(key)
                }
                val parser = entry.parser!!
                val stream = parser.tokenStream as CommonTokenStream
                parser.reset()
                action.invoke(parser, stream)
            }
        }
    }

    abstract fun createParser(key: K): T
}