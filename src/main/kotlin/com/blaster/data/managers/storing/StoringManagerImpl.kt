package com.blaster.data.managers.storing

import com.blaster.business.LocatedInfo
import com.blaster.data.entities.Insert
import java.io.File

class StoringManagerImpl : StoringManager {
    private val insertsMap = HashMap<LocatedInfo, List<Insert>>()

    override fun storeInserts(located: LocatedInfo, inserts: List<Insert>) {
        insertsMap[located] = inserts
    }

    override fun getInserts(): Map<LocatedInfo, List<Insert>> = insertsMap
}