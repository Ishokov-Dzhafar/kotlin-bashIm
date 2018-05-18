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

const val TAG: String = "MainActivity"

class MainActivity : AppCompatActivity(), ChangeSourceListener {

    override fun sourceChaged(position: Int) {
        Log.d(TAG, "from main = ${adapter[position]}")
        val intent = Intent(applicationContext, QuotesActivity:: class.java)
        intent.putExtra(INTENT_NAME_NAME, adapter[position].name)
        intent.putExtra(INTENT_SITE_NAME, adapter[position].site)
        startActivity(intent)
    }

    @BindView(R.id.list)
    lateinit var listView:RecyclerView
    val compositeDisposible:CompositeDisposable = CompositeDisposable()
    val repository:SearchRepository = SearchRepositoryProvider.provideSearchRepository()
    lateinit var adapter:SourceOfQuotesAdapter

    private val list: MutableList<SourceOfQuotes> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val q:Quote = Quote("site","name", "desc", "html", "link")
        ButterKnife.bind(this)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        listView.layoutManager = llm
        compositeDisposible.add(
                repository.searchSourcesQuotes()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            result.forEach {
                                list.addAll(it)
                            }
                            adapter = SourceOfQuotesAdapter(list)
                            listView.adapter = adapter
                            adapter.addListener(this)
                            Log.d(TAG, list.toString())
                        })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposible.dispose()
    }
}
