package com.dewabrata.simpledatabase

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dewabrata.simpledatabase.roomdb.UserDatabase
import com.dewabrata.simpledatabase.roomdb.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions
import java.util.UUID

class AddData : AppCompatActivity() {
    lateinit var userDatabase: UserDatabase
    lateinit var tv_nama: EditText
    lateinit var tv_email: EditText
    lateinit var btnCamera : Button
    lateinit var btnSimpan : Button
    lateinit var potrait : ImageView
    lateinit var imagepath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)
        val data = intent.getParcelableExtra<UserEntity>("user")

        userDatabase = UserDatabase.getInstance(this)

        tv_nama = findViewById(R.id.tv_name)
        tv_email = findViewById(R.id.editTextTextEmailAddress)
        btnCamera = findViewById(R.id.btnCaptureImage)
        btnSimpan = findViewById(R.id.btnSendData)
        potrait = findViewById(R.id.potrait)

        if(data!=null){

            tv_nama.setText(data.nama)
            tv_email.setText(data.email)
            Glide.with(this)
                .load(data.photo)
                .apply(RequestOptions.circleCropTransform())
                .into(potrait)
            imagepath = data.photo
        }

        btnCamera.setOnClickListener(View.OnClickListener {

            openCamera()

        })
        findViewById<Button>(R.id.btnPickFile).setOnClickListener(View.OnClickListener {
             val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 2)        })

        btnSimpan.setOnClickListener(View.OnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                userDatabase.userDao().insertUser(
                    UserEntity(
                        0,
                        tv_nama.text.toString(),
                        tv_email.text.toString(),
                        imagepath
                    )
                )




            }
            finish()


        })


    }

    fun openCamera(){

        if (EasyPermissions.hasPermissions(this, android.Manifest.permission.CAMERA)){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
             startActivityForResult(intent, 1)
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "Need permission for camera",
                0, android.Manifest.permission.CAMERA)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK){
            val extras = data?.extras
            val imageBitmap = extras?.get("data")
            potrait.setImageBitmap(imageBitmap as android.graphics.Bitmap?)
            imagepath = saveToInternalStorage(this, imageBitmap, UUID.randomUUID().toString()+"profile.png")


        }else if(requestCode ==2 && resultCode == AppCompatActivity.RESULT_OK){
            val uri = data?.data
            potrait.setImageURI(uri)
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            imagepath = saveToInternalStorage(this, bitmap, UUID.randomUUID().toString()+"profile.png")
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults)
    }

    fun saveToInternalStorage(context: Context, bitmap: Bitmap?, filename: String) : String{
        context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, it)
            it.flush()
            it.close()

        }
        return context.filesDir.absolutePath + "/" + filename

    }
}