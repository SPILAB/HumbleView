package spilab.net.humbleimageview.android

import android.graphics.Bitmap
import android.os.AsyncTask
import androidx.palette.graphics.Palette

internal class AndroidPalette {

    fun generate(bitmap: Bitmap, onGenerated: (palette: Palette) -> Unit): AsyncTask<Bitmap, Void, Palette> {
        return androidx.palette.graphics.Palette.Builder(bitmap).generate {
            onGenerated(it!!)
        }
    }
}