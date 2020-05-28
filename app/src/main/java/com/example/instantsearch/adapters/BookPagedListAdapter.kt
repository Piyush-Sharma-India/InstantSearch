package com.example.instantsearch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.instantsearch.R
import com.example.instantsearch.data.Book
import com.example.instantsearch.viewholder.BookViewHolder

class BookPagedListAdapter : PagedListAdapter<Book, BookViewHolder>(BookPagedListAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_item_layout, parent, false)

        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        book?.let {
            holder.bind(book)
        }
    }

    companion object : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem::class == newItem::class
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.bookName == newItem.bookName
                    && oldItem.author == newItem.author
        }
    }

}