package com.example.myapplication.ui.search

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.cars.CarAdapter
import com.example.myapplication.ui.cars.renderCarsState

class SearchResultsFragment : Fragment(R.layout.fragment_search_results) {

    private val viewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = CarAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.state.observe(viewLifecycleOwner) { view.renderCarsState(it, adapter) }
        if (viewModel.state.value == null) viewModel.search(requireArguments().getString(ARG_QUERY).orEmpty())

        view.findViewById<Button>(R.id.btnRetry).setOnClickListener { viewModel.retry() }
        view.findViewById<View>(R.id.btnBack).setOnClickListener { parentFragmentManager.popBackStack() }
    }

    companion object {
        private const val ARG_QUERY = "query"

        fun newInstance(query: String) = SearchResultsFragment().apply {
            arguments = bundleOf(ARG_QUERY to query)
        }
    }
}
