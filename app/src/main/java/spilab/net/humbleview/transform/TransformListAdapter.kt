package spilab.net.humbleview.transform

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import spilab.net.humbleview.R
import spilab.net.humbleimageview.HumbleImageView

class TransformListAdapter(private val imagesUrls: Array<String>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<TransformListAdapter.ViewHolder>() {

    enum class ViewType(val typeIndex: Int,
                        val resourceId: Int) {
        ROUNDED(0, R.layout.transform_list_rounded_item),
        ROUNDED_CORNER(1, R.layout.transform_list_rounded_corner_item);

        companion object {
            private val map = ViewType.values().associateBy(ViewType::typeIndex)
            fun fromInt(type: Int) = map[type]
        }
    }

    class ViewHolder(private val viewHolder: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(viewHolder) {
        fun bind(url: String) {
            val humbleImageView = viewHolder.findViewById<HumbleImageView>(R.id.humbleImageView)
            // TODO: Add documentation to explain why we should set the place holder again
            humbleImageView.setImageResource(R.drawable.ic_photo_black)
            humbleImageView.setUrl(url)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): TransformListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(ViewType.fromInt(viewType)!!.resourceId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imagesUrls[position])
    }

    override fun getItemViewType(position: Int): Int {
        return (position % 2)
    }

    override fun getItemCount() = imagesUrls.size
}