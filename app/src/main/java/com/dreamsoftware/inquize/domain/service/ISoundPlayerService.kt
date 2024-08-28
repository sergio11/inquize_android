package com.dreamsoftware.inquize.domain.service

import androidx.annotation.RawRes
import com.dreamsoftware.inquize.R

/**
 * Represents a player for UI sounds.
 */
interface ISoundPlayerService {

    /**
     * Plays the provided UI sound.
     *
     * @param uiSound The UI sound to be played.
     */
    fun playSound(uiSound: UISound)

    /**
     * Enum class representing different UI sounds.
     * @property associatedRawResIntId - The associated raw resId..
     */
    enum class UISound(@RawRes val associatedRawResIntId: Int) {
        /**
         * Represents the sound played when the assistant is muted.
         */
        ASSISTANT_MUTED(R.raw.ui_sound_assistant_muted),

        /**
         * Represents the sound played when the assistant is unmuted.
         */
        ASSISTANT_UNMUTED(R.raw.ui_sound_assistant_unmuted)
    }
}