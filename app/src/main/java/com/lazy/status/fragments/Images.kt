package com.lazy.status.fragments

import android.annotation.SuppressLint
import android.app.Activity.MODE_PRIVATE
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.belazy.lazystatus.R
import com.lazy.status.adapter.StatusAdapter
import com.lazy.status.models.modelclasss
import com.lazy.status.screens.Imageview
import java.io.File
import java.lang.reflect.Type

class Images : Fragment() {
    private lateinit var rvStatusList:RecyclerView
    private lateinit var nostatus:LinearLayout
    private lateinit var noapp:LinearLayout
    private lateinit var inst1:TextView
    private lateinit var inst2:TextView
    private lateinit var inst:TextView
    private lateinit var sometext:TextView
    private lateinit var StatusList:ArrayList<modelclasss>
    private lateinit var StatusAdapter:StatusAdapter
    private lateinit var Files:Array<File>
    var install:Boolean=true
    var stg1:String="1. Open Whatsapp and Watch Status!"
    var stg2:String="2. Then, Come back and save them!"
    var sometxt:String=""
    var stg:String="The Status file will be deleted after 24 hours of publishing so please save it before.you can find saved status in your gallery."
//    val args = requireArguments()
    private lateinit var imagelist : ArrayList<modelclasss>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_images, container, false )


    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvStatusList= view.findViewById(R.id.imagelist)
        nostatus= view.findViewById(R.id.nostatus)
        noapp= view.findViewById(R.id.noapp)
        inst1= view.findViewById(R.id.inst1)
        inst2= view.findViewById(R.id.inst2)
        inst= view.findViewById(R.id.inst)
        sometext= view.findViewById(R.id.sometext)
        StatusList=ArrayList()
        inst1.text = stg1
        inst2.text = stg2
        inst.text = stg

        val sh=requireContext().getSharedPreferences("urilist", MODE_PRIVATE)
        val gson= Gson()
        val imagelisturi=sh.getString("imagelist",null)
        val type:Type= object :TypeToken<ArrayList<modelclasss>>(){}.type
        imagelist=gson.fromJson(imagelisturi,type)
        val viewimage:View=layoutInflater.inflate(R.layout.viewimage,null)
        val imagev=viewimage.findViewById<View>(R.id.imageview)
//        if (SDK_INT>=Build.VERSION_CODES.R) {
//            val result = readDataFromPrefs()
//            if (result) {
//                val sh = context?.getSharedPreferences("DATA_PATH", MODE_PRIVATE)
//                val treeUri = sh?.getString("PATH", "")
//
//                context?.contentResolver?.takePersistableUriPermission(
//                    Uri.parse(treeUri),
//                    Intent.FLAG_GRANT_READ_URI_PERMISSION
//                )
//                if (treeUri != null) {
//                    val files = DocumentFile.fromTreeUri(
//                        requireContext().applicationContext,
//                        Uri.parse(treeUri)
//                    )
//                    if (files != null) {
//                        for (file: DocumentFile in files.listFiles()) {
//                            if (!file.name!!.endsWith(".nomedia")) {
//                                if (file.name!!.endsWith(".jpg")) {
//                                    val modelclasss = modelclasss(file.name!!, file.uri.toString())
//                                    StatusList.add(modelclasss)
//                                }
//
//                            }
//                        }
//                        if (StatusList.isNotEmpty()){
//                            nostatus.visibility=View.GONE
//                            setuprecyclerview(StatusList)
//                        }else{
//                            nostatus.visibility=View.VISIBLE
//                        }
////                    val gson = Gson()
////                    val json = gson.toJson(urilist)//converting list to Json
////                    val editor:SharedPreferences.Editor =  sharedPreferences.edit()
////                    editor.putString("flutter.list",json)
////                    editor.apply()
//
//                    }
//
//
//
//                }
//
//
//            } else {
//                getfolderpermission()
//            }
            val sh1= requireContext().getSharedPreferences("installed", MODE_PRIVATE)
            val app=sh1.getString("app","")
           if (app=="wa"){

               sometxt="Sorry! WhatsApp is not install on your device."
               install = sh1.getBoolean("Wainstall",false)
            }else{
               sometxt="Sorry! WhatsApp Business is not install on your device."
               install = sh1.getBoolean("Wbinstall",false)
            }
            sometext.text = sometxt
            if (install){
                noapp.visibility=View.GONE
                if (imagelist.isNotEmpty()){
                    nostatus.visibility=View.GONE
                    setuprecyclerview(imagelist)
                }else{
                    nostatus.visibility=View.VISIBLE
                }
            }else{
                noapp.visibility=View.VISIBLE
            }

