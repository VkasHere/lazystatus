package com.lazy.status.screens

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.belazy.lazystatus.R
import com.lazy.status.models.modelclasss
import java.io.IOException
import java.io.OutputStream
import java.time.LocalDateTime


class videoview : AppCompatActivity() {
    private lateinit var  status: ArrayList<modelclasss>
    private lateinit var  videoplayer: VideoView
    private lateinit var  save: ImageButton
    private lateinit var  share: ImageButton
    private lateinit var  back: LinearLayout
    private  var  mediaController: MediaController?=null
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videoview)
        videoplayer=findViewById(R.id.videoplayer)
        save=findViewById(R.id.save)
        share=findViewById(R.id.share)
        back=findViewById(R.id.layout)
        status= intent.getParcelableArrayListExtra<modelclasss>("status")!!
        val position= intent.getIntExtra("position",0)


        back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val uri= Uri.parse(status[position].fileUri)
        videoplayer.setVideoURI(uri)
        videoplayer.requestFocus()
        videoplayer.setOnErrorListener { mp, i, i2 ->
            Toast.makeText(this,"An unknown error occured!",Toast.LENGTH_SHORT).show()
            false
        }
        videoplayer.setOnPreparedListener{mp->
            videoplayer.start()
            mp.setOnVideoSizeChangedListener { mediaPlayer, i, i2 ->
                mediaController= MediaController(this)
                videoplayer.setMediaController(mediaController)
                mediaController!!.setAnchorView(videoplayer)
            }
        }

        save.setOnClickListener{
            val inputstream=contentResolver.openInputStream(uri)
            val filename="${System.currentTimeMillis()}.mp4"
            try {
                val values= ContentValues()
                values.put(MediaStore.MediaColumns.DISPLAY_NAME,filename)
                values.put(MediaStore.MediaColumns.MIME_TYPE,"video/mp4")
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS+"/Videos/")
                val uri1=contentResolver.insert(MediaStore.Files.getContentUri("external"),values)
                val outputstream: OutputStream =uri1?.let { contentResolver.openOutputStream(it)}!!
                if (inputstream!=null){
                    outputstream.write(inputstream.readBytes())
                }
                outputstream.close()
                Toast.makeText(applicationContext,"Video saved in Gallery!",Toast.LENGTH_SHORT).show()

            }catch (e: IOException){
                Toast.makeText(applicationContext,"Something went wrong !!",Toast.LENGTH_SHORT).show()
            }
        }

        share.setOnClickListener{
            val shareintent = Intent(Intent.ACTION_SEND)
            shareintent.type = "video/mp4"
            shareintent.putExtra(
                "android.intent.extra.STREAM",
                uri
            )
            startActivity(Intent.createChooser(shareintent, "${LocalDateTime.now()}"))

        }

    }
}