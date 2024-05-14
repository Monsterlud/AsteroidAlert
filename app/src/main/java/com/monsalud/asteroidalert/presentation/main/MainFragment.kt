package com.monsalud.asteroidalert.presentation.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.monsalud.asteroidalert.R
import com.monsalud.asteroidalert.data.AsteroidRepository
import com.monsalud.asteroidalert.data.local.room.AsteroidDatabase
import com.monsalud.asteroidalert.databinding.FragmentMainBinding
import kotlinx.coroutines.launch


class MainFragment : Fragment() {

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

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val adapter = AsteroidAdapter(AsteroidClickListener { asteroid ->
            Toast.makeText(context, "Asteroid ID: ${asteroid.id}", Toast.LENGTH_SHORT).show()
            viewModel.onAsteroidItemClicked(asteroid)
        })
        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroids.observe(viewLifecycleOwner) { asteroids ->
            adapter.submitList(asteroids)
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

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
