package com.dreamsoftware.inquize.data.remote.languagemodel

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.seconds
import com.dreamsoftware.inquize.data.remote.datasource.IMultiModalLanguageModelDataSource.MessageContent
import com.dreamsoftware.inquize.data.remote.datasource.impl.GeminiLanguageModelDataSourceImpl

// Note: Android#Logger should be mocked to run the test. Hence a local jvm
// test cannot be used to test the class.
class GeminiLanguageModelClientTest {

    private lateinit var geminiLanguageModelClient: GeminiLanguageModelDataSourceImpl

    @Before
    fun setUp() {
        geminiLanguageModelClient = GeminiLanguageModelDataSourceImpl()
    }

    @Test
    fun getResponseTest_validMessage_validResponseMustBeReceivedSuccessfully() {
        runTest(timeout = 15.seconds) {
            val messageContents = listOf(MessageContent.Text(text = "Hi there, how are you doing?"))
            val response = geminiLanguageModelClient.apply { startNewChatSession() }
                .sendMessage(messageContents).getOrThrow()
            assert(response.isNotBlank())
        }
    }

    @Test(expected = IllegalStateException::class)
    fun sendMessageTest_attemptToSendMessageWithoutStartingNewSession_mustThrowException() =
        runTest {
            geminiLanguageModelClient.sendMessage(
                listOf(MessageContent.Text(text = "Hi there, how are you doing?"))
            ).getOrThrow()
        }

    @Test
    fun startNewConversationTest_startingNewSessionAfterEndingPrevious_mustBeSuccessful() =
        runTest(timeout = 20.seconds) {
            val messageContents = listOf(MessageContent.Text(text = "Hi there, how are you doing?"))

            // start new chat session
            geminiLanguageModelClient.startNewChatSession()

            // send message
            val response = geminiLanguageModelClient.sendMessage(messageContents).getOrThrow()
            assert(response.isNotBlank())

            // end chat session
            geminiLanguageModelClient.endChatSession()

            // start a new conversation again
            geminiLanguageModelClient.startNewChatSession()

            // send message
            val response2 = geminiLanguageModelClient.sendMessage(messageContents).getOrThrow()
            assert(response2.isNotBlank())
        }
}