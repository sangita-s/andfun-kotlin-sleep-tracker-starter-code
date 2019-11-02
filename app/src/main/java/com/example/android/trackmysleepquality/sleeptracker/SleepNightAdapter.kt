package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

//class SleepNightAdapter : RecyclerView.Adapter<SleepNightAdapter.ViewHolder>() {
//class SleepNightAdapter(val clickListener:SleepNightListener) : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()) {
class SleepNightAdapter(val clickListener: SleepNightListener)
    : ListAdapter<DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    //Not needed after changing to List Adapter
//    var data = listOf<SleepNight>()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }

    //Not needed after changing to List Adapter
//    override fun getItemCount() = data.size

//    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
//        val item = data[position]
//        if(item.sleepQuality <= 1)
//            holder.textView.setTextColor(Color.RED)
//        else
//            holder.textView.setTextColor(Color.BLACK)
//        holder.textView.text = item.sleepQuality.toString()
//    }

    fun AddHeaderAndSubmitList(list: List<SleepNight>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.SleepNightItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    //    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //Not needed after changing to List Adapter
//        val item = data[position]
        //Not needed after data binding?
//        val item = getItem(position)
//        holder.bindd(item)

        when (holder) {
            is ViewHolder -> {
                val nightItem = getItem(position) as DataItem.SleepNightItem
                holder.bindd(nightItem.sleepNight, clickListener)
            }
        }
    }

    //    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return ViewHolder.fromm(parent)
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.fromm(parent)
            else -> throw ClassCastException("Unknown View Type ${viewType}")
        }
    }

    //    class ViewHolder private constructor(itemView: ListItemSleepNightBinding) : RecyclerView.ViewHolder(itemView) {
    class ViewHolder private constructor(val binding: ListItemSleepNightBinding)
        : RecyclerView.ViewHolder(binding.root) {

        //No need after data class since we change below to binding.xxxxx
//        val sleepLength: TextView = binding.sleepLength
//        val quality: TextView = binding.qualityString
//        val qualityImage: ImageView = binding.qualityImage

        fun bindd(item: SleepNight, clickListener: SleepNightListener) {
            binding.sleep = item
            binding.clickListenerr = clickListener
            binding.executePendingBindings()
            //THen add bindings to xml list_item_sleep_night
            //After creating data binding adapter
//            val res = itemView.context.resources
//            binding.sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
//            binding.qualityString.text = convertNumericQualityToString(item.sleepQuality, res)
//
//            binding.qualityImage.setImageResource(when (item.sleepQuality) {
//                0 -> R.drawable.ic_sleep_0
//                1 -> R.drawable.ic_sleep_1
//                2 -> R.drawable.ic_sleep_2
//                3 -> R.drawable.ic_sleep_3
//                4 -> R.drawable.ic_sleep_4
//                5 -> R.drawable.ic_sleep_5
//                else -> R.drawable.ic_sleep_active
//            })
        }

        companion object {
            fun fromm(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                //No need after data binding
//                val view = layoutInflater
//                        .inflate(R.layout.list_item_sleep_night, parent, false)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
//                return ViewHolder(view)
                return ViewHolder(binding)
            }
        }
    }
}

//class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {
class SleepNightDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
//        return oldItem == newItem
        return oldItem.equals(newItem)
    }
}

class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClickk(night: SleepNight) = clickListener(night.nightId)
    //Continue coding in list item sleep night xml
}

//Adding headers
sealed class DataItem {
    data class SleepNightItem(val sleepNight: SleepNight) : DataItem() {
        override val id = sleepNight.nightId
    }

    //since no data -> object - so only one instance
    object Header : DataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}

class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun from(parent: ViewGroup): TextViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.header, parent, false)
            return TextViewHolder(view)
        }
    }
}