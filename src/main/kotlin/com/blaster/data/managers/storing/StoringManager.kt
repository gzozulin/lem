package com.blaster.data.managers.storing

import com.blaster.business.LocatedInfo
import com.blaster.data.entities.Insert
import java.io.File

interface StoringManager {
    fun storeInserts(located: LocatedInfo, inserts: List<Insert>)
    fun getInserts() : Map<LocatedInfo, List<Insert>>
}