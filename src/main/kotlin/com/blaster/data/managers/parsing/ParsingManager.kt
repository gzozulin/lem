package com.blaster.data.managers.parsing

import com.blaster.business.ClassLocation
import com.blaster.business.GlobalLocation
import com.blaster.business.MemberLocation

import com.blaster.data.inserts.Insert

interface ParsingManager {
    fun parseGlobalMethodDef(location: GlobalLocation) : List<Insert>
    fun parseMemberMethodDef(location: MemberLocation) : List<Insert>

    fun parseGlobalDecl(location: GlobalLocation) : List<Insert>
    fun parseMemberDecl(location: MemberLocation) : List<Insert>
    fun parseClassDecl(location: ClassLocation) : List<Insert>
}