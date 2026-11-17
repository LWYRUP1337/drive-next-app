package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.cars.CarAdapter
import com.example.myapplication.ui.cars.renderCarsState
import com.example.myapplication.ui.main.MainActivity
import com.example.myapplication.ui.search.SearchResultsFragment

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = CarAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.state.observe(viewLifecycleOwner) { view.renderCarsState(it, adapter) }
        view.findViewById<Button>(R.id.btnRetry).setOnClickListener { viewModel.load() }

        val searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.findViewById<TextView>(androidx.appcompat.R.id.search_src_text).textSize = 14f
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    searchView.clearFocus()
                    (requireActivity() as MainActivity).openScreen(SearchResultsFragment.newInstance(query.trim()))
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })
    }
}
