package ramirez.eduardo.notas

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

import kotlinx.android.synthetic.main.activity_principal.*
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
            this.startActivity(intent)
        }
    }
    private fun crearnota(){
        val archivos = File(ubicacion(), "Notas").listFiles()
        for(i in archivos){
var nota = Nota(i.name)
            listaNotas.add(nota)
        }



    }
    private fun ubicacion():String{
        var carpeta = File(Environment.getExternalStorageDirectory(), "Notas")
        if(carpeta.exists()){
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
            val intent = Intent(this.context, DetallePelicula::class.java)
            intent.putExtra("nombreP", pel.nombre)
            intent.putExtra("descripcionP", pel.descripcion)
            intent.putExtra("imagenP", pel.imagen)
            context!!.startActivity(intent)
        }

        return vista
    }

    override fun getItem(position: Int): Any {
        return peliculas?.get(position) ?: "Error"
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return peliculas?.size ?:0
    }
}
}
