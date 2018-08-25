package com.appsempresariales.udea.database

import android.content.Context
import com.appsempresariales.udea.models.Note

//Por implementar
class NotesBL {
    private  var db: DBHelper ?= null

    /**Metodo para obtener la nota completa de la base de datos y retorna el objeto completo*/
    fun getNote(id: String): Note{

        return db!!.getId(id)
    }

    /**Metodo para actualizar la nota existente en la base de datos*/
    fun updateInBD(id: String, title: String, body: String){
        var num = id.toInt()
        var note = Note()
        note.setID(num)
        note.setTitle(title)
        note.setBody(body)
        db!!.updateNote(note)
    }

    /**Metodo que obtiene el contexto y se lo envia a la base de datos*/
    fun setContext(context: Context){
        this.db = DBHelper(context)
    }
}