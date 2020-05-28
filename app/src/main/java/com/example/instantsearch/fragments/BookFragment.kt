package com.example.instantsearch.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.item.StatsTextView
import com.algolia.instantsearch.helper.android.list.autoScrollToStart
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.algolia.instantsearch.helper.stats.StatsPresenterImpl
import com.algolia.instantsearch.helper.stats.connectView
import com.example.instantsearch.R
import com.example.instantsearch.activity.BookSearchActivity
import com.example.instantsearch.adapters.BookPagedListAdapter
import com.example.instantsearch.viewmodel.BookViewModel
import kotlinx.android.synthetic.main.book_fragment_layout.*

class BookFragment : Fragment() {

    private val mConnectionHandler = ConnectionHandler()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.book_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(requireActivity())[BookViewModel::class.java]

        setUpBookList(viewModel)
        createConnection(viewModel)

        filters.setOnClickListener {
            (requireActivity() as BookSearchActivity).showAuthorFacetFragment()
        }
    }

    private fun createConnection(viewModel: BookViewModel) {
        val searchBoxView = SearchBoxViewAppCompat(searchView)
        val statsView = StatsTextView(stats)

        mConnectionHandler += viewModel.mSearchBox.connectView(searchBoxView)
        mConnectionHandler += viewModel.mStatus.connectView(statsView, StatsPresenterImpl())
    }

    private fun setUpBookList(viewModel: BookViewModel) {
        val bookAdapter = BookPagedListAdapter()

        viewModel.mBookList.observe(viewLifecycleOwner, Observer { hits ->
            bookAdapter.submitList(hits)
        })

        bookList.let {
            it.itemAnimator = null
            it.adapter = bookAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
            it.autoScrollToStart(bookAdapter)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mConnectionHandler.clear()
    }
}