package com.ealejandroC.pruebacorreo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class MainActivity : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var btnEnviar: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editTextEmail = findViewById(R.id.editTextEmail)
        btnEnviar = findViewById(R.id.btnEnviar)

        btnEnviar.setOnClickListener {
            val correo = editTextEmail.text.toString().trim()

            if (correo.isNotEmpty()) {
                enviarCorreo(correo)
            } else {
                Toast.makeText(this, "Ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show()
            }
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun enviarCorreo(correo: String) {
        GlobalScope.launch(Dispatchers.IO) {
        val propiedades = System.getProperties()
            propiedades["mail.smtp.auth"] = "true"
            propiedades["mail.smtp.starttls.enable"] = "true"
            propiedades["mail.smtp.host"] = "smtp.gmail.com" // Cambia esto por el host SMTP que quieras usar
            propiedades["mail.smtp.port"] = "587" // Puerto SMTP (587 es el puerto estándar para TLS/STARTTLS)

        val sesion = Session.getInstance(propiedades, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("correo", "contraseña_aplicacion")
            }
        })

        try {
            val mensaje = MimeMessage(sesion)
            mensaje.setFrom(InternetAddress("correo"))
            mensaje.addRecipient(Message.RecipientType.TO, InternetAddress(correo))
            mensaje.subject = "Asunto del correo"
            mensaje.setText("Este es el contenido del correo.")

            Transport.send(mensaje)
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }
    }
}