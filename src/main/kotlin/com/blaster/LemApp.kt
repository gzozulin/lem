package com.blaster

import KotlinLexer
import KotlinParser
import com.blaster.inserts.Insert
import io.reactivex.Completable
import io.reactivex.Observable
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File

fun tokens(charStream: CharStream) = CommonTokenStream(KotlinLexer(charStream))
fun parser(tokenStream: CommonTokenStream) = KotlinParser(tokenStream)

fun main() {
    /*
     * This is a nasty comment
     */

    Observable.fromIterable(listOf(File("src/main/kotlin/com/blaster/LemApp.kt")))
        .map { CharStreams.fromFileName(it.absolutePath) }
        .flatMap { renderMain(it) }
        .flatMap { renderIncludes(it) }
        .flatMap { renderInserts(it) }
        .flatMapCompletable { printArticle(it) }
        .subscribe()

    // Lets imagine, that we will do something additionally here
    System.out.println("Done!")

    System.out.println("No, seriously, done!")
}

// Main to insert stream
fun renderMain(charStream: CharStream): Observable<List<Insert>> = Observable.just(charStream)
        .map { tokens(charStream) }
        .map { it to parser(it) }
        .map { FunctionMainVisitor(it.first).visitKotlinFile(it.second.kotlinFile()) }

// Swap includes
fun renderIncludes(inserts: List<Insert>): Observable<List<Insert>> = Observable.just(inserts)

// Inserts to article
fun renderInserts(inserts: List<Insert>): Observable<Article> = Observable.fromIterable(inserts)
        .doOnNext { System.out.println(it) }
        .toList()
        .toObservable()
        .map { Article() }

// Printing the article
fun printArticle(article: Article): Completable = Completable.complete()
