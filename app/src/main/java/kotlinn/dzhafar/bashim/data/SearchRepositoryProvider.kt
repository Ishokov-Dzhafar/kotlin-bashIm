package kotlinn.dzhafar.bashim.data

/**
 * Created by dzhafar on 08.05.18.
 */
object SearchRepositoryProvider {
    fun provideSearchRepository():SearchRepository {
        return SearchRepository(BashImApiService.create())
    }
}