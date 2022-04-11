package com.br.appevents.events.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigation

open class BaseFragment<Binding>(
    layout: Int,
    private var initializer: ((View) -> Binding)?
) : Fragment(layout) {

    private var _binding: Binding? = null
    val binding: Binding
        get() = _binding!!

    private val controller: NavController
        get() = Navigation.findNavController(requireView())

    fun navigate(navDirections: NavDirections) = controller.navigate(navDirections)
    fun pop() = controller.popBackStack()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = initializer?.invoke(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        initializer = null
    }
}