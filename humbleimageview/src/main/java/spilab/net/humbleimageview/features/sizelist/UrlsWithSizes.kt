package spilab.net.humbleimageview.features.sizelist

import spilab.net.humbleimageview.view.ViewSize

data class UrlsWithSizes(val urlsWithSizes: List<UrlWithSize>) {

    companion object {
        fun fromUrl(url: String): UrlsWithSizes {
            return UrlsWithSizes(listOf(UrlWithSize(url)))
        }
    }

    init {
        require(!urlsWithSizes.isEmpty())
    }

    fun getBestUrlWithSize(fromViewSize: ViewSize): UrlWithSize {
        var currentUrlWithSize: UrlWithSize = urlsWithSizes.first()
        for (urlWithSize in urlsWithSizes) {
            if (matchViewWithOrHeight(fromViewSize.width, urlWithSize.urlSize.width, currentUrlWithSize.urlSize.width)
                    && (matchViewWithOrHeight(fromViewSize.height, urlWithSize.urlSize.height, currentUrlWithSize.urlSize.height))
                    && matchViewSize(fromViewSize, urlWithSize, currentUrlWithSize)) {
                currentUrlWithSize = urlWithSize
            }
        }
        return currentUrlWithSize
    }

    private fun matchViewWithOrHeight(viewSize: Int, urlWithSize: Int, currentUrlWithSize: Int): Boolean {
        return (urlWithSize >= viewSize
                || (currentUrlWithSize < viewSize && urlWithSize > currentUrlWithSize))
    }

    private fun matchViewSize(fromViewSize: ViewSize, urlWithSize: UrlWithSize, currentUrlWithSize: UrlWithSize): Boolean {
        return ((fromViewSize.width > currentUrlWithSize.urlSize.width)
                || (fromViewSize.height > currentUrlWithSize.urlSize.height)
                || ((currentUrlWithSize.urlSize.width * currentUrlWithSize.urlSize.height) > (urlWithSize.urlSize.width * urlWithSize.urlSize.height)))
    }
}

