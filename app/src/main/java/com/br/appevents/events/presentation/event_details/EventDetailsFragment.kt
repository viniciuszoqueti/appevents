package com.br.appevents.events.presentation.event_details

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.br.appevents.R
import com.br.appevents.databinding.EventDetailsFragmentBinding
import com.br.appevents.events.data.network.models.toDomain
import com.br.appevents.events.domain.models.Event
import com.br.appevents.events.domain.resource.Resource
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EventDetailsFragment @Inject constructor() : Fragment() {

    private var _binding: EventDetailsFragmentBinding? = null
    private val binding: EventDetailsFragmentBinding get() = _binding!!

    private var eventId: Int = 0

    private val eventDetailsViewModel: EventDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.event_details_fragment,
            container,
            false
        )

        arguments?.let {
            eventId = EventDetailsFragmentArgs.fromBundle(it).eventId
        }

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListners()
        eventDetailsViewModel.getEventById(eventId)
    }


    private fun setupObservers() {
        eventDetailsViewModel.eventLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.ResourceStatus.SUCCESS -> {
                    it.data?.toDomain()?.let { event ->
                        Glide
                            .with(this)
                            .load(event.image)
                            .apply(RequestOptions.circleCropTransform())
                            .placeholder(R.drawable.ic_photo_default)
                            .into(binding.ivEventLogo)

                        binding.tvTitle.text = event.title
                        binding.tvPrice.text = context?.getString(R.string.price, event.price)
                        binding.tvDescription.text = event.description
                        openMapLocate(event)

                    }
                    binding.pbLoad.visibility = View.GONE
                }
                Resource.ResourceStatus.LOADING -> showLoad()
                Resource.ResourceStatus.ERROR -> {
                    showError()
                    findNavController().popBackStack()
                }
                Resource.ResourceStatus.NOT_FOUND -> findNavController().popBackStack()
            }
        }

        eventDetailsViewModel.checkinLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.ResourceStatus.SUCCESS -> checkinSucess()
                Resource.ResourceStatus.LOADING -> showLoad()
                else -> showError()
            }
        }
    }

    private fun setupListners() {

        binding.btnShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    requireContext().getString(R.string.url_deeplink, eventId)
                )
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        binding.btnCheckin.setOnClickListener {

            val name = binding.edName.text.toString()
            val email = binding.edEmail.text.toString()
            eventDetailsViewModel.let {
                if (it.validationFormCheckin(name, email)) {
                    it.checkin(eventId, name, email)
                } else {
                    Toast.makeText(
                        context,
                        requireContext().getString(R.string.message_checkin),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun openMapLocate(event: Event) {
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
        findNavController().popBackStack()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}