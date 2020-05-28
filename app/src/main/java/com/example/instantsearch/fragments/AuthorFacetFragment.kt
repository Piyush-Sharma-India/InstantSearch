package com.example.instantsearch.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.filter.facet.FacetListAdapter
import com.algolia.instantsearch.helper.android.list.autoScrollToStart
import com.algolia.instantsearch.helper.filter.facet.connectView
import com.example.instantsearch.R
import com.example.instantsearch.viewholder.AuthorFacetListViewHolder
import com.example.instantsearch.viewmodel.BookViewModel
import kotlinx.android.synthetic.main.author_facet_fragment.*

class AuthorFacetFragment : Fragment() {

    private val mConnectionHandler = ConnectionHandler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.author_facet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(requireActivity())[BookViewModel::class.java]
        val authorFacetAdapter = initAuthorFacetList()

        createConnection(viewModel, authorFacetAdapter)
    }

    private fun createConnection(
        viewModel: BookViewModel,
        authorFacetAdapter: FacetListAdapter
    ) {
        mConnectionHandler += viewModel.mAuthorFacetList.connectView(
            authorFacetAdapter,
            viewModel.mAuthorFacetPresenter
        )
    }

    private fun initAuthorFacetList(): FacetListAdapter {
        val authorFacetAdapter = FacetListAdapter(AuthorFacetListViewHolder.Factory)

        authorFacetList.let {
            it.adapter = authorFacetAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
            it.autoScrollToStart(authorFacetAdapter)
        }
        return authorFacetAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mConnectionHandler.clear()
    }
}
