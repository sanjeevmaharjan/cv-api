package np.sanjeev.lucene.extension

import np.sanjeev.core.BaseEntity
import np.sanjeev.core.extension.unwrap
import org.hibernate.search.backend.lucene.search.query.LuceneSearchResult
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory
import org.hibernate.search.engine.search.query.SearchResult
import org.hibernate.search.engine.search.query.dsl.SearchQueryOptionsStep
import org.hibernate.search.engine.search.sort.dsl.CompositeSortComponentsStep
import org.hibernate.search.engine.search.sort.dsl.FieldSortOptionsStep
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.data.web.PagedModel

private fun SearchSortFactory.setSort(order: Sort.Order)
        : FieldSortOptionsStep<*, out SearchPredicateFactory>? {

    val fieldSearch = field(order.property)
    if (order.isAscending) {
        fieldSearch.asc()
    } else {
        fieldSearch.desc()
    }

    return fieldSearch
}

private fun SearchSortFactory.withSort(
    sort: Sort
): CompositeSortComponentsStep<*>? {
    var composite = this.composite()

    for (order in sort.get()) {
        composite = composite.add(
            this.setSort(order)
        )
    }

    return composite
}

@Suppress("UNCHECKED_CAST")
fun <T> SearchQueryOptionsStep<*, *, *, *, *>.getPage(pageable: Pageable): PagedModel<T> {
    var query = if (pageable.sort.isSorted) {
        sort { f -> f.withSort(pageable.sort) }
    } else {
        sort { f -> f.score() }
    }

    val result = query
        .highlighter { f -> f.fastVector().tag("<em style=\"color:#6bb77b;\">", "</em>") }
        .fetch(pageable.pageNumber * pageable.pageSize, pageable.pageSize)

    if (result is LuceneSearchResult) {
        // can also get topDocs
        val res = result as LuceneSearchResult<T>

        return PagedModel(PageableExecutionUtils.getPage(res.hits(), pageable, res.total()::hitCount))
    } else if (result is SearchResult) {
        val res = result as SearchResult<T>
        return PagedModel(PageableExecutionUtils.getPage(res.hits(), pageable, res.total()::hitCount))
    } else {
        throw RuntimeException("Invalid Result")
    }
}

fun <T : BaseEntity> SearchQueryOptionsStep<*, *, *, *, *>.get(): T? {
    val result = fetchSingleHit().unwrap()
    @Suppress("UNCHECKED_CAST")
    return result as T?
}
