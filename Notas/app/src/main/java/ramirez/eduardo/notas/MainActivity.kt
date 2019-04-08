package ramirez.eduardo.notas

import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_guardar.setOnClickListener(){
            val titulo = txt_titulo.text.toString()
            if(titulo==""){
                Toast.makeText(this,"Error: Indique el título.", Toast.LENGTH_SHORT).show()
            }
            else {
                try {
                    val nombre = titulo + ".txt"
                    val fos = openFileOutput(nombre, Context.MODE_PRIVATE)
                    fos.write(txt_cuerpo.text.toString().toByteArray())
                    fos.close()
                    Toast.makeText(this, "¡Se guardó la nota!", Toast.LENGTH_SHORT).show()
                }catch (e: Exception){
                    Toast.makeText(this,"Error: No se pudo guardar.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        btn_leer.setOnClickListener() {

            var titulo = txt_titulo.text.toString()
            if (titulo == "") {
                Toast.makeText(this, "Error: Indique el título.", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    var ofi = openFileInput(titulo + ".txt")
                    var isr = InputStreamReader(ofi)
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
                    ofi.close()
                }catch (e: Exception){
                    Toast.makeText(this,"Error: No se econtró el archivo.", Toast.LENGTH_SHORT).show()
                    txt_cuerpo.setText("")
                }
            }
        }
        btn_eliminar.setOnClickListener(){
            var titulo = txt_titulo.text.toString()
            if(titulo == ""){
                Toast.makeText(this,"Error: Indique el título.", Toast.LENGTH_SHORT).show()
            }
            else {
                try {
                    val ofi = openFileInput(titulo + ".txt")
                    val archivo = File(filesDir, titulo + ".txt")
                    archivo.delete()
                    ofi.close()
                    Toast.makeText(this,"¡Archivo eliminado!", Toast.LENGTH_SHORT).show()
                }catch (e: Exception){
                    Toast.makeText(this,"Error: No se encontró el archivo.", Toast.LENGTH_SHORT).show()
                }
            }

        }
        btn_guardarExterno.setOnClickListener(){
            var titulo = txt_titulo.text.toString()
            var cuerpo =  txt_cuerpo.text.toString()
            if(titulo == "" || cuerpo == ""){
                Toast.makeText(this,"Error: Verifique que los campos no estan vacíos..", Toast.LENGTH_SHORT).show()

            }
            else{
                try {
                    if (isExternarStorageWritable() && verificarPermiso()) {
                        val archivo = File(Environment.getExternalStorageDirectory(), titulo + ".txt")
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
    fun isExternarStorageWritable(): Boolean{
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED.equals(state)
    }
    private fun verificarPermiso(): Boolean{
        var permiso = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        var verificacion =  ContextCompat.checkSelfPermission(this,permiso)
        return verificacion == PackageManager.PERMISSION_GRANTED
    }
}
