package kotlinn.dzhafar.bashim

import android.app.Application
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinn.dzhafar.bashim.data.Quote
import kotlinn.dzhafar.bashim.data.SearchRepository
import kotlinn.dzhafar.bashim.data.SearchRepositoryProvider
import kotlinn.dzhafar.bashim.data.SourceOfQuotes
import kotlin.dzhafar.bashim.R

const val INTENT_NAME_NAME = "INTENT_NAME_NAME"
const val INTENT_SITE_NAME = "INTENT_SITE_NAME"

class QuotesActivity : AppCompatActivity() {


    @BindView(R.id.list)
    lateinit var listView:RecyclerView
    val compositeDisposible:CompositeDisposable = CompositeDisposable()
    val repository:SearchRepository = SearchRepositoryProvider.provideSearchRepository()
    lateinit var adapter:SourceOfQuotesAdapter

    private val list: MutableList<Quote> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        listView.layoutManager = llm
        val site = intent.getStringExtra(INTENT_SITE_NAME)
        val name = intent.getStringExtra(INTENT_NAME_NAME)
        compositeDisposible.add(
                repository.searchQuotest(site, name)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                                list.addAll(result)
                            listView.adapter = QuotesAdapter(list)
                            Log.d(TAG, list.toString())
                        })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposible.dispose()
    }
}
