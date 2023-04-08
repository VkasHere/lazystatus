package com.lazy.status

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.RotateAnimation
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.viewpager.widget.ViewPager
import com.belazy.lazystatus.R
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.lazy.status.adapter.Myadapter
import com.lazy.status.models.modelclasss
import kotlinx.android.synthetic.main.activity_imageview.*
import kotlinx.android.synthetic.main.dialogbox.view.*
import kotlinx.android.synthetic.main.fragment_images.*
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var switch: ImageButton
    private lateinit var refresh: ImageButton
    private lateinit var swtch: LinearLayout
    private  var screen:String="Whatsapp"
     lateinit var imageslist:ArrayList<modelclasss>
     lateinit var videoslist:ArrayList<modelclasss>
    var wapathfolder31:String="Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses"
    var wbpathfolder31:String="Android%2Fmedia%2Fcom.whatsapp.w4b%2FWhatsApp Business%2FMedia%2F.Statuses"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageslist=ArrayList()
        videoslist=ArrayList()

        tabLayout=findViewById(R.id.tablayout)
        viewPager=findViewById(R.id.viewpager)
        switch=findViewById(R.id.switchbox)
        refresh=findViewById(R.id.refresh)



        startactivity()

        refresh.setOnClickListener{
            val anim = RotateAnimation(30f, 360f, refresh.width/2f, refresh.height / 2f )
            anim.fillAfter = true
            anim.repeatCount = 0
            anim.duration = 1000
            refresh.startAnimation(anim)
            imageslist= arrayListOf<modelclasss>()
            videoslist= arrayListOf<modelclasss>()
            startactivity()
        }
