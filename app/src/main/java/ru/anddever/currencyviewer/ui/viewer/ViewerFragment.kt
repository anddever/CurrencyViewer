package ru.anddever.currencyviewer.ui.viewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import ru.anddever.currencyviewer.databinding.FragmentViewerBinding
import javax.inject.Inject

class ViewerFragment @Inject constructor(
    private val factory: ViewModelProvider.Factory
): Fragment() {

    private var _binding: FragmentViewerBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController
        get() = findNavController()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: ViewerViewModel by viewModels(factoryProducer = { factory })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}