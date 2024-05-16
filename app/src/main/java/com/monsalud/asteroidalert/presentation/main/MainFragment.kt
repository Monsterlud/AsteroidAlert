package com.monsalud.asteroidalert.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.monsalud.asteroidalert.R
import com.monsalud.asteroidalert.data.AsteroidRepository
import com.monsalud.asteroidalert.data.local.room.AsteroidDatabase
import com.monsalud.asteroidalert.databinding.FragmentMainBinding
import com.monsalud.asteroidalert.presentation.displayExplanationDialog
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class MainFragment : Fragment() {
    var apodDescription: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )
        val application = requireNotNull(this.activity).application
        val database = AsteroidDatabase.getInstance(application)
        val asteroidRepository = AsteroidRepository(database)
        val viewModelFactory = MainViewModelFactory(asteroidRepository)
        val viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[MainViewModel(asteroidRepository)::class.java]
        binding.viewModel = viewModel
        val adapter = AsteroidAdapter(AsteroidClickListener { asteroid ->
            viewModel.onAsteroidItemClicked(asteroid)
        })
        binding.asteroidRecycler.adapter = adapter
        binding.lifecycleOwner = this

        viewModel.pictureOfTheDay.observe(viewLifecycleOwner) { apod ->
            apod?.let {
                apodDescription = apod.explanation
            } ?: run {
                apodDescription = getString(R.string.default_apod_description)
            }
            val picasso = Picasso.Builder(context).build()
            picasso.load(apod?.hdurl).into(binding.ivImageOfTheDay)
        }

        viewModel.asteroids.observe(viewLifecycleOwner) { asteroids ->
            adapter.submitList(asteroids.sortedBy {
                it.closeApproachDate
            })
        }

        viewModel.navigateToAsteroidDetail.observe(viewLifecycleOwner) { asteroid ->
            viewLifecycleOwner.lifecycleScope.launch {
                asteroid?.let {
                    this@MainFragment.findNavController()
                        .navigate(MainFragmentDirections.actionShowDetail(asteroid))
                    viewModel.doneNavigating()
                }
            }
        }

        viewModel.loadingStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                AsteroidApiStatus.LOADING -> binding.statusLoadingWheel.isVisible
                AsteroidApiStatus.DONE -> binding.statusLoadingWheel.isInvisible
                else -> Toast.makeText(
                    context,
                    "There was an error loading data from Nasa",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.show_image_explanation-> {
                if (apodDescription.isNullOrEmpty()) displayExplanationDialog(requireContext(), apodDescription)
                else displayExplanationDialog(requireContext(), getString(R.string.default_apod_description))
                true
            }
            R.id.view_week_asteroids-> {

                true
            }
            R.id.view_today_asteroids-> {

                true
            }
            R.id.view_saved_asteroids-> {

                true
            }

            else -> true
        }
    }
}
