package ramirez.eduardo.notas

import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.lang.Exception
import java.util.jar.Manifest

class DetalleNota : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_guardar.setOnClickListener(){

            guardarExterno()
        }
        btn_leer.setOnClickListener() {

            leeExterno()
        }
        btn_eliminar.setOnClickListener(){
            eliminarExterno()

        }

    }
    fun ubicacion():String{
        var carpeta = File(Environment.getExternalStorageDirectory(), "Notas")
        if(carpeta.exists()){
            carpeta.mkdir()
        }
        return carpeta.absolutePath
    }
    fun isExternarStorageWritable(): Boolean{
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED.equals(state)
    }
    private fun verificarPermiso(): Boolean{
        var permiso = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        var verificacion =  ContextCompat.checkSelfPermission(this,permiso)
        return verificacion == PackageManager.PERMISSION_GRANTED
    }
    private fun leeExterno(){
        var nombre = txt_titulo.text.toString()
        var archivo = File(ubicacion(), nombre+".txt")


        if (nombre == "") {
            Toast.makeText(this, "Error: Indique el título.", Toast.LENGTH_SHORT).show()
        } else {
            try {
                var fis = FileInputStream(archivo)
                var isr = InputStreamReader(fis)
                var br = BufferedReader(isr)
                var sb = StringBuilder()
                var texto = br.readLine()
                while (texto != null) {
                    sb.append(texto + "\n")
                    texto = br.readLine()
                }
                txt_cuerpo.setText(sb)
                br.close()
                isr.close()
                fis.close()
            }catch (e: Exception){
                Toast.makeText(this,"Error: No se econtró el archivo.", Toast.LENGTH_SHORT).show()
                txt_cuerpo.setText("")
            }
        }
    }
    private fun eliminarExterno(){
        var nombre = txt_titulo.text.toString()
        if(nombre == ""){
            Toast.makeText(this,"Error: Indique el título.", Toast.LENGTH_SHORT).show()
        }
        else {
            try {
                val ofi = openFileInput(nombre + ".txt")
                val archivo = File(ubicacion(), nombre + ".txt")
                archivo.delete()
                ofi.close()
                Toast.makeText(this,"¡Archivo eliminado!", Toast.LENGTH_SHORT).show()
            }catch (e: Exception){
                Toast.makeText(this,"Error: No se encontró el archivo.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun guardarExterno(){
        var titulo = txt_titulo.text.toString()
        var cuerpo =  txt_cuerpo.text.toString()
        if(titulo == "" || cuerpo == ""){
            Toast.makeText(this,"Error: Verifique que los campos no estan vacíos..", Toast.LENGTH_SHORT).show()

        }
        else{
            try {
                if (isExternarStorageWritable() && verificarPermiso()) {
                    val archivo = File(ubicacion(), titulo + ".txt")
                    val fos = FileOutputStream(archivo)
                    fos.write(cuerpo.toByteArray())
                    fos.close()
                } else {
                    Toast.makeText(this, "Error: No se guardó el archivo", Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                Toast.makeText(this, "Error: No se guardó el archivo", Toast.LENGTH_SHORT).show()

            }
        }
    }
}
