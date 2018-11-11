package spilab.net.humbleimageview.features.slideshow

import spilab.net.humbleimageview.features.request.HumbleViewRequest
import spilab.net.humbleimageview.features.sizelist.UrlsWithSizes
import spilab.net.humbleimageview.features.transition.FeatureTransition

internal class SlideshowFactory {

    companion object {
        const val DEFAULT_DELAY_BETWEEN_LOADED_IMAGES_MILLIS = 4000L
    }

    fun fromUrls(humbleViewRequest: HumbleViewRequest,
                 featureTransition: FeatureTransition,
                 attachedToWindow: Boolean,
                 urls: Array<out CharSequence>): SlideshowUrls? {
        val sizesList: List<UrlsWithSizes> = urls.map { UrlsWithSizes.fromUrl(it.toString()) }.toList()
        return SlideshowUrls(humbleViewRequest, featureTransition, attachedToWindow, sizesList)
    }
}