package com.appsempresariales.udea.database

import android.content.Context
import com.appsempresariales.udea.models.Note

//Por implementar
class Notes_DTO {
    private  var db: DBHelper ?= null

    fun getNote(id: String): Note{

        return db!!.getId(id)
    }

    fun updateInBD(id: String, title: String, body: String){
        var num = id.toInt()
        var note = Note()
        note.setID(num)
        note.setTitle(title)
        note.setBody(body)
        db!!.updateNote(note)
    }

    fun setContext(context: Context){
        this.db = DBHelper(context)
    }
}