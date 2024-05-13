package com.monsalud.asteroidalert.presentation.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.monsalud.asteroidalert.R
import com.monsalud.asteroidalert.data.local.room.AsteroidDatabase
import com.monsalud.asteroidalert.data.local.room.AsteroidDatabaseDao
import com.monsalud.asteroidalert.databinding.FragmentMainBinding


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
        val dataSource = AsteroidDatabase.getInstance(application).asteroidDatabaseDao
        val viewModelFactory = MainViewModelFactory(dataSource)
        val viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[MainViewModel(dataSource)::class.java]
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = AsteroidAdapter(AsteroidListener { asteroidId ->
            viewModel.onAsteroidItemClicked(asteroidId)
            Toast.makeText(context, "Asteroid Id: $asteroidId", Toast.LENGTH_SHORT).show()
            viewModel.doneNavigating()
        })
        binding.asteroidRecycler.adapter = adapter

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
