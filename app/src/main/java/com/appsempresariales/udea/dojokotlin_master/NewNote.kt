package com.appsempresariales.udea.dojokotlin_master

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.appsempresariales.udea.database.DBHelper
import com.appsempresariales.udea.database.NotesBL
import kotlinx.android.synthetic.main.new_note.*
import com.appsempresariales.udea.models.Note

class NewNote : AppCompatActivity() {
    private var db: DBHelper ?=null
    private var idNote : String ?= null
    private var notesBL: NotesBL ?= null
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

            /**Se pregunta si los campos son vacios o no, en caso de serlos sacará un error*/
            if(tittle?.isNullOrEmpty()){
                titulo.setError("Campo Vacio")/**Muestra el error en la casilla del titulo*/
                super.closeContextMenu() /**Linea para matener en la actividad actual y no continuar*/
            }else{
                /**Se pregunta si tiene id nula o no, en caso de ser nula significa que es una nota nueva,
                 * en caso contrario es una nota a modificar*/
                if(idNote.isNullOrBlank()){
                    db = DBHelper(context)
                    db!!.insertNote(newNote)
                }else{
                    updateNote()
                }
                val open = Intent(context,MainActivity::class.java) //Volver a la vista principal después de guardar
                startActivity(open)
            }
        }
        salir.setOnClickListener{ // Botón Salir
            val open = Intent(this,MainActivity::class.java)
            startActivity(open)
        }

    }

    /**Metodo que recibe los valores de una nota existente y que los muestra para ver que se va a modificar*/
    private fun receivingValue() {
        val intent = intent
        notesBL = NotesBL()  /**Inicializo el objeto de la clase DTO*/
        notesBL!!.setContext(context) /**Importante: Setearle el contexto ya que lo necesita la BD*/
        var value = intent.getStringExtra("notaID")
        if(!(value === null)){
            idNote = value!!
            Toast.makeText(context,idNote,Toast.LENGTH_LONG)
            var notes = notesBL!!.getNote(idNote!!) /**Incovo el metodo de notasDTO*/
            titulo.setText(notes.getTitle())
            texto.setText(notes.getBody())
        }
    }

    /**Metodo que actualiza las notas en la vista y en la BD*/
    private fun updateNote(){
        val intent = intent
        val value = intent.getStringExtra("notaID")
        idNote = value.toString()
        val title = titulo!!.text.toString()
        val body = texto!!.text.toString()
        notesBL!!.setContext(context) /**Importante: Setearle el contexto ya que lo necesita la BD*/
        notesBL!!.updateInBD(idNote!!,title, body) /**Incovo el metodo de notasDTO*/
    }
}
