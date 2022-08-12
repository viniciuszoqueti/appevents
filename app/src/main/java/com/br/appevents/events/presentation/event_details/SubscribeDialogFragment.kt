package com.br.appevents.events.presentation.event_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.br.appevents.R
import com.br.appevents.databinding.SubscribeDialogFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SubscribeDialogFragment : BottomSheetDialogFragment() {

    private var _binding: SubscribeDialogFragmentBinding? = null
    private val binding get() = _binding!!
    private val eventDetailsViewModel: EventDetailsViewModel by activityViewModels()
    private val args: EventDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SubscribeDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {

        binding.btnCheckin.setOnClickListener {
            val name = binding.edName.text.toString()
            val email = binding.edEmail.text.toString()
            eventDetailsViewModel.let {
                if (it.validationFormCheckin(name, email)) {
                    it.checkin(args.eventId, name, email)
                    this.dismiss()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}