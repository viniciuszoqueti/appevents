package com.br.appevents.events.presentation.events_list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.br.appevents.R
import com.br.appevents.databinding.ItemListEventBinding
import com.br.appevents.events.presentation.models.EventModelPresentation

class EventsListAdapter(
    private val eventsList: List<EventModelPresentation>,
    private val onClickItem: (eventItem: EventModelPresentation) -> Unit
) : RecyclerView.Adapter<EventsListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemListEventBinding = ItemListEventBinding.inflate(layoutInflater, parent, false)

        return ItemViewHolder(itemListEventBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(eventsList[position])
    }

    override fun getItemCount(): Int = eventsList.count()

    inner class ItemViewHolder(
        private val binding: ItemListEventBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(itemEvent: EventModelPresentation) {

            Glide
                .with(itemView)
                .load(itemEvent.image)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.ic_photo_default)
                .into(binding.ivEventLogo)

            binding.tvTitle.text = itemEvent.title
            binding.tvPrice.text = itemView.context.getString(R.string.price, itemEvent.price)

            binding.mcvItemEvent.setOnClickListener {
                onClickItem(itemEvent)
            }
        }
    }

}