package com.fp.data.repository

import com.fp.R
import com.fp.ui.store.Product
import com.fp.ui.screens.artist.Artist
import java.time.LocalDate

/**
 * [Datasource] generates a list of [Affirmation]
 */


//https://dummyjson.com/products?limit=30&skip=0&select=title,description,category,price,rating,stock,thumbnail
class Datasource() {

    fun loadProducts(): List<Product> {
        return listOf<Product>(
            Product(1, "Boli", "Boli Bic sensancional", "Papeleria", 3.12, 3.0, 20,""),
            Product(2, "Lapiz", "Lapiz increible", "Papeleria", 2.0, 4.2, 10,""))
    }

    fun loadArtist(): List<Artist> {
        return listOf<Artist>(
            Artist(
                R.string.aretha_franklin_summary,
                R.drawable.aretha_gettyimages_85514879,
                "Aretha Franklin",
                LocalDate.parse("1942-03-25"),
                3000.0
            ),
            Artist(
                R.string.whitney_houston_summary,
                R.drawable.whitney_houston_gettyimages_1292416791_e1670452097358,
                "Whitney Houston",
                LocalDate.parse("1963-08-09"),
                4000.0
            ),
            Artist(
                R.string.sam_cooke_summary,
                R.drawable.sam_cooke_gettyimages_73907124,
                "Sam Cooke",
                LocalDate.parse("1931-01-22"),
                4000.0
            ),
            Artist(
                R.string.billie_holiday_summary,
                R.drawable.billie_holiday_gettyimages_74275784,
                "Billie Holiday",
                LocalDate.parse("1915-04-07"),
                1000.0
            ),
            Artist(
                R.string.mariah_carey_summary,
                R.drawable.mariah_gettyimages_1253819231,
                "Mariah Carey",
                LocalDate.parse("1969-03-27"),
                2000.0
            ),
            Artist(
                R.string.ray_charles_summary,
                R.drawable.ray_charles_gettyimages_51362146,
                "Ray Charles Houston",
                LocalDate.parse("1930-09-23"),
                3000.0
            ),
            Artist(
                R.string.stevie_wonder,
                R.drawable.stevie_wonder_gettyimages_140857023,
                "Stevie Wonder",
                LocalDate.parse("1950-05-13"),
                2500.75
            ),
            Artist(
                R.string.beyonce_summary,
                R.drawable.beyonce_gettyimages_634993342,
                "Beyonce",
                LocalDate.parse("1981-09-04"),
                5000.00
            ),
            Artist(
                R.string.otis_reading_summary,
                R.drawable.otis_redding_gettyimages_74292558,
                "Otis Redding",
                LocalDate.parse("1941-09-09"),
                1050.00
            ),
            Artist(
                R.string.al_green_summary,
                R.drawable.al_green_gettyimages_84999719,
                "Al Green",
                LocalDate.parse("1946-04-13"),
                750.00
            )
        )
    }
}