package com.monsalud.asteroidalert.presentation.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.monsalud.asteroidalert.R
import com.monsalud.asteroidalert.databinding.FragmentDetailBinding
import com.monsalud.asteroidalert.presentation.displayExplanationDialog


class DetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val asteroid = DetailFragmentArgs.fromBundle(requireArguments()).selectedAsteroid

        val viewModelFactory = DetailViewModelFactory(asteroid, application)
        binding.viewModel = ViewModelProvider(
            this, viewModelFactory
        )[DetailViewModel::class.java]

        binding.helpButton.setOnClickListener {
            displayExplanationDialog(requireContext(), getString(R.string.astronomica_unit_explanation))
        }

        return binding.root
    }
}
