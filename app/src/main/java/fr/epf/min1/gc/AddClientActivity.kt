package fr.epf.min1.gc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

private const val TAG = "AddClientActivity"

class AddClientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_client)

        val lastNameEditText =
            findViewById<EditText>(R.id.add_client_last_name_edittext)
        val firstNameEditText =
            findViewById<EditText>(R.id.add_client_first_name_edittext)

        val addButton = findViewById<Button>(R.id.add_client_button)

        addButton.setOnClickListener{
            Log.d(TAG, "Nom : ${lastNameEditText.text} ")
            Log.d(TAG, "Prenom : ${firstNameEditText.text} ")
        }
    }
}