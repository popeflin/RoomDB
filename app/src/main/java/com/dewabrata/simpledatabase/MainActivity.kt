package com.dewabrata.simpledatabase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dewabrata.simpledatabase.roomdb.UserDatabase
import com.dewabrata.simpledatabase.roomdb.UserEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var userDatabase: UserDatabase

    lateinit var rv_user: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        
      

        userDatabase = UserDatabase.getInstance(this)
        rv_user = findViewById(R.id.rv_user)
        rv_user.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener(View.OnClickListener {

            startActivity(android.content.Intent(this, AddData::class.java))
        })

        

       loadData()

    }

    override fun onResume() {
        super.onResume()
       loadData()
}

 fun loadData() {
     CoroutineScope(Dispatchers.IO).launch {
         Log.d("Hasil Query =", userDatabase.userDao().getAllUser().toString())
         val adapterUser = AdapterUser(userDatabase.userDao().getAllUser(), {
             //update
                 user ->
             startActivity(Intent(this@MainActivity, AddData::class.java).putExtra("user", user))
         }, {
             //delete
             CoroutineScope(Dispatchers.IO).launch {
                 userDatabase.userDao().deleteUser(it)
                    loadData()


             }
         })
         runOnUiThread {

             rv_user.adapter = adapterUser
         }


     }
 }
}