package com.developerscracks.sivapp.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class Permisos {
    fun checkCamaraPermiso(activity: Activity): Boolean{
        return if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)){
                false
            }else{
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), 0)
                true
            }
        }else{
            true
        }
    }


    fun checkEscrituraPermiso(activity: Activity): Boolean{
        return if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                false
            }else{
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
                true
            }
        }else{
            true
        }
    }
}