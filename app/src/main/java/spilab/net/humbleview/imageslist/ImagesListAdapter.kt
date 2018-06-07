package spilab.net.humbleview.imageslist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import spilab.net.humbleview.R
import spilab.net.humbleimageview.HumbleImageView

class ImagesListAdapter(private val imagesUrls: Array<String>) :
        RecyclerView.Adapter<ImagesListAdapter.ViewHolder>() {

    class ViewHolder(val viewHolder: View) : RecyclerView.ViewHolder(viewHolder) {
        fun bind(url: String) {
            val humbleImageView = viewHolder.findViewById<HumbleImageView>(R.id.humbleImageView)
            // TODO: Add documentation to explain why we should set the place holder again
            humbleImageView.setImageResource(R.drawable.ic_photo_black_48px)
            humbleImageView.setUrl(url)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ImagesListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.images_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imagesUrls[position])
    }

    override fun getItemCount() = imagesUrls.size
}