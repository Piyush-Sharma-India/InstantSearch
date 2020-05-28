package com.example.instantsearch.data

import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.instantsearch.helper.highlighting.Highlightable
import com.algolia.search.model.Attribute
import kotlinx.serialization.json.JsonObject

data class Book(
    val bookName: String, val author: String,
    val price: Int, val inStock: Boolean,
    override val _highlightResult: JsonObject?
) : Highlightable {

    val highlightedAuthor: HighlightedString?
        get() = getHighlight(Attribute("author"))
}