package com.example.instantsearch.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString
import com.example.instantsearch.R
import com.example.instantsearch.data.Book
import kotlinx.android.synthetic.main.book_item_layout.view.*

class BookViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(book: Book) {
        view.bookNameValueTextView.text = book.bookName
        view.authorNameValueTextView.text = book.highlightedAuthor?.toSpannedString() ?: book.author
        view.priceValueTextView.text = view.context.getString(R.string.rs) + book.price.toString()
    }
}