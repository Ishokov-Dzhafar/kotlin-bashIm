package kotlinn.dzhafar.bashim.data

/**
 * Created by dzhafar on 08.05.18.
 */
class SearchRepository(val apiService: BashImApiService) {

    fun searchQuotest(site:String, name:String):io.reactivex.Observable<List<Quote>> {
        return apiService.searchQuotest(site, name, 50)
    }

    fun searchSourcesQuotes():io.reactivex.Observable<List<List<SourceOfQuotes>>> {
        return apiService.searchSources()
    }
}