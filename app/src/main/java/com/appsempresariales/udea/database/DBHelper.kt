package com.appsempresariales.udea.database

import com.appsempresariales.udea.models.Note
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

//Se crean constantes con el nombre de la base de datos y la versión
val DATABASE_NAME = "Notes"
val DATABASE_VERSION = 1


/***
 * Al poseer argumentos en el nombre de la clase, quiere decir que su constructor necesita
   esa variable para inicializar.
   Ej: var db = DBHelper(se le mando el contexto)
 * Los dos puntos es para heredar, y la clase SQLite neceista los parametros

 */
class DBHelper (var context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    //Este metodo se encarga de crear la tabla cuando no exista en el celular
    override fun onCreate(p0: SQLiteDatabase?) {
        //Se crea el query para crear la tabla.
        //Las variables son constantes que tienen por defecto el nombre de las columnas
        val queryCreateTable = "CREATE TABLE "+ Tables.Notes.TABLE_NAME +
                " ("+ Tables.Notes._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Tables.Notes.COLUMN_TITLE + " TEXT NOT NULL,"+
                Tables.Notes.COLUMN_BODY+ " TEXT NOT NULL)";
        p0!!.execSQL(queryCreateTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    fun insertNote(note: Note){

        //Se abre la conexión con la base de datos
        val db = this.writableDatabase

        //Se pasan los parametros por los values
        val datos = ContentValues()
        datos.put(Tables.Notes.COLUMN_TITLE, note.getTitle())
        datos.put(Tables.Notes.COLUMN_BODY, note.getBody())

        //Se ejecuta el query, con el nombre de la tabla, sin argumentos nulos, y con los datos
        var resultado = db!!.insert(Tables.Notes.TABLE_NAME,null,datos)

        //Si hay error informar al usuario
        if(resultado === -1.toLong())
        {
            Toast.makeText(context,"Hubo un error al ingresar la nota",Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context,"Nota creada con éxito",Toast.LENGTH_SHORT).show()
        }
    }

    fun getNotes() : MutableList<Note>
    {

        //Lista resultado de notas
        var notes : MutableList<Note> = ArrayList()

        //Se abre la conexión a la base de datos
        val db = this.readableDatabase
        val querySelect = "SELECT * FROM " + Tables.Notes.TABLE_NAME

        //Se obtiene el valor de la base de datos
        var resultado = db!!.rawQuery(querySelect,null)
        /*Se mueve al primer registro de todos los obtenidos*/
        if(resultado.moveToFirst())
        {
            //Se crean notas a medida que se encuentren
            //Se setean los valores
            do {
                var note = Note()
                note.setID(resultado.getString(resultado
                        .getColumnIndex(Tables.Notes._ID)).toInt())

                note.setBody(resultado.getString(resultado
                        .getColumnIndex(Tables.Notes.COLUMN_BODY)))

                note.setTitle(resultado.getString(resultado
                        .getColumnIndex(Tables.Notes.COLUMN_TITLE)))

                notes.add(note)
            }while (resultado.moveToNext())
        }
        //Cerrar conexiones
        resultado.close()
        db!!.close()
        return notes
    }

    fun deleteNote(note: Note)
    {
        //Se abre conexión a la base de datos
        val db = this.writableDatabase
        //Se toma el ID de la nota enviada
        var args = arrayOf(note.getID().toString())

        var resultado = db!!.delete(Tables.Notes.TABLE_NAME, Tables.Notes._ID + " =?",args)

        if(resultado === -1)
        {
            Toast.makeText(context,"Hubo un error al borrar la nota",Toast.LENGTH_SHORT).show()
        }
        else
        {
            Toast.makeText(context,"Nota eliminada con éxito",Toast.LENGTH_SHORT).show()
        }
        //Cerrar la conexión
        db.close()
    }

    //Por implementarse
    fun updateNote(note : Note)
    {
        val db = this.writableDatabase
        val datos = ContentValues()
        var args = arrayOf(note.getID().toString())

        datos.put(Tables.Notes.COLUMN_TITLE,note.getTitle())
        datos.put(Tables.Notes.COLUMN_BODY, note.getBody())
        db!!.update(Tables.Notes.TABLE_NAME,datos,Tables.Notes._ID + " =?", args)
        db!!.close()
    }

    //Por implementarse
    fun getId(id: String): Note{

        var args = arrayOf(id)
        val db = this.readableDatabase
        /*var queryline = "SELECT * FROM "+Tables.Notes.TABLE_NAME+" WHERE " + Tables.Notes._ID + " = "+id*/
        val columns = arrayOf(Tables.Notes._ID,Tables.Notes.COLUMN_BODY,Tables.Notes.COLUMN_TITLE)
        var resultado = db!!.query(Tables.Notes.TABLE_NAME,columns,Tables.Notes._ID + " =?",args,null,null,null)

        var note = Note()

        if(resultado.moveToFirst())
        {
                note.setID(resultado.getString(resultado
                        .getColumnIndex(Tables.Notes._ID)).toInt())

                note.setBody(resultado.getString(resultado
                        .getColumnIndex(Tables.Notes.COLUMN_BODY)))

                note.setTitle(resultado.getString(resultado
                        .getColumnIndex(Tables.Notes.COLUMN_TITLE)))
    }
        db!!.close()
        return note
    }
}