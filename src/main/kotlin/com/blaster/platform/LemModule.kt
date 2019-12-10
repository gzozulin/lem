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
    fun templatesFile() = File("templates")

    // This methods will provide the configuration for the FreeMarker template engine. Its only parameter is a place, where we store the templates. This parameter is fulfilled by DI as well.
    @Singleton
    @Provides
    fun freemarkerConfig(@Named("TEMPLATES_FILE") templatesFile: File): Configuration {
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
    fun statementsManager(): StatementsManager = StatementsManagerImpl()

    @Singleton
    @Provides
    fun printingManager(): PrintingManager = PrintingManagerImpl()

    @Singleton
    @Provides
    fun kotlinManager(): KotlinManager = KotlinManagerImpl()

    // This method will create 'InteractorParse' instance [text; https://docs.samsungknox.com/dev/common/knox-version-mapping.htm] and inject it into the 'rest of the application'. Its annotation tells us, that only one instance of this class will be created.
    @Singleton
    @Provides
    fun interactorParse() = InteractorParse()

    @Singleton
    @Provides
    fun interactorLocation() = InteractorLocation()

    @Singleton
    @Provides
    fun interactorPrint() = InteractorPrint()

    @Singleton
    @Provides
    fun interactorCommands() = InteractorCommands()

    @Singleton
    @Provides
    fun interactorFormat() = InteractorFormat()

    @Singleton
    @Provides
    fun interactorStructs() = InteractorStructs()

    @Singleton
    @Provides
    fun interactorSpans() = InteractorSpans()
}