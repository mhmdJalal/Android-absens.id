package com.example.absensid.ui.teacher

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.absensid.R
import com.example.absensid.adapter.AbsensiAdapter
import com.example.absensid.data.Absensi
import com.example.absensid.ui.ImageViewerActivity
import kotlinx.android.synthetic.main.activity_teacher_absensi.*
import org.jetbrains.anko.startActivity

class TeacherAbsensiActivity : AppCompatActivity(), AbsensiAdapter.ItemEvents  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_absensi)

        val absensis = ArrayList<Absensi>()
        absensis.add(Absensi(1, "https://www.garnier.co.id/-/media/project/loreal/brand-sites/garnier/apac/id/pages/article-pages/tips-percaya-diri-saat-foto-selfie-dari-pevita-pearce/244-tips-percaya-diri-saat-foto-selfie-dari-pevita-pearce.png", "Jln.Raya Wangun Desa Sindangsari Kec. Bogor Timur Kota Bogor", "Senin, 1 Juni 2020"))
        absensis.add(Absensi(2, "https://cdn.idntimes.com/content-images/community/2020/06/ireneredvelvet-20200607-87-341f72b027011984eafc7294065d4c47.png", "Jln.Raya Wangun Desa Sindangsari Kec. Bogor Timur Kota Bogor", "Selasa, 2 Juni 2020"))
        absensis.add(Absensi(3, "https://www.beepdo.com/wp-content/uploads/2019/04/Screen-Shot-2019-04-17-at-6.42.34-PM.png", "Jln.Raya Wangun Desa Sindangsari Kec. Bogor Timur Kota Bogor", "Rabu, 3 Juni 2020"))
        absensis.add(Absensi(4, "https://cdn.idntimes.com/content-images/community/2020/06/gidleyuqi-20200607-135-611921b1208256b2d01bebd5cad5093c.png", "Jln.Raya Wangun Desa Sindangsari Kec. Bogor Timur Kota Bogor", "Kamis, 4 Juni 2020"))
        absensis.add(Absensi(5, "https://media.cdnandroid.com/item_images/982908/imagen-selfie-with-ronaldo-0big.jpg", "Jln.Raya Wangun Desa Sindangsari Kec. Bogor Timur Kota Bogor", "Jum'at, 5 Juni 2020"))
        absensis.add(Absensi(6, "https://www.garnier.co.id/-/media/project/loreal/brand-sites/garnier/apac/id/pages/article-pages/tips-percaya-diri-saat-foto-selfie-dari-pevita-pearce/244-tips-percaya-diri-saat-foto-selfie-dari-pevita-pearce.png", "Jln.Raya Wangun Desa Sindangsari Kec. Bogor Timur Kota Bogor", "Senin, 8 Juni 2020"))
        absensis.add(Absensi(7, "https://cdn.idntimes.com/content-images/community/2020/06/ireneredvelvet-20200607-87-341f72b027011984eafc7294065d4c47.png", "Jln.Raya Wangun Desa Sindangsari Kec. Bogor Timur Kota Bogor", "Selasa, 9 Juni 2020"))
        absensis.add(Absensi(8, "https://www.beepdo.com/wp-content/uploads/2019/04/Screen-Shot-2019-04-17-at-6.42.34-PM.png", "Jln.Raya Wangun Desa Sindangsari Kec. Bogor Timur Kota Bogor", "Rabu, 10 Juni 2020"))
        absensis.add(Absensi(9, "https://cdn.idntimes.com/content-images/community/2020/06/gidleyuqi-20200607-135-611921b1208256b2d01bebd5cad5093c.png", "Jln.Raya Wangun Desa Sindangsari Kec. Bogor Timur Kota Bogor", "Kamis, 11 Juni 2020"))
        absensis.add(Absensi(10, "https://media.cdnandroid.com/item_images/982908/imagen-selfie-with-ronaldo-0big.jpg", "Jln.Raya Wangun Desa Sindangsari Kec. Bogor Timur Kota Bogor", "Jum'at, 12 Juni 2020"))

        val adapter = AbsensiAdapter(this)
        recycler_absensi.adapter = adapter
        recycler_absensi.layoutManager = LinearLayoutManager(this)
        adapter.setData(absensis)

        image_back.setOnClickListener {
            super.onBackPressed()
        }
    }

    override fun onImageClicked(url: String, position: Int) {
        val urls = ArrayList<String>()
        urls.add(url)
        startActivity<ImageViewerActivity>("urls" to urls, "page" to position)
    }

    override fun onLocationClicked(lat: Double, long: Double) {
        val gmmIntentUri: Uri = Uri.parse("geo:$lat,$long")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.data = gmmIntentUri
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            val uri = "https://www.google.com/maps/search/?api=1&query=$lat,$long"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.data = Uri.parse(uri)
            startActivity(intent)
        }
    }
}
