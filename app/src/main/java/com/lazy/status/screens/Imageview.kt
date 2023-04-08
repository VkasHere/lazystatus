package com.lazy.status.screens

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import com.belazy.lazystatus.R
import com.lazy.status.adapter.StatusAdapter
import com.lazy.status.models.modelclasss
import java.io.*


class Imageview : AppCompatActivity() {
    private lateinit var imageviewrecycler: RecyclerView
    private lateinit var StatusAdapter: StatusAdapter
    private lateinit var  status: ArrayList<modelclasss>
    private lateinit var  back:LinearLayout
    private lateinit var  savebtn:ImageButton
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imageview)

        status= intent.getParcelableArrayListExtra<modelclasss>("status")!!
        val position= intent.getIntExtra("position",0)
        back=findViewById(R.id.layout)
        imageviewrecycler= findViewById(R.id.viewimagelist)
//        savebtn= findViewById(R.id.save)

        StatusAdapter= StatusAdapter(this, status,false)
        imageviewrecycler.apply {
            setHasFixedSize(true)
            layoutManager= StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL)
            adapter=StatusAdapter
            val snapHelper: SnapHelper = PagerSnapHelper()
            imageviewrecycler.layoutManager = layoutManager
            snapHelper.attachToRecyclerView(imageviewrecycler)
        }

        back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        imageviewrecycler.layoutManager?.scrollToPosition(position)
        imageviewrecycler.adapter=StatusAdapter
        StatusAdapter.onitemclick={
            if (it.fileUri!!.endsWith(".mp4")){
                val inputstream=contentResolver.openInputStream(Uri.parse(it.fileUri))
                val filename="${System.currentTimeMillis()}.mp4"
                try {
                    val values=ContentValues()
                    values.put(MediaStore.MediaColumns.DISPLAY_NAME,filename)
                    values.put(MediaStore.MediaColumns.MIME_TYPE,"video/mp4")
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_DOCUMENTS+"/Videos/")
                    val uri=contentResolver.insert(MediaStore.Files.getContentUri("external"),values)
                    val outputstream:OutputStream=uri?.let { contentResolver.openOutputStream(it)}!!
                    if (inputstream!=null){
                        outputstream.write(inputstream.readBytes())
                    }
                    outputstream.close()
                }catch (e:IOException){
                    Toast.makeText(applicationContext,"Something went wrong !!",Toast.LENGTH_SHORT).show()
                }
            }else{
                val bitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,Uri.parse(it.fileUri))
                val filename="${System.currentTimeMillis()}.jpg"
                var fos:OutputStream?=null
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                    contentResolver.also {resolver->
                        val contentvalues=ContentValues().apply {
                            put(MediaStore.MediaColumns.DISPLAY_NAME,filename)
                            put(MediaStore.MediaColumns.MIME_TYPE,"image/jpg")
                            put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES)
                        }
                        val imageurl:Uri?=resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentvalues)
                        fos=imageurl?.let{resolver.openOutputStream(it)}
                    }
                }else{
                    val imagedir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    val image=File(imagedir,filename)
                    fos=FileOutputStream(image)
                }
                fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG,100,it) }
                Toast.makeText(applicationContext,"Image saved!!",Toast.LENGTH_SHORT).show()

            }
        }

        StatusAdapter.onshareclick={
            val icon: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,Uri.parse(it.fileUri))
            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/jpeg"
            val bytes = ByteArrayOutputStream()
            icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val f = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + "temporary_file.jpg"
            )
            try {
                f.createNewFile()
                val fo = FileOutputStream(f)
                fo.write(bytes.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
            }
            share.putExtra(Intent.EXTRA_STREAM, Uri.parse(it.fileUri))
            startActivity(Intent.createChooser(share, "Share Image"))
         }
//       val posn= (imageviewrecycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
       val uri=status[0].fileUri
//        savebtn.setOnClickListener {
//            val bitmap:Bitmap= MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.parse(uri))
//            val external=Environment.getExternalStorageDirectory().toString()
//            val fo:OutputStream
//            try {
//                val resolver=contentResolver
//                val contentValues=ContentValues()
//                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,"Images"+".jpg")
//                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES)
//
//            }catch (_:IOException){}
//            val File= File(external,"test.jpg")
//        }
    }
}