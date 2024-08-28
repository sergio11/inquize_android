package com.dreamsoftware.inquize.domain.sound

import android.content.Context
import android.media.MediaPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


/**
 * A concrete impl of [UISoundPlayer] that uses the Android [MediaPlayer] to play UI sounds.
 *
 * @property context The context in which the sound should be played.
 */
class AndroidUISoundPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) : UISoundPlayer {
    private var mediaPlayer: MediaPlayer? = null
    override fun playSound(uiSound: UISoundPlayer.UISound) {
        if (mediaPlayer?.isPlaying == true) return
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, uiSound.associatedRawResIntId)
        }
        mediaPlayer?.let { player ->
            player.setOnCompletionListener {
                it.release()
                mediaPlayer = null
            }
            player.start()
        }
    }
}