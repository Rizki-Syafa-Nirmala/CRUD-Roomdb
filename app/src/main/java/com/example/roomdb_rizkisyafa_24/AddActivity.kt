package com.example.roomdb_rizkisyafa_24

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.roomdb_rizkisyafa_24.room.Movie
import com.example.roomdb_rizkisyafa_24.room.MovieDb
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {

    val db by lazy { MovieDb(this) }
    private var movieId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setUpView()
        setupListener()
    }

    fun setUpView() {
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType) {
            Constants.TYPE_CREATE -> {
                btn_update.visibility = View.GONE
            }
            Constants.TYPE_READ -> {
                btn_save.visibility = View.GONE
                btn_update.visibility = View.GONE

            }
            Constants.TYPE_UPDATE -> {
                btn_save.visibility = View.GONE

            }
        }
    }

    fun setupListener() {
        btn_save.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.movieDao().addMovie(
                    Movie(
                        0, et_tittle.text.toString(),
                        et_description.text.toString()
                    )
                )
                finish()
            }
        }

        btn_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.movieDao().updateMovie(
                    Movie(
                        movieId, et_tittle.text.toString(),
                        et_description.text.toString()
                    )
                )
                finish()
            }

            fun getMovie() {
                movieId = intent.getIntExtra("intent_id", 0)
                CoroutineScope(Dispatchers.IO).launch {
                    val movies = db.movieDao().getMovie(movieId)[0]
                    et_tittle.setText(movies.title)
                    et_description.setText(movies.desc)
                }
            }
        }
    }
}