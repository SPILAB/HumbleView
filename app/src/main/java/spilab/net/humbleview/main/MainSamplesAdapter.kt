package spilab.net.humbleview.main

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import spilab.net.humbleview.R
import spilab.net.humbleimageview.HumbleImageView

class MainSamplesAdapter(private val samplesButtons: Array<SampleButton>,
                         private val mainSamplesAdapterListener: MainSamplesAdapterListener) :
        RecyclerView.Adapter<MainSamplesAdapter.ViewHolder>() {

    interface MainSamplesAdapterListener {
        fun onClick(intent: Intent)
    }

    data class SampleButton(val text: Int, val url: String, val intent: Intent)

    class ViewHolder(private val viewHolder: View) : RecyclerView.ViewHolder(viewHolder),
            View.OnClickListener {

        private lateinit var intent: Intent

        private lateinit var listener: MainSamplesAdapterListener

        fun bind(sampleButton: SampleButton, listener: MainSamplesAdapterListener) {
            intent = sampleButton.intent
            this.listener = listener
            viewHolder.findViewById<View>(R.id.button)
                    .setOnClickListener(this)
            viewHolder.findViewById<TextView>(R.id.textView)
                    .setText(sampleButton.text)
            with(viewHolder.findViewById<HumbleImageView>(R.id.humbleImageView)) {
                setImageDrawable(ColorDrawable(resources.getColor(R.color.md_brown_200)))
                setUrl(sampleButton.url)
            }
        }

        override fun onClick(v: View?) {
            listener.onClick(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MainSamplesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.main_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(samplesButtons[position], mainSamplesAdapterListener)
    }

    override fun getItemCount() = samplesButtons.size
}