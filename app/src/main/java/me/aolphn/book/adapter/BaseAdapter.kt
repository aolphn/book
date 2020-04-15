package me.aolphn.book.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.aolphn.book.R
import me.aolphn.book.databinding.TitleContentItemBinding
/**
 * @author OF
 *
 */
class BaseAdapter(context: Context, private val data: List<IData>) :
    RecyclerView.Adapter<BaseViewHolder>() {
    private val layoutInflater:LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        BaseViewHolder(TitleContentItemBinding.inflate(layoutInflater))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.tile.text = data[position].title()
        holder.content.text = data[position].content()
    }

}

interface IData{
    fun title():String
    fun content():String
}

class BaseViewHolder(private val binding: TitleContentItemBinding) : RecyclerView.ViewHolder(binding.root) {

    val tile = binding.title
    val content = binding.content
    fun itemView():View = itemView
}