package com.example.absensid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.absensid.R
import com.example.absensid.data.Absensi
import com.example.absensid.util.gone
import com.example.absensid.util.underlineText
import com.example.absensid.util.visible
import kotlinx.android.synthetic.main.item_absensi.view.*

/**
 * @author Jalal
 * @2020
 */
class AbsensiAdapter(private val listener: ItemEvents) :
    RecyclerView.Adapter<AbsensiAdapter.ViewHolder>() {

    private var data: List<Absensi> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_absensi, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Absensi, position: Int) = with(itemView) {
            //itemView here
            if ((position + 1) % 2 == 0) {
                image_left.gone()
                image_right.visible()

                setSpace(left = false)

                Glide.with(context)
                    .load(item.imageUrl)
                    .into(image_right)

                image_right.setOnClickListener {
                    listener.onImageClicked(item.imageUrl, position)
                }
            } else {
                image_left.visible()
                image_right.gone()

                setSpace(left = true)

                Glide.with(context)
                    .load(item.imageUrl)
                    .into(image_left)

                image_left.setOnClickListener {
                    listener.onImageClicked(item.imageUrl, position)
                }
            }

            text_tanggal.text = item.tanggal
            text_tanggal.underlineText()
            val lokasi = "Lokasi : <u><font color=#42a5f5>${item.alamat}</font></u>"
            text_lokasi.text = HtmlCompat.fromHtml(lokasi, HtmlCompat.FROM_HTML_MODE_COMPACT)

            text_lokasi.setOnClickListener {
                listener.onLocationClicked(-6.664915, 106.774405)
            }
        }

        fun setSpace(left: Boolean = false) = with(itemView) {
            if (left) {
                val layoutParamTanggal = ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParamTanggal.endToStart = R.id.image_right
                layoutParamTanggal.startToEnd = R.id.image_left
                layoutParamTanggal.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParamTanggal.setMargins(20, 0, 20, 0)
                text_tanggal.layoutParams = layoutParamTanggal

                val layoutParamLokasi = ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParamLokasi.endToStart = R.id.image_right
                layoutParamLokasi.startToEnd = R.id.image_left
                layoutParamLokasi.topToBottom = R.id.text_tanggal
                layoutParamLokasi.setMargins(20, 10, 20, 0)
                text_lokasi.layoutParams = layoutParamLokasi
            } else {
                val layoutParamTanggal = ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParamTanggal.endToStart = R.id.image_right
                layoutParamTanggal.startToEnd = R.id.image_left
                layoutParamTanggal.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParamTanggal.setMargins(0, 0, 20, 0)
                text_tanggal.layoutParams = layoutParamTanggal

                val layoutParamLokasi = ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParamLokasi.endToStart = R.id.image_right
                layoutParamLokasi.startToEnd = R.id.image_left
                layoutParamLokasi.topToBottom = R.id.text_tanggal
                layoutParamLokasi.setMargins(0, 10, 20, 0)
                text_lokasi.layoutParams = layoutParamLokasi
            }
        }
    }

    fun setData(data: List<Absensi>) {
        this.data = data
        notifyDataSetChanged()
    }

    interface ItemEvents {
        fun onImageClicked(url: String, position: Int)
        fun onLocationClicked(lat: Double, long: Double)
    }
}