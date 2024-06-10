package fr.epf.min1.gc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class CountryDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)

        val countryName = findViewById<TextView>(R.id.detail_country_name)
        val countryCapital = findViewById<TextView>(R.id.detail_country_capital)
        val countryRegion = findViewById<TextView>(R.id.detail_country_region)
        val countryFlag = findViewById<ImageView>(R.id.detail_country_flag)

        val intent = intent
        val name = intent.getStringExtra("EXTRA_NAME")
        val capital = intent.getStringExtra("EXTRA_CAPITAL")
        val region = intent.getStringExtra("EXTRA_REGION")
        val flagUrl = intent.getStringExtra("EXTRA_FLAG_URL")

        countryName.text = name
        countryCapital.text = capital ?: "N/A"
        countryRegion.text = region

        Glide.with(this)
            .load(flagUrl)
            .into(countryFlag)
    }
}