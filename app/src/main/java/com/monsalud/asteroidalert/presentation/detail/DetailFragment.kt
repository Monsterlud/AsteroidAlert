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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /**
         * Setup binding object
         */
        val binding = FragmentDetailBinding.inflate(inflater)

        // setting the app toolbar title here improves TalkBack announcement
        requireActivity().title = getString(R.string.app_name)

        val asteroid = DetailFragmentArgs.fromBundle(requireArguments()).selectedAsteroid

        /**
         * Setup ViewModel & bind this ViewModel with the layout xml file
         * Set the xml lifecycleOwner to this Fragment
         */
        val application = requireNotNull(activity).application
        val viewModelFactory = DetailViewModelFactory(asteroid, application)
        binding.viewModel = ViewModelProvider(
            this, viewModelFactory
        )[DetailViewModel::class.java]
        binding.lifecycleOwner = this

        /**
         * Setup click listener for the help button
         */
        binding.helpButton.setOnClickListener {
            displayExplanationDialog(requireContext(), getString(R.string.astronomica_unit_explanation))
        }

        return binding.root
    }
}
