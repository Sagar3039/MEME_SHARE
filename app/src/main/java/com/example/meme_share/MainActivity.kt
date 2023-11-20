package com.example.meme_share

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide


class MainActivity : AppCompatActivity() {
    var currentImageurl : String? = null
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        load_meme()
    }
    private fun load_meme() {

        val queue =Volley.newRequestQueue(this)
        val url = "https://meme-api.com/gimme"
        val imageView =findViewById<ImageView>(R.id.imageView)

        val jsonObjectRequest =JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                currentImageurl = response.getString("url")
                Glide.with(this).load(currentImageurl).into(imageView)
            },
            Response.ErrorListener {
                Toast.makeText(this,"somthing wemt's wrong",Toast.LENGTH_LONG).show()

            })

        queue.add(jsonObjectRequest)

    }


    fun Next_meme(view: View) {
        load_meme()


    }
    fun downloadMeme(view: View) {
        val downloadButton = findViewById<Button>(R.id.downloadButton)

        // Check if the image URL is available
        if (currentImageurl != null) {
            val request = DownloadManager.Request(Uri.parse(currentImageurl))
            request.setTitle("Meme Download")
            request.setDescription("Downloading meme image")
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "meme_image.png")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)

            Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No image to download", Toast.LENGTH_SHORT).show()
        }
    }
    fun Share_meme(view: View) {
        val intent= Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"$currentImageurl")
        val chooser = Intent.createChooser(intent,"Share this meme using...")
        startActivity(chooser)
    }
}