//        swipeToRefreshLV.setOnRefreshListener {
//            swipeToRefreshLV.isRefreshing=false
//            imageslist= arrayListOf<modelclasss>()
//            videoslist= arrayListOf<modelclasss>()
//            startactivity()
//        }

        switch.setOnClickListener{
            val dialogbinding:View=LayoutInflater.from(this).inflate(R.layout.dialogbox,null)
            val dialogbox=Dialog(this)
            dialogbox.setContentView(dialogbinding)
            dialogbox.setCancelable(true)
            dialogbox.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogbox.show()
            val rg=dialogbinding.findViewById<RadioGroup>(R.id.radiogroup)
            if (screen=="Whatsapp"){  rg.check(R.id.r1)} else{ rg.check(R.id.r2)}
            dialogbinding.submit.setOnClickListener {
                val sel:Int=dialogbinding.radiogroup.checkedRadioButtonId
                screen= dialogbinding.findViewById<RadioButton>(sel).text.toString()
                val sharedPreferences=getSharedPreferences("Screens", MODE_PRIVATE)
                val myedit=sharedPreferences.edit()
                myedit.putString("Screen",screen)
                myedit.apply()
                dialogbox.dismiss()
                super.recreate()
//                finish()
//                startActivity(intent)
//                overridePendingTransition(0,1)
            }

        }



        tabLayout.addTab(tabLayout.newTab().setText("Images"))
        tabLayout.addTab(tabLayout.newTab().setText("Videos"))
        tabLayout.tabGravity=TabLayout.GRAVITY_FILL
        tabLayout.tabRippleColor = null

        val adapter=Myadapter(this,supportFragmentManager,tabLayout.tabCount)
        viewPager.adapter=adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem=tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

    }

     fun startactivity(){
        val shared=getSharedPreferences("Screens", MODE_PRIVATE)
        screen=shared.getString("Screen","Whatsapp")!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (screen=="Whatsapp"){
                switch.setImageResource(R.drawable.wa)
                if ( isAppInstalled("com.whatsapp")){
                    val sh: SharedPreferences = getSharedPreferences("DATA_PATH", Context.MODE_PRIVATE)
                    val uripath=sh.getString("Wapath","")
                    val result = readDataFromPrefs(uripath!!)
                    if (result) {
                        getmedia(uripath)
                    }else{
                        getfolderpermission()
                    }
                    val sh1=getSharedPreferences("installed", MODE_PRIVATE)
                    val myedit=sh1.edit()
                    myedit.putBoolean("Wainstall",true)
                    myedit.putString("app","wa")
                    myedit.apply()
                }else{
                    val sh1=getSharedPreferences("installed", MODE_PRIVATE)
                    val myedit=sh1.edit()
                    myedit.putBoolean("Wainstall",false)
                    myedit.putString("app","wa")
                    myedit.apply()
                }

            }else{
                switch.setImageResource(R.drawable.wb)
                if ( isAppInstalled("com.whatsapp.w4b")){

                    val sh: SharedPreferences = getSharedPreferences("DATA_PATH", Context.MODE_PRIVATE)
                    val uripath=sh.getString("WBpath","")
                    val result = readDataFromPrefs(uripath!!)
                    if (result) {
                        getmedia(uripath)
                    }else{
                        getfolderpermission()
                    }
                    val sh1=getSharedPreferences("installed", MODE_PRIVATE)
                    val myedit=sh1.edit()
                    myedit.putBoolean("Wbinstall",true)
                    myedit.putString("app","wb")
                    myedit.apply()
                }else{

                    val sh1=getSharedPreferences("installed", MODE_PRIVATE)
                    val myedit=sh1.edit()
                    myedit.putBoolean("Wbinstall",false)
                    myedit.putString("app","wb")
                    myedit.apply()
                }
            }


        }else{
            val view=layoutInflater.inflate(R.layout.viewimage,null)
            val button=view.findViewById<ImageButton>(R.id.share)
            button.visibility=View.GONE
            if (screen=="Whatsapp"){
                switch.setImageResource(R.drawable.wa)
                if ( !isAppInstalled("com.whatsapp")){
                    val sh1=getSharedPreferences("installed", MODE_PRIVATE)
                    val myedit=sh1.edit()
                    myedit.putBoolean("Wainstall",false)
                    myedit.putString("app","wa")
                    myedit.apply()
                }else{
                    val sh1=getSharedPreferences("installed", MODE_PRIVATE)
                    val myedit=sh1.edit()
                    myedit.putBoolean("Wainstall",true)
                    myedit.putString("app","wa")
                    myedit.apply()
                    getmediabelow("/WhatsApp/Media/.Statuses")
                }
            }else{
                switch.setImageResource(R.drawable.second)
                if ( !isAppInstalled("com.whatsapp.w4b")){
                    val sh1=getSharedPreferences("installed", MODE_PRIVATE)
                    val myedit=sh1.edit()
                    myedit.putBoolean("Wbinstall",false)
                    myedit.putString("app","wb")
                    myedit.apply()
                }else{
                    val sh1=getSharedPreferences("installed", MODE_PRIVATE)
                    val myedit=sh1.edit()
                    myedit.putBoolean("Wbinstall",true)
                    myedit.putString("app","wb")
                    myedit.apply()
                    getmediabelow("/WhatsApp Business/Media/.Statuses")
                }

            }

        }


        val sh=getSharedPreferences("urilist", MODE_PRIVATE)
        val myedit=sh.edit()
        val gson=Gson()
        val json1:String=gson.toJson(imageslist)
        val json2:String=gson.toJson(videoslist)
        myedit.putString("imagelist",json1)
        myedit.putString("videolist",json2)
        myedit.apply()

         val adapter=Myadapter(this,supportFragmentManager,tabLayout.tabCount)
         viewPager.adapter=adapter


    }
    private fun getmediabelow(path:String) {
        var modelclass:modelclasss
        val targetpath:String= Environment.getExternalStorageDirectory().absolutePath +path
        val targetdir= File(targetpath)
        val files = targetdir.listFiles()!!

        for (file in files) {
            if (!file.name.endsWith(".nomedia")) {
                if (file.name.endsWith(".jpg")) {
                    modelclass = modelclasss(file.name, Uri.fromFile(file).toString())
                    imageslist.add(modelclass)
                }else{
                    modelclass = modelclasss(file.name, Uri.fromFile(file).toString())
                    videoslist.add(modelclass)
                }
            }
//                    val file:File=Files[i]

        }
    }

    private fun readDataFromPrefs(uripath:String): Boolean {
        if (uripath.isEmpty()){
            return false
        }
        return true
    }

    private fun getfolderpermission() {
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.R) {
            val storageManager =
                application.getSystemService(Context.STORAGE_SERVICE) as StorageManager
            val intent = storageManager.primaryStorageVolume.createOpenDocumentTreeIntent()
            val target = if(screen=="Whatsapp") wapathfolder31 else wbpathfolder31
            var uri = intent.getParcelableExtra<Uri>("android.provider.extra.INITIAL_URI") as Uri
            var scheme = uri.toString()
            scheme = scheme.replace("/root/", "/tree/")
            scheme += "%3AAndroid%2Fmedia/document/primary%3A$target"
            uri = Uri.parse(scheme)
            intent.putExtra("android.provider.extra.INITIAL_URI", uri)
            intent.putExtra("android.content.extra.SHOW_ADVANCED", true)
            startActivityForResult(intent, 1234)
        }
    }
    private fun getmedia(treeUri:String){
        contentResolver?.takePersistableUriPermission(
            Uri.parse(treeUri),
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        val files = DocumentFile.fromTreeUri(
            applicationContext,
            Uri.parse(treeUri)
        )
        if (files != null) {
            for (file: DocumentFile in files.listFiles()) {
                if (!file.name!!.endsWith(".nomedia")) {
                    if (file.name!!.endsWith(".jpg")) {
                        val modelclasss = modelclasss(file.name!!, file.uri.toString())
                        imageslist.add(modelclasss)
                    }else{
                        val modelclasss = modelclasss(file.name!!, file.uri.toString())
                        videoslist.add(modelclasss)
                    }

                }
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?){
        super.onActivityResult(requestCode,resultCode,data)
        if (resultCode== RESULT_OK){
            val treeUri=data?.data
            val sharedPreferences=getSharedPreferences("DATA_PATH", MODE_PRIVATE)
            val myedit=sharedPreferences.edit()
            val foldername=if (screen=="Whatsapp") "Wapath" else "WBpath"

            myedit.putString(foldername,treeUri.toString())
            myedit.apply()
            if (treeUri!=null){
                contentResolver?.takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val files= DocumentFile.fromTreeUri(applicationContext,treeUri)
                if (files != null) {
                    startactivity()
//                    for (file:DocumentFile in files.listFiles()){
//                        if(!file.name!!.endsWith(".nomedia")){
//                            if (file.name!!.endsWith(".jpg")) {
//                                val modelclasss = modelclasss(file.name!!, file.uri.toString())
//                                imageslist.add(modelclasss)
//                            }else{
//                                val modelclasss = modelclasss(file.name!!, file.uri.toString())
//                                videoslist.add(modelclasss)
//                            }
//                        }
//                    }

                }
            }
        }
    }
    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (ignored: PackageManager.NameNotFoundException) {
            false
        }
    }

}