package com.blaster.platform

import com.blaster.business.ParseUseCase
import com.blaster.business.PrintUseCase
import com.blaster.data.managers.printing.PrintingManagerImpl
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ LemModule::class ])
interface LemComponent {
    fun inject(lemApp: LemApp)
    fun inject(parseUseCase: ParseUseCase)
    fun inject(printUseCase: PrintUseCase)
    fun inject(printingManagerImpl: PrintingManagerImpl)
}

val LEM_COMPONENT : LemComponent = DaggerLemComponent.builder().build()