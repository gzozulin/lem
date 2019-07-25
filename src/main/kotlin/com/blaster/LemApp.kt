package com.blaster

import KotlinLexer
import KotlinParser
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

    /*
     * This is another one
     */

    Observable.fromIterable(listOf(File("src/main/kotlin/com/blaster/LemApp.kt")))
        .map { CharStreams.fromFileName(it.absolutePath) }
        .flatMap { renderMain(it) }
        .flatMap { renderInserts(it) }
        .flatMap { renderArticle(it) }
        .flatMapCompletable { printArticle(it) }
        .subscribe()
}

fun renderMain(charStream: CharStream): Observable<List<Insert>> =
    Observable.just(charStream)
        .map { tokens(charStream) }
        .map { it to parser(it) }
        .map { FunctionMainVisitor(it.first).visitKotlinFile(it.second.kotlinFile()) }

fun renderInserts(inserts: List<Insert>): Observable<List<ParagraphInsert>> =
    Observable.empty()

fun renderArticle(paragraphs: List<ParagraphInsert>): Observable<Article> =
    Observable.empty()

fun printArticle(article: Article): Completable =
    Completable.complete()
