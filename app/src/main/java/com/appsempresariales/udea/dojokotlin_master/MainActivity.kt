package com.appsempresariales.udea.dojokotlin_master

import android.content.Context
import com.appsempresariales.udea.models.Note
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import com.appsempresariales.udea.database.DBHelper
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    internal var lvsLista: ListView ?=null // Lista referente al listView en la que se muestran las notas
    var notes : MutableList<Note> = ArrayList() // Array que almacena todos los objetos Note las notas (Se debe llenar con el array que retorna el método read de la BD)
    private var vibrator : Vibrator?=null
    private var db: DBHelper ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //    setSupportActionBar(toolbar)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator ?
        lvsLista = findViewById(R.id.lista) // Se instancia el listView

         //Agrega al adapter los títulos de las notas por medio del método toString en la clase Note
         //Ingresa la configuración del adaptador al listView

        lvsLista!!.onItemClickListener = this
        lvsLista!!.onItemLongClickListener = this
        //Aquí se deben crear los metodos click listener para el listView

       fab.setOnClickListener {  // Botón para agregar una nueva nota
           val open = Intent(this,NewNote::class.java)
           startActivity(open) //Cambia a la actividad new_note.xml
        }

        val context = this
        db = DBHelper(context)
        notes= db!!.getNotes()
        refrescar()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        //Aqui se debe invocar la función que tome los datos
        var nota : Note = notes.get(p2)
        //Los pase por un intend, y actualiza la nota
        val open = Intent(this,NewNote::class.java)
        open.putExtra("notaID",nota.getID().toString())
        startActivity(open)    }

    override fun onItemLongClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(3,10))
            alertDialog(p2)
        }
        else
        {
            vibrator?.vibrate(50)
            alertDialog(p2)
        }
        return true
    }

    private fun alertDialog(posicion : Int)
    {
        var alert = AlertDialog.Builder(this)
        alert.setTitle(R.string.app_alertTitle)
                .setPositiveButton("Eliminar"){dialog, which ->
                    db!!.deleteNote(notes.get(posicion))
                    refrescar()
                }
                .setNegativeButton("Cancelar"){dialog, whic ->
                }
                .show()
    }

    private fun refrescar()
    {
        notes = db!!.getNotes()
        var adapter = ArrayAdapter<Note>(this,android.R.layout.simple_list_item_1,notes)
        lvsLista!!.adapter= adapter as ListAdapter?
    }
}
