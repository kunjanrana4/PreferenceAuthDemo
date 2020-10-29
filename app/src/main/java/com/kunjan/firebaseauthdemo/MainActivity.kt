package com.kunjan.firebaseauthdemo

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)

    val login:Button = findViewById(R.id.login)
    private val register:Button = findViewById(R.id.register)
    private val logout:Button = findViewById(R.id.logout)
    private val user:EditText = findViewById(R.id.username)
    private val password = findViewById<EditText>(R.id.password)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        val sharedPreferences = EncryptedSharedPreferences.create(
                "shared_preferences_filename",
                masterKeyAlias,
                this,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        //auth = Firebase.auth

//        val providers = arrayListOf(
//            AuthUI.IdpConfig.EmailBuilder().build(),
//            AuthUI.IdpConfig.GoogleBuilder().build())
//
//        startActivityForResult(AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setAvailableProviders(providers)
//                .build(),1)

        login.setOnClickListener {
            val encryptedPassword = sharedPreferences.getString(user.text.toString(), "null")
            if (password.text.toString() == encryptedPassword) {
                Toast.makeText(this, "You have successfully logged in.", Toast.LENGTH_LONG).show()
                updateUI()
            }else {
                Toast.makeText(this, "Login Unsuccessful. Please try again.", Toast.LENGTH_LONG).show()
            }
        }

        register.setOnClickListener{
            sharedPreferences.edit()
                    .putString(user.text.toString(), password.text.toString())
                    .apply()
            Toast.makeText(this, "You have successfully registered.", Toast.LENGTH_LONG).show()
        }

        logout.setOnClickListener{
            Toast.makeText(this, "You have successfully logged out.", Toast.LENGTH_LONG).show()
            logout.visibility = View.GONE
            login.visibility = View.VISIBLE
            register.visibility = View.VISIBLE
            user.visibility = View.VISIBLE
            password.visibility = View.VISIBLE
        }
    }

    private fun updateUI() {
        Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show()
        user.text.clear()
        password.text.clear()
        logout.visibility = View.VISIBLE
        login.visibility = View.GONE
        register.visibility = View.GONE
        user.visibility = View.GONE
        password.visibility = View.GONE
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == 1) {
//            val response = IdpResponse.fromResultIntent(data)
//            if (resultCode == Activity.RESULT_OK) {
//                val user = FirebaseAuth.getInstance().currentUser
//            } else {
//                Toast.makeText(this, "Username or password is incorrect.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}