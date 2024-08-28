import android.content.Context
import android.graphics.Bitmap

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dreamsoftware.inquize.data.local.bitmapstore.AndroidBitmapStore
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class AndroidBitmapStoreTest {

    private lateinit var bitmapStore: AndroidBitmapStore
    private lateinit var context: Context
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        bitmapStore = AndroidBitmapStore(context, testDispatcher)
    }

    @Test
    fun saveRetrieveAndDeleteTest() = runTest(testDispatcher) {
        // given a test bitmap
        val testBitmap = Bitmap.createBitmap(101, 106, Bitmap.Config.ARGB_8888)

        // the bitmap must be saved successfully
        val uri = bitmapStore.saveBitmap(testBitmap)!!
        advanceUntilIdle()

        // if the bitmap was saved successfully, it must be also be retrievable
        val retrievedBitmap = bitmapStore.retrieveBitmapForUri(uri)!!
        advanceUntilIdle()
        assert(retrievedBitmap.width == testBitmap.width)
        assert(retrievedBitmap.height == testBitmap.height)

        // if the bitmap is deleted
        bitmapStore.deleteBitmapWithUri(uri)
        // trying to retrieve it must return null
        assertNull(bitmapStore.retrieveBitmapForUri(uri))
    }

    @Test
    fun multipleSavesAndClearTest() = runTest(testDispatcher) {
        // Given a list of saved bitmaps
        val bitmaps = listOf(
            Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888),
            Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888),
            Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888),
        )
        val uris = bitmaps.map { bitmapStore.saveBitmap(it) }
        advanceUntilIdle()

        uris.zip(bitmaps).forEach { (uri, associatedBitmap) ->
            // if the bitmap was saved successfully, it must be also be retrievable
            val retrievedBitmap = bitmapStore.retrieveBitmapForUri(uri!!)
            advanceUntilIdle()
            assert(retrievedBitmap!!.width == associatedBitmap.width)
            assert(retrievedBitmap.height == associatedBitmap.height)
        }

        // when the bitmap store is cleared
        assertTrue(bitmapStore.deleteAllSavedBitmaps())

        // trying to retrieve the bitmaps must return null
        assert(uris.all { bitmapStore.retrieveBitmapForUri(it!!) == null })
    }


}
