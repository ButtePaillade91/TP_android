package fr.epf.min1.gc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

private const val TAG = "AddClientActivity"

class AddClientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_client)

        val lastNameEditText =
            findViewById<EditText>(R.id.add_client_last_name_edittext)
        val firstNameEditText =
            findViewById<EditText>(R.id.add_client_first_name_edittext)

        val genderRadioGroup = findViewById<RadioGroup>(R.id.add_client_gender_radiogroup)
        genderRadioGroup.check(R.id.add_client_gender_woman_radiobutton)

        val addButton = findViewById<Button>(R.id.add_client_button)

        val ageSeekbar = findViewById<SeekBar>(R.id.add_client_age_seekbar)
        //ageSeekbar.max = 65 -18
        val ageTextview = findViewById<TextView>(R.id.add_client_age_textview)
        val levelSpinner = findViewById<Spinner>(R.id.add_client_level_spinner)

        ageSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(s0: SeekBar?, progress: Int, p2: Boolean) {
                ageTextview.text = (progress).toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) = Unit
            override fun onStopTrackingTouch(p0: SeekBar?) = Unit

        })

        addButton.setOnClickListener{
            Log.d(TAG, "Nom : ${lastNameEditText.text} ")
            Log.d(TAG, "Prenom : ${firstNameEditText.text} ")

            var gender =
                if(genderRadioGroup.checkedRadioButtonId
                    == R.id.add_client_gender_woman_radiobutton) "F" else "H"

            Log.d(TAG, "Genre : ${gender}")
            Log.d(TAG,"Niveau : ${levelSpinner.selectedItem}")

            Toast.makeText(this, R.string.add_client_message, Toast.LENGTH_LONG).show()
            finish()
        }
    }
}