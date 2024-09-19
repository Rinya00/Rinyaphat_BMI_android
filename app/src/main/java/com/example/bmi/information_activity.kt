package com.example.bmi

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.StrictMode
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

class information_activity : AppCompatActivity() {
    @SuppressLint("DefaultLocale")

    private lateinit var spinner: Spinner
    private lateinit var editTextHeight: EditText
    private lateinit var editTextWeight: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_information)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        spinner = findViewById<Spinner>(R.id.spinner)
        val creatGender = ArrayAdapter.createFromResource(
            this,
            R.array.Gender,
            android.R.layout.simple_spinner_item
        )

        creatGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(creatGender)

        editTextHeight = findViewById<EditText>(R.id.editTextHeight)
        editTextWeight = findViewById<EditText>(R.id.editTextWeight)
        val nextbutton = findViewById<Button>(R.id.nextbutton)

        nextbutton.setOnClickListener {
            val url: String = getString(R.string.root_url)

            val okHttpClient = OkHttpClient()
            val formBody: RequestBody = FormBody.Builder()
                .add("Gender", spinner.selectedItemId.toString())
                .add("Height", editTextHeight.text.toString())
                .add("Weight", editTextWeight.text.toString())
                .build()
            val request: Request = Request.Builder()
                .url(url)
                .post(formBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val data = JSONObject(response.body!!.string())
                if (data.length() > 0) {
                    val Index = data.getString("Index")
                    val message = "BMI ของคุณอยู่ในเกรณ์ $Index"
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("ระบบประเมินค่าBMI!!")
                    builder.setMessage(message)
                    builder.setNeutralButton("OK", clearText())
                    val alert = builder.create()
                    alert.show()


                }
            }

        }
    }



        private fun clearText(): DialogInterface.OnClickListener {
            return DialogInterface.OnClickListener { dialog, which ->
                spinner.setSelection(0)
                editTextHeight.text.clear()
                editTextWeight.text.clear()
            }

        }

}