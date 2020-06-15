package com.example.absensid.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.absensid.R
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.activity_image_viewer.*
import java.io.File
import java.util.ArrayList

class ImageViewerActivity : AppCompatActivity() {

    private var bannerUrls: ArrayList<String> = arrayListOf()
    private var bannerBitmaps: ArrayList<Bitmap> = arrayListOf()
    private var isFile: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

        bannerUrls = intent.getStringArrayListExtra("urls")
        isFile = intent.getBooleanExtra("isFile", false)

        viewPager.adapter = ImageViewerAdapter()
        viewPager.currentItem = intent.getIntExtra("page", 0)

        close_layout.setOnClickListener {
            super.onBackPressed()
        }
        if (bannerUrls.size > 1) {
            circleIndicator.setViewPager(viewPager)
        }
        if (isFile) {
            for (item in bannerUrls) {
                val file = File(item)
                val img = BitmapFactory.decodeFile(file.absolutePath)
                val imgBitmap = Bitmap.createScaledBitmap(img, img.width, img.height, false)
                bannerBitmaps.add(imgBitmap)
            }
        }
    }

    internal inner class ImageViewerAdapter : PagerAdapter() {

        override fun getCount(): Int {
            return bannerUrls.size
        }

        override fun isViewFromObject(v: View, obj: Any): Boolean {
            return v === obj as PhotoView
        }

        override fun instantiateItem(container: ViewGroup, i: Int): Any {
            val photo = PhotoView(this@ImageViewerActivity)
            if (isFile) {
                Glide.with(this@ImageViewerActivity).load(bannerBitmaps[i])
            } else {
                Glide.with(this@ImageViewerActivity).load(bannerUrls[i])
            }
                .apply(
                    RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(photo)
            (container as ViewPager).addView(photo, 0)
            return photo
        }

        override fun destroyItem(container: ViewGroup, i: Int, obj: Any) {
            (container as ViewPager).removeView(obj as PhotoView)
        }
    }
}
