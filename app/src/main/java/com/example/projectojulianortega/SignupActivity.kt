package com.example.projectojulianortega

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
// import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.projectojulianortega.databinding.SignupMainBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    //   "binding" se refiere a un proceso mediante el cual se establece una conexión entre los componentes de la interfaz de usuario (UI) y el código de la lógica de la aplicación.

    private lateinit var binding: SignupMainBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = SignupMainBinding.inflate(layoutInflater)

        //Splash Screen code

       // Thread.sleep(3000)
       // installSplashScreen()

        setContentView(binding.root)// Establece el contenido de la actividad utilizando la vista raíz inflada (binding.root), lo que significa que el diseño definido en activity_main.xml se muestra en la pantalla cuando se lanza la actividad.

        firebaseAuth = FirebaseAuth.getInstance()


        // On click Listener



        binding.Ingresarbtn.setOnClickListener {

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                firebaseAuth.createUserWithEmailAndPassword(email, password)

                    .addOnCompleteListener(this){task ->
                        if (task.isSuccessful){

                            Toast.makeText(this, "Ingreso Exitoso", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, MainActivityListaDeMonedas::class.java)
                            startActivity(intent)
                            finish()

                        }else{

                            Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show()

                        }

                    }



            }else{

                Toast.makeText(this, "Ingrese email y contraseña", Toast.LENGTH_SHORT).show()

            }


        }


    }

}
