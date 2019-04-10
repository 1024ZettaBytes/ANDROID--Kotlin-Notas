package ramirez.eduardo.notas

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.detalle_nota_activity.*
import java.io.*
import java.lang.Exception
import java.util.jar.Manifest

class DetalleNota : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.detalle_nota_activity)



        val esEdicion = intent.getBooleanExtra("EDICION",false)



        if (esEdicion){
            var titulo = intent.getStringExtra("tituloN")
            titulo = titulo.substring(0, titulo.length-4)
            entrada_titulo.setText(titulo)
            leeExterno()
            entrada_titulo.isEnabled = false
            btn_guarda.setOnClickListener() {
                guardarExterno()
                setResult(Activity.RESULT_OK)
            }
            btn_elimina.setOnClickListener(){
                eliminarExterno()
                setResult(Activity.RESULT_OK)
                finish()
            }

        }
        else{
            btn_elimina.setText("Cancelar")
            btn_guarda.setOnClickListener() {
                guardarExterno()
                setResult(Activity.RESULT_OK)
                finish()
            }
            btn_elimina.setOnClickListener(){
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }

    }
    fun ubicacion():String{
        var carpeta = File(Environment.getExternalStorageDirectory(), "Notas")
        if(!carpeta.exists()){
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
        var nombre = entrada_titulo.text.toString()
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
                entrada_cuerpo.setText(sb)
                br.close()
                isr.close()
                fis.close()
            }catch (e: Exception){
                Toast.makeText(this,"Error: No se econtró el archivo.", Toast.LENGTH_SHORT).show()
                entrada_cuerpo.setText("")
            }
        }
    }
    private fun eliminarExterno(){
        var nombre = entrada_titulo.text.toString()
        if(nombre == ""){
            Toast.makeText(this,"Error: Indique el título.", Toast.LENGTH_SHORT).show()
        }
        else {
            try {

                val archivo = File(ubicacion(), nombre + ".txt")
                archivo.delete()

                Toast.makeText(this,"¡Archivo eliminado!", Toast.LENGTH_SHORT).show()
            }catch (e: Exception){
                Toast.makeText(this,"Error: No se encontró el archivo.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun guardarExterno(){
        var titulo = entrada_titulo.text.toString()
        var cuerpo =  entrada_cuerpo.text.toString()
        if(titulo == "" || cuerpo == ""){
            Toast.makeText(this,"Error: Verifique que los campos no estan vacíos..", Toast.LENGTH_SHORT).show()

        }
        else{
            try {
                if (isExternarStorageWritable() && verificarPermiso()) {
                    val archivo = File(ubicacion(), titulo + ".txt")
                    if(archivo.exists()) {
                        archivo.delete()
                    }
                    val fos = FileOutputStream(archivo)
                    fos.write(cuerpo.toByteArray())
                    fos.close()
                    Toast.makeText(this, "¡Bien! Archivo guardado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error: No se guardó el archivo", Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                Toast.makeText(this, "Error: No se guardó el archivo", Toast.LENGTH_SHORT).show()

            }
        }
    }
}
