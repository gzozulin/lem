package com.blaster.platform

import com.blaster.business.*
import com.blaster.data.managers.parsing.ParsingManager
import com.blaster.data.managers.parsing.ParsingManagerImpl
import com.blaster.data.managers.printing.PrintingManagerImpl
import com.blaster.data.managers.printing.PrintingManager
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
    @Named("SOURCE_ROOT")
    fun providesSourceRoot() = File("src/main/kotlin")

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
    fun providesLocatorUseCse() = LocatorInteractor()

    @Singleton
    @Provides
    fun providePrintingManager() : PrintingManager = PrintingManagerImpl()

    @Singleton
    @Provides
    fun providesParsingManager() : ParsingManager = ParsingManagerImpl()

    @Singleton
    @Provides
    fun providesParseUseCase() = ParseInteractor()

    @Singleton
    @Provides
    fun providesPrintUseCase() = PrintInteractor()

    @Singleton
    @Provides
    fun providesParseCommentUseCase() = CommentsInteractor()
}