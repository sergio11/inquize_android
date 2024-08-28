package com.dreamsoftware.inquize.framework.sound

import android.content.Context
import android.media.MediaPlayer
import com.dreamsoftware.inquize.domain.service.ISoundPlayerService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


/**
 * A concrete impl of [ISoundPlayerService] that uses the Android [MediaPlayer] to play UI sounds.
 *
 * @property context The context in which the sound should be played.
 */
class ISoundPlayerServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ISoundPlayerService {

    private var mediaPlayer: MediaPlayer? = null

    override fun playSound(uiSound: ISoundPlayerService.UISound) {
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