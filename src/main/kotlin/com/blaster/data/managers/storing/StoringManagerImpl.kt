package com.blaster.data.managers.storing

import com.blaster.data.entities.Insert
import java.io.File

class StoringManagerImpl : StoringManager {
    private val insertsMap = HashMap<File, List<Insert>>()

    override fun storeInserts(root: File, inserts: List<Insert>) {
        insertsMap[root] = inserts
    }

    override fun getInserts(): Map<File, List<Insert>> = insertsMap
}