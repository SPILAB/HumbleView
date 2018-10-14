package spilab.net.humbleimageview.features.slideshow

import spilab.net.humbleimageview.features.request.HumbleViewRequest
import spilab.net.humbleimageview.features.sizelist.UrlsWithSizes
import spilab.net.humbleimageview.features.transition.FeatureTransition
import spilab.net.humbleimageview.log.HumbleLogs
import java.util.Observer

internal class SlideshowUrls(private val humbleViewRequest: HumbleViewRequest,
                             featureTransition: FeatureTransition,
                             private val images: List<UrlsWithSizes>) {

    companion object {
        fun fromUrls(humbleViewRequest: HumbleViewRequest,
                     featureTransition: FeatureTransition,
                     urls: Array<out CharSequence>): SlideshowUrls? {
            val sizesList: List<UrlsWithSizes> = urls.map { UrlsWithSizes.fromUrl(it.toString()) }.toList()
            return SlideshowUrls(humbleViewRequest, featureTransition, sizesList)
        }
    }

    private var index: Int

    init {
        require(images.isNotEmpty()) {
            "The 'images: List<UrlsWithSizes>' parameters should contain at last one image."
        }
        index = 0
        humbleViewRequest.cancel()
        featureTransition.addObserver { observable, any ->
            if (any == FeatureTransition.State.ON_TRANSITION_COMPLETED) {
                nextIndex()
                humbleViewRequest.urls = images[index]
            }
        }
        if (featureTransition.isCompleted()) {
            humbleViewRequest.urls = images[index]
        }
    }

    private fun nextIndex() {
        index++
        if (index == images.size) {
            index = 0
        }
    }
}