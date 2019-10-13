package com.blaster.business

import com.blaster.data.inserts.Insert
import com.blaster.data.inserts.InsertCommand
import com.blaster.data.inserts.SUBCOMMAND_DECL
import com.blaster.data.inserts.SUBCOMMAND_DEF
import com.blaster.data.managers.parsing.ParsingManager
import com.blaster.data.managers.kotlin.KotlinParser
import com.blaster.data.managers.kotlin.KotlinManager
import com.blaster.platform.LEM_COMPONENT
import io.reactivex.Observable
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import java.io.File
import javax.inject.Inject

class InteractorParse {
    @Inject
    lateinit var interactorLocation: InteractorLocation

    @Inject
    lateinit var parsingManager: ParsingManager

    @Inject
    lateinit var kotlinManager: KotlinManager

    @Inject
    lateinit var interactorTokens: InteractorTokens

    init {
        LEM_COMPONENT.inject(this)
    }

    fun parseDef(sourceRoot: File, path: String): List<Insert> {
        val location = interactorLocation.locate(sourceRoot, path)
        val definition = kotlinManager.extractDef(location)
        val inserts = interactorTokens.extractTokens(definition)
        return processCommands(sourceRoot, inserts)
    }

    private fun parseDecl(sourceRoot: File, path: String): List<Insert> {
        val location = interactorLocation.locate(sourceRoot, path)
        val declarations = kotlinManager.extractDecl(location)
        val inserts = Observable.fromIterable(declarations)
            .flatMap { Observable.fromIterable(interactorTokens.extractTokens(it)) }
            .toList()
            .blockingGet()
        return processCommands(sourceRoot, inserts)
    }

    private fun processCommands(sourceRoot: File, inserts: List<Insert>): List<Insert> {
        val mutableList = ArrayList(inserts)
        val iterator = mutableList.listIterator()
        while (iterator.hasNext()) {
            val insert = iterator.next()
            if (insert is InsertCommand) {
                when (insert.type) {
                    InsertCommand.Type.INCLUDE -> {
                        when (insert.subcommand) {
                            SUBCOMMAND_DECL -> {
                                insert.children.addAll(parseDecl(sourceRoot, insert.argument))
                            }
                            SUBCOMMAND_DEF -> {
                                insert.children.addAll(parseDef(sourceRoot, insert.argument))
                            }
                        }
                    }
                    InsertCommand.Type.OMIT -> {
                        check(iterator.hasNext()) { "What to omit??" }
                        iterator.remove()
                        iterator.next()
                        iterator.remove()
                    }
                    InsertCommand.Type.INLINE -> {
                        iterator.remove()
                        when (insert.subcommand) {
                            SUBCOMMAND_DECL -> {
                                val declarations = parseDecl(sourceRoot, insert.argument)
                                for (decl in declarations) {
                                    iterator.add(decl)
                                }
                            }
                            SUBCOMMAND_DEF -> {
                                val definitions = parseDef(sourceRoot, insert.argument)
                                for (def in definitions) {
                                    iterator.add(def)
                                }
                            }
                        }
                    }
                    else -> {
                        // do nothing
                    }
                }
            }
        }
        return mutableList
    }
}