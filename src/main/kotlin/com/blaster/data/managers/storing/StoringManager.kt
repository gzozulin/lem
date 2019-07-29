package com.blaster.data.managers.storing

import com.blaster.data.entities.Insert
import java.io.File

interface StoringManager {
    fun storeInserts(root: File, inserts: List<Insert>)
    fun getInserts() : Map<File, List<Insert>>
}