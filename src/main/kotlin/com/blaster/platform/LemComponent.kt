package com.blaster.platform

import com.blaster.business.ConvertInsertsUseCase
import com.blaster.business.ParseCommandUseCase
import com.blaster.business.ParseMethodUseCase
import com.blaster.business.PrintUseCase
import com.blaster.data.managers.printing.PrintingManagerImpl
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ LemModule::class ])
interface LemComponent {
    fun inject(lemApp: LemApp)
    fun inject(parseMethodUseCase: ParseMethodUseCase)
    fun inject(printUseCase: PrintUseCase)
    fun inject(printingManagerImpl: PrintingManagerImpl)
    fun inject(convertInsertsUseCase: ConvertInsertsUseCase)
    fun inject(parseCommandUseCase: ParseCommandUseCase)
}

val LEM_COMPONENT : LemComponent = DaggerLemComponent.builder().build()