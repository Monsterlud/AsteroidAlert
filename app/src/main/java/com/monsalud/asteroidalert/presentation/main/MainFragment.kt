package com.monsalud.asteroidalert.presentation.main

import android.content.Context
import android.os.Bundle
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
import com.monsalud.asteroidalert.data.remote.getEndOfWeekDate
import com.monsalud.asteroidalert.data.remote.getTodayDate
import com.monsalud.asteroidalert.databinding.FragmentMainBinding
import com.monsalud.asteroidalert.presentation.displayExplanationDialog
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone


class MainFragment : Fragment() {
    private lateinit var apodDescription: String
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: AsteroidAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        apodDescription = getString(R.string.default_apod_description)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /**
         * Setup binding object and set astronomy picture of the day to default
         * so that there is a photo while image is loading
         */

        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )
        binding.ivImageOfTheDay.setImageResource(R.drawable.default_nasa_image)

        /**
         * Setup ViewModel & bind this ViewModel with the layout xml file
         */
        val application = requireNotNull(this.activity).application
        val database = AsteroidDatabase.getInstance(application)
        val asteroidRepository = AsteroidRepository(database)
        val viewModelFactory = MainViewModelFactory(asteroidRepository)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[MainViewModel(asteroidRepository)::class.java]
        binding.viewModel = viewModel
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.clearAsteroids()
//        }

        /**
         * Setup Adapter & bind this Adapter with the layout file's RecyclerView
         * Also, set the layout file's lifecycleOwner to this Fragment
         */
        adapter = AsteroidAdapter(AsteroidClickListener { asteroid ->
            viewModel.onAsteroidItemClicked(asteroid)
        })
        binding.asteroidRecycler.adapter = adapter
        binding.lifecycleOwner = this

        /**
         * Observe all ViewModel LiveData values
         */
        viewModel.pictureOfTheDay.observe(viewLifecycleOwner) { apod ->
            apod?.let {
                apodDescription = apod.explanation
            } ?: run {
                apodDescription = getString(R.string.default_apod_description)
            }
            val picasso = Picasso.Builder(context).build()
            apod?.let {
                picasso.load(apod.hdurl).into(binding.ivImageOfTheDay)
            } ?: run {
                picasso.load(R.drawable.default_nasa_image)
            }
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
            R.id.show_image_explanation -> {
                if (apodDescription.isEmpty()) displayExplanationDialog(
                    requireContext(),
                    apodDescription
                )
                else displayExplanationDialog(
                    requireContext(),
                    getString(R.string.default_apod_description)
                )
                true
            }

            R.id.view_week_asteroids -> {
                val weeksAsteroids = viewModel.asteroids.value?.filter { asteroid ->
                    val calendar = Calendar.getInstance()
                    calendar.timeZone = TimeZone.getDefault()
                    asteroid.closeApproachDate in getTodayDate().. getEndOfWeekDate(calendar)
                }
                adapter.submitList(weeksAsteroids?.sortedBy {
                    it.closeApproachDate
                })
                true
            }

            R.id.view_today_asteroids -> {
                val todaysAsteroids = viewModel.asteroids.value?.filter { asteroid ->
                    asteroid.closeApproachDate == getTodayDate()
                }
                adapter.submitList(todaysAsteroids)
                true
            }

            R.id.view_saved_asteroids -> {
                val savedAsteroids = viewModel.asteroids.value
                adapter.submitList(savedAsteroids?.sortedBy {
                    it.closeApproachDate
                })
                true
            }

            else -> true
        }
    }
}
