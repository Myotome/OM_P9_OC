package com.openclassrooms.realestatemanager

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class CoroutineDispatchers @Inject constructor(){
    val ioDispatchers: CoroutineContext = Dispatchers.IO
}