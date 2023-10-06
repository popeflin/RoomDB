package com.dewabrata.simpledatabase

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dewabrata.simpledatabase.roomdb.UserEntity

class AdapterUser(var userList: List<UserEntity> , val clickListener:(UserEntity) -> Unit,val onlongclick:(UserEntity)->Unit): RecyclerView.Adapter<AdapterUser.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tv_nama = itemView.findViewById(R.id.tv_nama) as TextView
        val tv_email = itemView.findViewById(R.id.tv_email) as TextView
        val iv_photo = itemView.findViewById(R.id.iv_photo) as ImageView

        fun bind(user: UserEntity){
            with(itemView){
                tv_nama.text = user.nama
                tv_email.text = user.email
                Glide.with(context)
                    .load(user.photo)
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_photo)
                
            }

            
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterUser.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))
    }

    override fun onBindViewHolder(holder: AdapterUser.ViewHolder, position: Int) {

        holder.itemView.setOnLongClickListener {
            onlongclick(userList.get(position))
            true
        }
        holder.itemView.setOnClickListener {
            clickListener(userList.get(position))
        }
      return holder.bind(userList[position])
    }




    override fun getItemCount(): Int {
        return  userList.size
    }
}
