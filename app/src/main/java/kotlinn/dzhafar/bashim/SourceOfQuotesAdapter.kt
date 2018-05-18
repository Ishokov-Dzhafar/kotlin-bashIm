package kotlinn.dzhafar.bashim

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinn.dzhafar.bashim.data.SourceOfQuotes
import kotlinx.android.synthetic.main.source_item.view.*
import kotlin.dzhafar.bashim.R

/**
 * Created by dzhafar on 17.05.18.
 */
class SourceOfQuotesAdapter(list: MutableList<SourceOfQuotes>) : RecyclerView.Adapter<SourceOfQuotesAdapter.ViewHolder>() {

    private val mListeners: MutableList<ChangeSourceListener> = mutableListOf()
    private val mItems: MutableList<SourceOfQuotes> = list


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.title.text = item.desc
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.source_item, parent, false)
        return ViewHolder(view).listen{
            position, type -> changeSource(position)
        }
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val title = view.text!!
    }

    fun addListener(listener: ChangeSourceListener) {
        mListeners.add(listener)
    }

    fun changeSource(position: Int) {
        mListeners.forEach{
            it.sourceChaged(position)
        }
    }

    fun <T: RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit):T {
        itemView.setOnClickListener {
            event.invoke(adapterPosition, getItemViewType())
        }
        return this
    }

    operator fun get (position: Int):SourceOfQuotes {
        return mItems[position]
    }
}