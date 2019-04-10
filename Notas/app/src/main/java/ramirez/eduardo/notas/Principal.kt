package ramirez.eduardo.notas

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.content_principal.*
import kotlinx.android.synthetic.main.nota.view.*
import java.io.File

class Principal : AppCompatActivity() {
    var listaNotas = ArrayList<Nota>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->

           var intent = Intent(this, DetalleNota::class.java)
            intent.putExtra("EDICION",false)
            this.startActivityForResult(intent, 1)
        }
       actualizaLista()
    }
    private fun crearnota(){
        var archivos = File(ubicacion())
        archivos.walk().forEach {
            if(it.isFile) {
                var nota = Nota(it.name)
                listaNotas.add(nota)
            }
        }

        }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


            if (resultCode == Activity.RESULT_OK) {
               actualizaLista()
            }

    }

private fun actualizaLista(){
    listaNotas = ArrayList()
    crearnota()
    var adaptador = AdaptadorNota(this, listaNotas)
    listView.adapter = adaptador
}
    private fun ubicacion():String{
        var carpeta = File(Environment.getExternalStorageDirectory(), "Notas")
        if(!carpeta.exists()){
            carpeta.mkdir()
        }
        return carpeta.absolutePath
    }
private class AdaptadorNota:BaseAdapter{
    var context : Context? = null
    var notas : ArrayList<Nota>? = null
    constructor(context: Context, notas: ArrayList<Nota>){
        this.context = context
        this.notas = notas
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var layout = LayoutInflater.from(context)
        var vista = layout?.inflate(R.layout.nota, null)!!
        var nota = notas!![position]
        if(vista != null){
            vista.nota_nombre.text = nota.nombre
        }
        vista.setOnClickListener {
            val intent = Intent(this.context, DetalleNota::class.java)
            intent.putExtra("EDICION",true)
            intent.putExtra("tituloN", nota.nombre)
            val origin = this.context as Activity
            origin.startActivityForResult(intent, 1)

        }

        return vista
    }

    override fun getItem(position: Int): Any {
        return notas?.get(position) ?: "Error"
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return notas?.size ?:0
    }
}
}
