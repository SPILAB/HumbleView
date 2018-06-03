package spilab.net.humbleimageview.model.cache

import android.os.Handler
import io.mockk.*
import org.junit.Before
import org.junit.Test
import spilab.net.humbleimageview.android.AndroidHttpURLConnection
import spilab.net.humbleimageview.model.*
import spilab.net.humbleimageview.model.drawable.DrawableDecoder
import spilab.net.humbleimageview.model.drawable.HumbleBitmapDrawable
import java.io.ByteArrayInputStream
import java.net.HttpURLConnection
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future


class HumbleViewDownloaderTest {

    private lateinit var mockHumbleViewModel: HumbleViewModel
    private lateinit var humbleViewDownloader: HumbleViewDownloader

    private lateinit var mockFutureTask: Future<*>
    private lateinit var mockExecutorService: ExecutorService
    private lateinit var captureTaskRunnable: CapturingSlot<Runnable>

    private lateinit var mockHttpURLConnection: HttpURLConnection
    private lateinit var mockAndroidHttpURLConnection: AndroidHttpURLConnection
    private val responseCode = 200
    private val imageInputStream = ByteArrayInputStream(ByteArray(8))

    private lateinit var mockHandler: Handler
    private lateinit var captureHandlerRunnable: CapturingSlot<Runnable>

    private lateinit var mockHumbleBitmapDrawable: HumbleBitmapDrawable
    private lateinit var mockBitmapDrawableDecoder: DrawableDecoder

    @Before
    fun setUp() {

        mockBitmapDrawableDecoder = mockk()
        mockHumbleBitmapDrawable = mockk()

        mockHumbleViewModel = mockk()
        every { mockHumbleViewModel.onBitmapReady(mockHumbleBitmapDrawable) } returns Unit

        mockExecutorService = mockk()
        mockFutureTask = mockk()
        captureTaskRunnable = slot()
        every {
            mockExecutorService.submit(
                    capture(captureTaskRunnable)
            )
        } returns mockFutureTask

        mockHttpURLConnection = mockk()
        every { mockHttpURLConnection.responseCode } returns responseCode
        every { mockHttpURLConnection.inputStream } returns imageInputStream
        every { mockHttpURLConnection.disconnect() } returns Unit

        mockAndroidHttpURLConnection = mockk()
        every { mockAndroidHttpURLConnection.openConnection(any()) } returns mockHttpURLConnection

        mockHandler = mockk()
        captureHandlerRunnable = slot()
        every {
            mockHandler.post(
                    capture(captureHandlerRunnable)
            )
        } returns true


        every { mockBitmapDrawableDecoder.decodeBitmapDrawableForViewSize(any(), any()) } returns mockHumbleBitmapDrawable
        humbleViewDownloader = HumbleViewDownloader(
                mockAndroidHttpURLConnection,
                mockBitmapDrawableDecoder,
                mockHandler,
                mockHumbleViewModel)
    }

    @Test
    fun `Given a bitmap id, When start download, Then should download the bitmap`() {
        objectMockk(HumbleViewExecutor).use {
            every { HumbleViewExecutor.executorService } returns mockExecutorService
            humbleViewDownloader.start(humbleBitmapId = HumbleBitmapId())
            captureTaskRunnable.captured.run()
            captureHandlerRunnable.captured.run()
            verify { mockHumbleViewModel.onBitmapReady(mockHumbleBitmapDrawable) }
        }
    }

    @Test
    fun `Given a bitmap id, When duplicate start download, Then should download the bitmap only once`() {
        objectMockk(HumbleViewExecutor).use {
            every { HumbleViewExecutor.executorService } returns mockExecutorService
            humbleViewDownloader.start(humbleBitmapId = HumbleBitmapId())
            verify(exactly = 1) { mockExecutorService.submit(any()) }
            humbleViewDownloader.start(humbleBitmapId = HumbleBitmapId())
            verify(exactly = 1) { mockExecutorService.submit(any()) }
            captureTaskRunnable.captured.run()
            captureHandlerRunnable.captured.run()
            verify { mockHumbleViewModel.onBitmapReady(mockHumbleBitmapDrawable) }
        }
    }
}