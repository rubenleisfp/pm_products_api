package com.fp.ui.screens.artist

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.time.LocalDate

/**
 * [Artist] is the data class to represent the Affirmation text and imageResourceId
 */
data class Artist(
    @StringRes val stringResourceId: Int,
    @DrawableRes val imageResourceId: Int,
    val name : String,
    val birthday : LocalDate,
    val salary: Double
)