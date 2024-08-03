package np.sanjeev.core

import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.Repository

/**
 * Only Needs "write" operations since Read operations are from Lucene
 */
@NoRepositoryBean
interface BaseRepository<T : BaseEntity, ID> : Repository<T, ID> {
    fun <S : T?> save(entity: S): S
    fun <S : T?> saveAll(entities: Iterable<S>?): Iterable<S>?
    fun deleteById(id: ID)
    fun delete(entity: T)
    fun deleteAllById(ids: Iterable<ID?>?)
    fun deleteAll(entities: Iterable<T?>?)
    fun deleteAll()
}
