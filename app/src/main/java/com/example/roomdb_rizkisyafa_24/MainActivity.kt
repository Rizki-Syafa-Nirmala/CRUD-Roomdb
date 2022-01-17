package com.example.roomdb_rizkisyafa_24

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdb_rizkisyafa_24.room.Movie
import com.example.roomdb_rizkisyafa_24.room.MovieDb
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    val db by lazy { MovieDb(this) }
    lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupListener()
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        loadNote()
    }

    fun loadNote() {
        CoroutineScope(Dispatchers.IO).launch {
            val movies = db.movieDao().getMovies()
            Log.d("MainActivity", "dbresponse: $movies")
            withContext(Dispatchers.Main){
                movieAdapter.setData(movies)
            }
        }
    }

    fun setupListener() {
        add_movie.setOnClickListener() {
            intentEdit(0, Constants.TYPE_CREATE)
        }
    }

    fun intentEdit(movieId:Int, intentType: Int){
        startActivity(Intent(applicationContext, AddActivity::class.java)
            .putExtra("intent_id", movieId)
            .putExtra("intent_type", intentType)
        )
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter(arrayListOf(), object : MovieAdapter.OnAdapterListener{
            override fun onClick(movie: Movie) {
                intentEdit(movie.id, Constants.TYPE_READ)
            }
            override fun onUpdate(movie: Movie) {
                intentEdit(movie.id, Constants.TYPE_UPDATE)
            }

            override fun onDelete(movie: Movie) {
                CoroutineScope(Dispatchers.IO).launch {
                    db.movieDao().deleteMovie(movie)
                    loadNote()
                }
            }
        })
        rv_movie.apply{
        layoutManager = LinearLayoutManager(applicationContext)
        adapter = movieAdapter
        }
    }

}