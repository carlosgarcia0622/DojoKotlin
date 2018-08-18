package com.appsempresariales.udea.dojokotlin_master

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.appsempresariales.udea.database.DBHelper
import com.appsempresariales.udea.database.Notes_DTO
import kotlinx.android.synthetic.main.new_note.*
import com.appsempresariales.udea.models.Note

class newNote : AppCompatActivity() {
    private var db: DBHelper ?=null
    private var idNote : String ?= null
    private var notesDTO: Notes_DTO ?= null
    private var note: Note ?= null
    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_note)

        this.receivingValue()/*-----------------------------------------------------*/
        guardar.setOnClickListener{ //Botón Guardar: Se deben capturar el id, título y el body y guardar en la base de datos (Falta capturar id y guardar en la base de datos)

            val tittle = titulo.text.toString()// Captura titulo
            val body = texto.text.toString()// Captura Body
            // Para capturar ID se debe conocer el tamaño de la lista (Solo aplica para nuevas notas, el caso editar se debe validar)

            var newNote = Note(tittle, body) //ejemplo creación nota: Definir tamaño de lista para ingresar el id correcto

            //Aquí debe ir el Query Create con el objeto previamente creado
            db = DBHelper(context)
            db!!.insertNote(newNote)

            val open = Intent(this,MainActivity::class.java) //Volver a la vista principal después de guardar
            startActivity(open)
        }
        salir.setOnClickListener{ // Botón Salir
            val open = Intent(this,MainActivity::class.java)
            startActivity(open)
        }

    }

    //Por implementar
    private fun receivingValue() {
        val intent = intent
        notesDTO = Notes_DTO()
        notesDTO!!.setContext(context)
        var value = intent.getStringExtra("notaID")
        if(!(value === null)){
            idNote = value!!
            Toast.makeText(context,idNote,Toast.LENGTH_LONG)
            var notes = notesDTO!!.getNote(idNote!!)
    }
    }

    //Por implementar
    private fun updateNote(){
        val intent = intent
        val value = intent.getStringExtra("notaID")
        idNote = value.toString()
        val title = titulo!!.text.toString()
        val body = texto!!.text.toString()
        notesDTO!!.setContext(context)
        notesDTO!!.updateInBD(idNote!!,title, body)
    }
}
