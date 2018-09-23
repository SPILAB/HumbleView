package spilab.net.humbleimageview.features.sizelist

import spilab.net.humbleimageview.view.ViewSize

data class SizeList(val urlsWithSizes: List<UrlWithSize>) {

    companion object {
        fun fromUrl(url: String?): SizeList? {
            if (url != null) {
                return SizeList(listOf(UrlWithSize(url)))
            }
            return null
        }
    }

    init {
        require(!urlsWithSizes.isEmpty())
    }

    fun getBestUrlWithSize(fromViewSize: ViewSize): UrlWithSize {
        var currentUrlWithSize: UrlWithSize = urlsWithSizes.first()
        for (urlWithSize in urlsWithSizes) {
            if (matchViewWithOrHeight(fromViewSize.width, urlWithSize.viewSize.width, currentUrlWithSize.viewSize.width)
                    && (matchViewWithOrHeight(fromViewSize.height, urlWithSize.viewSize.height, currentUrlWithSize.viewSize.height))
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
        return ((fromViewSize.width > currentUrlWithSize.viewSize.width)
                || (fromViewSize.height > currentUrlWithSize.viewSize.height)
                || ((currentUrlWithSize.viewSize.width * currentUrlWithSize.viewSize.height) > (urlWithSize.viewSize.width * urlWithSize.viewSize.height)))
    }
}