//        }

//        else{
////            if (ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
////                ActivityCompat.requestPermissions(requireActivity(),arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),100)
////            }else{
////                var modelclass:modelclasss
////                val targetpath:String=Environment.getExternalStorageDirectory().absolutePath +"/WhatsApp/Media/.Statuses"
////                val targetdir=File(targetpath)
////                Files= targetdir.listFiles()!!
////
////                    nostatus.visibility=View.GONE
////                    for (file in Files) {
////                        if (!file.name.endsWith(".nomedia")) {
////                            if (file.name.endsWith(".jpg")) {
////                                modelclass = modelclasss(file.name, Uri.fromFile(file).toString())
////                                StatusList.add(modelclass)
////                            }
////                        }
//////                    val file:File=Files[i]
////
////                    }
////                    if (StatusList.isNotEmpty()){
////                        nostatus.visibility=View.GONE
////                        setuprecyclerview(StatusList)
////                    }else{
////                        nostatus.visibility=View.VISIBLE
////                    }
////
////            }
//
//            if (imagelist.isNotEmpty()){
//                nostatus.visibility=View.GONE
//                setuprecyclerview(imagelist)
//            }else{
//                nostatus.visibility=View.VISIBLE
//            }
//        }
    }
    private fun getfolderpermission() {
        if (SDK_INT>=Build.VERSION_CODES.R) {
            val storageManager =
                requireActivity().application.getSystemService(Context.STORAGE_SERVICE) as StorageManager
            val intent = storageManager.primaryStorageVolume.createOpenDocumentTreeIntent()
            val target = "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses"
            var uri = intent.getParcelableExtra<Uri>("android.provider.extra.INITIAL_URI") as Uri
            var scheme = uri.toString()
            scheme = scheme.replace("/root/", "/tree/")
            scheme += "%3A$target"
            uri = Uri.parse(scheme)
            intent.putExtra("android.provider.extra.INITIAL_URI", uri)
            intent.putExtra("android.content.extra.SHOW_ADVANCED", true)
            startActivityForResult(intent, 1234)
        }
    }

    private fun readDataFromPrefs(): Boolean {

        val sh:SharedPreferences= requireContext().getSharedPreferences("DATA_PATH", Context.MODE_PRIVATE)
        val uripath=sh.getString("PATH","")
        if (uripath!=null){
            if (uripath.isEmpty()){
                return false
            }
        }
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?){
        super.onActivityResult(requestCode,resultCode,data)
        if (resultCode== RESULT_OK){
            val treeUri=data?.data
            val sharedPreferences=requireContext().getSharedPreferences("DATA_PATH", MODE_PRIVATE)
            val myedit=sharedPreferences.edit()
            myedit.putString("PATH",treeUri.toString())
            myedit.apply()
            if (treeUri!=null){
                context?.contentResolver?.takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val files= DocumentFile.fromTreeUri(requireContext().applicationContext,treeUri)
                if (files != null) {
                    for (file:DocumentFile in files.listFiles()){
                        if(!file.name!!.endsWith(".nomedia")){
                            val modelclasss=modelclasss(file.name!!,file.uri.toString())
                            StatusList.add(modelclasss)
                        }
                    }
                    setuprecyclerview(StatusList)
//                    val gson = Gson()
//                    val json = gson.toJson(urilist)//converting list to Json
//                    val editor:SharedPreferences.Editor =  sharedPreferences.edit()
//                    editor.putString("flutter.list",json)
//                    editor.apply()

                }

            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun setuprecyclerview(statusList: ArrayList<modelclasss>) {
        StatusAdapter= StatusAdapter(requireContext(), statusList,true)
            rvStatusList.apply {
                setHasFixedSize(true)
                layoutManager=StaggeredGridLayoutManager(3,LinearLayoutManager.VERTICAL)
                adapter=StatusAdapter
            }
        rvStatusList.adapter=StatusAdapter
        StatusAdapter.onitemclick={
            val intent= Intent(requireContext(), Imageview::class.java)
            intent.putExtra("status",statusList)
            intent.putExtra("position",statusList.indexOf(it))
            startActivity(intent)
        }
    }
}