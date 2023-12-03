package com.techno.baihai.activity.kotlin

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import com.techno.baihai.R
import com.techno.baihai.activity.kotlin.ProductDonateActivity.Companion.task


class ProductAdapter(mContext: Context, private var imgArrayList: MutableList<String>) :
    SliderViewAdapter<ProductAdapter.SliderAdapterVH>() {

    var context: Context = mContext
    private var itemClickListener: OnClickListener? = null


    interface OnClickListener {
        fun onItemClick(currentItem: MutableList<String>)
    }

    // Setter method for item click listener
    fun setOnItemClickListener(listener: OnClickListener) {
        this.itemClickListener = listener
    }
    private fun deleteItem(position: Int) {
        imgArrayList.removeAt(position)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.image_slider_layout_item, null)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        Log.e("image=", "" + imgArrayList[position])
        val item= imgArrayList[position]
        if (item!=null){
            Glide.with(context).load(item)
                .error(R.color.quantum_amber200)
                .into(viewHolder.imageViewBackground)
        }

//        val imgFile = imgArrayList[position]?.let { File(it) }
//        if (imgFile != null) {
//            if (imgFile.exists()) {
//                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
//                viewHolder.imageViewBackground.setImageBitmap(myBitmap)
//            }
//        }
        viewHolder.btnRemoveImage.setOnClickListener {
            if (count != -1) {
                deleteItem(position)
                notifyDataSetChanged()
                if (count == 0) {
                    task()
                    notifyDataSetChanged()
                }
            } else {
                task()
            }
            itemClickListener?.onItemClick(imgArrayList)
        }
    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return imgArrayList.size
    }

    class SliderAdapterVH(itemView: View) :
        ViewHolder(itemView) {
        private var itemViews: View? = null
        var imageViewBackground: ImageView
        private var imageGifContainer: ImageView
        private var textViewDescription: TextView
        var btnRemoveImage: ImageView

        init {
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider)
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container)
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider)
            btnRemoveImage = itemView.findViewById(R.id.btn_removeimage)
            btnRemoveImage.visibility = View.VISIBLE
            this.itemViews = itemView
        }
    }
}