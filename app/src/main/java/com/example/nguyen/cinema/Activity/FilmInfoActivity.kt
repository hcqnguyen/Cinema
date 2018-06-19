package com.example.nguyen.cinema.Activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.nguyen.cinema.Data.Model.Film
import com.example.nguyen.cinema.Data.Model.ResponeApi
import com.example.nguyen.cinema.R
import kotlinx.android.synthetic.main.activity_film_info.*

class FilmInfoActivity : AppCompatActivity() {

    var film : ResponeApi.Movie? = null
    val DOMAIN :String = "https://nam-cinema.herokuapp.com"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_info)

         if (intent.hasExtra("FILM")) {

              film = intent.getSerializableExtra("FILM") as ResponeApi.Movie
         }

        film?.let {
            text_title.text = it.title
            text_discription.text = it.description
            text_genre.text = it.genre
            text_release.text = it.release
            Glide.with(this)
                    .load(DOMAIN + it.cover)
                    .override(400, 400)
                    .error(R.drawable.ic_launcher_background)
                    .into(view_cover)
            text_creator_name.text = it.creator.username
        }




    }

}
