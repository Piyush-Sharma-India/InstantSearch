package com.example.instantsearch.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.instantsearch.R
import com.example.instantsearch.fragments.AuthorFacetFragment
import com.example.instantsearch.fragments.BookFragment

class BookSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showBookFragment()
    }

    private fun showBookFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(
                R.id.container,
                BookFragment()
            )
            .commit()
    }

    fun showAuthorFacetFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(
                R.id.container,
                AuthorFacetFragment()
            )
            .addToBackStack("author_facet")
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return super.onSupportNavigateUp()
    }
}
