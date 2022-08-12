package com.br.appevents.events.presentation.event_details

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.br.appevents.R
import com.br.appevents.databinding.EventDetailsFragmentBinding
import com.br.appevents.events.domain.resource.Resource
import com.br.appevents.events.presentation.BaseFragment
import com.br.appevents.events.presentation.models.EventModelPresentation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventDetailsFragment : BaseFragment<EventDetailsFragmentBinding>(
    R.layout.event_details_fragment,
    EventDetailsFragmentBinding::bind
) {
    private val eventDetailsViewModel: EventDetailsViewModel by activityViewModels()
    private val args: EventDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupListners()
        eventDetailsViewModel.getEventById(args.eventId)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_share_appbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_menu_share -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        requireContext().getString(R.string.url_deeplink, args.eventId)
                    )
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupObservers() {
        eventDetailsViewModel.eventLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val event = it.data
                    Glide
                        .with(this)
                        .load(event.image)
                        .apply(RequestOptions.circleCropTransform())
                        .placeholder(R.drawable.ic_photo_default)
                        .into(binding.ivEventLogo)

                    binding.tvTitle.text = event.title
                    binding.tvPrice.text = getString(R.string.price, event.price)
                    binding.tvDescription.text = event.description
                    openMapLocate(event)
                    binding.pbLoad.visibility = View.GONE
                }
                is Resource.Loading -> showLoad()
                is Resource.Error -> {
                    showError()
                    pop()
                }
                is Resource.NotFound -> pop()
            }
        }

        eventDetailsViewModel.checkinLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> checkinSucess()
                is Resource.Loading -> showLoad()
                else -> showError()
            }
        }
    }

    private fun setupListners() {

        binding.btnCheckin.setOnClickListener {
            navigate(
                EventDetailsFragmentDirections.actionEventDetailsFragmentToSubscribeDialogFragment(
                    args.eventId
                )
            )
        }
    }

    private fun openMapLocate(event: EventModelPresentation) {
        binding.btnMap.isEnabled = true
        binding.btnMap.setOnClickListener {
            val mapIntent = Intent(
                Intent.ACTION_VIEW,
                event.getMapFormattedUri()
            )
            try {
                startActivity(mapIntent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    requireActivity(),
                    R.string.error_map,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkinSucess() {
        binding.pbLoad.visibility = View.GONE
        Toast.makeText(
            context,
            requireContext().getString(R.string.checkin_sucess),
            Toast.LENGTH_LONG
        ).show()
        pop()
    }

    private fun showError() {
        binding.pbLoad.visibility = View.GONE
        Toast.makeText(
            context,
            requireContext().getString(R.string.message_error),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showLoad() {
        binding.pbLoad.visibility = View.VISIBLE
    }
}