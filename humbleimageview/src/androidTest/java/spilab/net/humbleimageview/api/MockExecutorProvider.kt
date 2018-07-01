package spilab.net.humbleimageview.api

import java.util.concurrent.ExecutorService

class MockExecutorProvider(private val mockExecutorService: ExecutorService) : ExecutorProvider() {

    override fun getExecutorService(): ExecutorService {
        return mockExecutorService
    }
}
