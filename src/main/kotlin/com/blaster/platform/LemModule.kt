package com.blaster.platform

import com.blaster.business.*
import com.blaster.data.managers.statements.StatementsManager
import com.blaster.data.managers.statements.StatementsManagerImpl
import com.blaster.data.managers.kotlin.KotlinManager
import com.blaster.data.managers.kotlin.KotlinManagerImpl
import com.blaster.data.managers.printing.PrintingManager
import com.blaster.data.managers.printing.PrintingManagerImpl
import dagger.Module
import dagger.Provides
import freemarker.template.Configuration
import freemarker.template.TemplateExceptionHandler
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Module
class LemModule {
    @Singleton
    @Provides
    @Named("TEMPLATES_FILE")
    fun provideTemplatesFile() = File("templates")

    @Singleton
    @Provides
    @Named("ARTICLES_FILE")
    fun provideArticlesFile() = File("articles")

    @Singleton
    @Provides
    fun provideFreemarkerConfig(@Named("TEMPLATES_FILE") templatesFile: File): Configuration {
        val cfg = Configuration(Configuration.VERSION_2_3_27)
        cfg.setDirectoryForTemplateLoading(templatesFile)
        cfg.defaultEncoding = "UTF-8"
        cfg.templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER
        cfg.logTemplateExceptions = false
        cfg.wrapUncheckedExceptions = true
        return cfg
    }

    @Singleton
    @Provides
    fun lexingManager(): StatementsManager = StatementsManagerImpl()

    @Singleton
    @Provides
    fun providePrintingManager(): PrintingManager = PrintingManagerImpl()

    @Singleton
    @Provides
    fun providesParsingManager(): KotlinManager = KotlinManagerImpl()


    @Singleton
    @Provides
    fun providesLocatorUseCse() = InteractorLocation()

    @Singleton
    @Provides
    fun providesParseUseCase() = InteractorParse()

    @Singleton
    @Provides
    fun providesPrintUseCase() = InteractorPrint()

    @Singleton
    @Provides
    fun extractorCommands() = InteractorCommands()
}