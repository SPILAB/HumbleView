package spilab.net.humbleimageview.features.slideshow

import android.os.Handler
import android.os.Looper
import spilab.net.humbleimageview.android.AndroidHandler
import spilab.net.humbleimageview.features.request.HumbleViewRequest
import spilab.net.humbleimageview.features.sizelist.UrlsWithSizes
import spilab.net.humbleimageview.features.transition.FeatureTransition
import java.util.*

internal class SlideshowUrls(private val humbleViewRequest: HumbleViewRequest,
                             private val featureTransition: FeatureTransition,
                             attachedToWindow: Boolean,
                             private val images: List<UrlsWithSizes>) : Runnable {

    companion object {

        const val DEFAULT_DELAY_BETWEEN_LOADED_IMAGES_MILLIS = 4000L

        fun fromUrls(humbleViewRequest: HumbleViewRequest,
                     featureTransition: FeatureTransition,
                     attachedToWindow: Boolean,
                     urls: Array<out CharSequence>): SlideshowUrls? {
            val sizesList: List<UrlsWithSizes> = urls.map { UrlsWithSizes.fromUrl(it.toString()) }.toList()
            return SlideshowUrls(humbleViewRequest, featureTransition, attachedToWindow, sizesList)
        }
    }

    var delayBetweenLoadedImagesMillis = DEFAULT_DELAY_BETWEEN_LOADED_IMAGES_MILLIS

    private var index: Int = 0
    private var observer: Observer = Observer { _: Observable, any: Any ->
        if (any == FeatureTransition.State.ON_TRANSITION_COMPLETED) {
            handler.postDelayed(this, delayBetweenLoadedImagesMillis)
        }
    }

    private val handler = AndroidHandler(Handler(Looper.getMainLooper()))

    init {
        require(images.isNotEmpty()) {
            "The 'images: List<UrlsWithSizes>' parameters should contain at last one image."
        }
        humbleViewRequest.cancel()
        if (attachedToWindow) startNextImage()
    }

    fun onAttached() {
        startNextImage()
    }

    fun onDetached() {
        featureTransition.deleteObserver(observer)
    }

    private fun startNextImage() {
        featureTransition.addObserver(observer)
        if (featureTransition.isCompleted()) {
            humbleViewRequest.urls = images[index]
        }
    }

    private fun nextIndex() {
        index++
        if (index == images.size) index = 0
    }

    override fun run() {
        nextIndex()
        humbleViewRequest.urls = images[index]
    }
}