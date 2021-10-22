package com.naman.flobizstack.DBroom

import androidx.room.TypeConverter
import com.naman.flobizstack.models.Owner


class Converters {

    @TypeConverter
    fun fromOwner(owner: Owner):String{
        return owner.profile_image
    }

    @TypeConverter
    fun toOwner(name:String):Owner{
        return Owner(0,"naman","","",0,0,"")
    }
    @TypeConverter
    fun fromTags(tags: List<String>):String{
        return tags[0]
    }

    @TypeConverter
    fun toTags(name:String):List<String>{
        return listOf("")
    }
}