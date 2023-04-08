package com.lazy.status.adapter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.lazy.status.fragments.Images
import com.lazy.status.fragments.Videos
import com.lazy.status.models.modelclasss

internal class Myadapter(var context: Context,fm:FragmentManager,var totaltabs:Int):FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
    return when(position){
        0 ->{
           Images()
        }
        1 ->{
            Videos()
        }
        else ->getItem(position)
    }
    }
    override fun getCount(): Int {
        return totaltabs
    }
}