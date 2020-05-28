package com.example.instantsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.android.filter.state.connectPagedList
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxConnectorPagedList
import com.algolia.instantsearch.helper.filter.facet.FacetListConnector
import com.algolia.instantsearch.helper.filter.facet.FacetListPresenterImpl
import com.algolia.instantsearch.helper.filter.facet.FacetSortCriterion
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.helper.stats.StatsConnector
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.example.instantsearch.constants.Constants
import com.example.instantsearch.data.Book
import io.ktor.client.features.logging.LogLevel

class BookViewModel : ViewModel() {
    private val mClient = ClientSearch(
        ApplicationID(Constants.APPLICATION_ID),
        APIKey(Constants.API_KEY), LogLevel.ALL
    )

    private val mIndex = mClient.initIndex(IndexName(Constants.INDEX_NAME))
    private val mSearcher = SearcherSingleIndex(mIndex)
    private val mDataSourceFactory = SearcherSingleIndexDataSource.Factory(mSearcher) { hit ->
        Book(
            hit.json.getPrimitive(Constants.BOOK_NAME).content,
            hit.json.getPrimitive(Constants.AUTHOR).content,
            hit.json.getPrimitive(Constants.PRICE).int,
            hit.json.getPrimitive(Constants.IN_STOCK).boolean,
            hit.json.getObjectOrNull(Constants.HIGHLIGHT_RESULT)
        )
    }

    private val mPageListConfig = PagedList.Config.Builder().setPageSize(Constants.PAGE_SIZE).build()
    val mBookList: LiveData<PagedList<Book>> =
        LivePagedListBuilder(mDataSourceFactory, mPageListConfig).build()

    val mSearchBox = SearchBoxConnectorPagedList(mSearcher, listOf(mBookList))
    private val mConnection = ConnectionHandler()
    val mStatus = StatsConnector(mSearcher)

    private val mFilterState = FilterState()
    val mAuthorFacetList = FacetListConnector(
        searcher = mSearcher,
        filterState = mFilterState,
        attribute = Attribute(Constants.AUTHOR),
        selectionMode = SelectionMode.Single
    )

    val mAuthorFacetPresenter = FacetListPresenterImpl(
        sortBy = listOf(FacetSortCriterion.CountDescending, FacetSortCriterion.IsRefined),
        limit = Constants.FACET_LIST_PRESENTER_IMPL_LIMIT
    )

    init {
        mConnection += mSearchBox
        mConnection += mStatus
        mConnection += mAuthorFacetList
        mConnection += mSearcher.connectFilterState(mFilterState)
        mConnection += mFilterState.connectPagedList(mBookList)
    }

    override fun onCleared() {
        super.onCleared()
        mSearcher.cancel()
        mConnection.clear()
    }
}