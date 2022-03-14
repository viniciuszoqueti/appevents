package com.br.appevents.events.presentation.events_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.br.appevents.R
import com.br.appevents.databinding.EventsListFragmentBinding
import com.br.appevents.events.data.network.models.toDomain
import com.br.appevents.events.domain.models.Event
import com.br.appevents.events.domain.resource.Resource
import com.br.appevents.events.presentation.events_list.adapters.EventsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EventsFragment @Inject constructor() : Fragment() {

    private var _binding: EventsListFragmentBinding? = null
    private val binding: EventsListFragmentBinding get() = _binding!!

    private val eventsViewModel: EventsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.events_list_fragment,
            container,
            false
        )

        return binding.root
    }

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
            when (it.status) {
                Resource.ResourceStatus.SUCCESS -> setupEventList(it.data?.toDomain())
                Resource.ResourceStatus.LOADING -> showLoad()
                else -> showError()
            }
        }
    }

    private fun setupEventList(list: List<Event>?) {

        if (list == null) {
            showError()
            return
        }

        binding.rvEvents.apply {
            layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
            )
            adapter = EventsListAdapter(list) { eventItem ->
                Navigation.findNavController(binding.root).navigate(
                    EventsFragmentDirections.actionEventsFragmentToEventDetailsFragment(eventItem.id)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

