package com.br.appevents.events.presentation.events_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.br.appevents.R
import com.br.appevents.databinding.EventsListFragmentBinding
import com.br.appevents.events.domain.models.EventModelDomain
import com.br.appevents.events.domain.resource.Resource
import com.br.appevents.events.presentation.BaseFragment
import com.br.appevents.events.presentation.events_list.adapters.EventsListAdapter
import com.br.appevents.events.presentation.models.EventModelPresentation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventsFragment : BaseFragment<EventsListFragmentBinding>(
    R.layout.events_list_fragment,
    EventsListFragmentBinding::bind
) {
    private val eventsViewModel: EventsViewModel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        eventsViewModel.loadEvents()
    }

    private fun setupObservers() {
        eventsViewModel.eventsListLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> setupEventList(it.data)
                is Resource.Loading -> showLoad()
                else -> showError()
            }
        }
    }

    private fun setupEventList(list: List<EventModelPresentation>?) {

        if (list == null) {
            showError()
            return
        }

        binding.rvEvents.apply {
            adapter = EventsListAdapter(list) { eventItem ->
                navigate(
                    EventsFragmentDirections.actionEventsFragmentToEventDetailsFragment(
                        eventItem.id
                    )
                )
            }
        }
        binding.pbLoad.visibility = View.GONE
    }

    private fun showError() {
        binding.pbLoad.visibility = View.GONE
        Toast.makeText(
            context,
            requireContext().getString(R.string.message_error),
            Toast.LENGTH_LONG
        ).show()
        requireActivity().finish()
    }

    private fun showLoad() {
        binding.pbLoad.visibility = View.VISIBLE
    }
}

