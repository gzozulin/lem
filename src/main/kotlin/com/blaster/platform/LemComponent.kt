package com.blaster.platform

import com.blaster.business.LocatorInteractor
import com.blaster.business.ParseInteractor
import com.blaster.business.PrintInteractor
import com.blaster.data.managers.printing.PrintingManagerImpl
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ LemModule::class ])
interface LemComponent {
    fun inject(lemApp: LemApp)
    fun inject(parseInteractor: ParseInteractor)
    fun inject(printInteractor: PrintInteractor)
    fun inject(printingManagerImpl: PrintingManagerImpl)
    fun inject(locatorInteractor: LocatorInteractor)
}

val LEM_COMPONENT : LemComponent = DaggerLemComponent.builder().build()