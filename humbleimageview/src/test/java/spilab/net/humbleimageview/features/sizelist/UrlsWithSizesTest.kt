package spilab.net.humbleimageview.features.sizelist

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import spilab.net.humbleimageview.view.ViewSize

@RunWith(Parameterized::class)
class UrlsWithSizesTest(private val viewWidth: Int, private val viewHeight: Int,
                        urlWidthA: Int, urlHeightA: Int,
                        urlWidthB: Int, urlHeightB: Int,
                        urlWidthC: Int, urlHeightC: Int,
                        private val expectedUrl: String) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                    arrayOf(64, 64, 32, 32, 64, 64, 96, 96, "url B"),
                    arrayOf(64, 64, 52, 52, 48, 48, 32, 32, "url A"),
                    arrayOf(64, 64, 32, 32, 48, 48, 52, 52, "url C"),
                    arrayOf(64, 64, 64, 64, 32, 32, 96, 96, "url A"),
                    arrayOf(120, 80, 64, 64, 200, 200, 96, 96, "url B"),
                    arrayOf(120, 80, 140, 100, 200, 200, 96, 96, "url A")

            )
        }
    }

    private var urlsWithSizes: UrlsWithSizes = UrlsWithSizes(listOf(
            UrlWithSize("url A", ViewSize(urlWidthA, urlHeightA)),
            UrlWithSize("url B", ViewSize(urlWidthB, urlHeightB)),
            UrlWithSize("url C", ViewSize(urlWidthC, urlHeightC)
            )))

    @Test
    fun `Given different url size and view size, When get best url, Then should return the matching url`() {
        Assert.assertEquals(expectedUrl,
                urlsWithSizes.getBestUrlWithSize(ViewSize(viewWidth, viewHeight)).url)
    }
}