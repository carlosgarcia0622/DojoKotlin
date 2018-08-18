package com.appsempresariales.udea.models

import android.util.Log
import com.appsempresariales.udea.database.DBHelper

class Note {

    //Atributos de la clase
    private var title: String = ""
    private var body:String = ""
    private var id : Int = 0

    //Constructor para iniciliazar con titulo y cuerpo
    constructor(title: String, body: String){
        this.title = title
        this.body = body
    }

    //Constructor vacio para inicializar mas facil
    constructor()
    {}

    //Funciones getter and setter
    fun getTitle():String{
        return this.title
    }

    fun getBody(): String{
        return this.body
    }

    fun getID() : Int
    {
        return this.id
    }

    fun setTitle(title : String)
    {
        this.title  = title
    }

    fun setBody(body : String)
    {
        this.body = body
    }

    fun setID(id : Int)
    {
        this.id = id
    }

    //Método que se encarga de mostar el titulo en la vista, por medio del ArrayAdapter
    override fun toString(): String {
        return "${this.title}"
    }
}