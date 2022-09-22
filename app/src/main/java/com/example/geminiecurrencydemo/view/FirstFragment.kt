package com.example.geminiecurrencydemo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.geminiecurrencydemo.R
import com.example.geminiecurrencydemo.databinding.FragmentFirstBinding
import com.example.geminiecurrencydemo.utils.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.loading_layout.*

@AndroidEntryPoint
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    private val firstViewModel by viewModels<FirstViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()

        binding.btnDetails.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }


    private fun initObservers() {

        firstViewModel.getAllSymbols().observe(viewLifecycleOwner) {
            when (it.getStatus()) {

                DataState.DataStatus.LOADING -> {
                    showHideLoading(isLoading = true)
                }
                DataState.DataStatus.SUCCESS -> {
                    showHideLoading()
                    it.getData()?.let { list ->
                        initDropdownList(list, binding.currencyFrom)
                        initDropdownList(list, binding.currencyTo)
                    }
                }
                DataState.DataStatus.ERROR -> {
                    showHideLoading(hasError = true, txt = "${it.getError()}")
                }
                DataState.DataStatus.NO_INTERNET -> {
                    showHideLoading(hasError = true, txt = "${it.getNoInternet()}")
                }

            }
        }

    }

    private fun initDropdownList(currencies: ArrayList<String?>, sp_currencies: AppCompatSpinner) {

        val adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                currencies
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_currencies.adapter = adapter
        sp_currencies.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

    }

    private fun showHideLoading(
        isLoading: Boolean = false,
        hasError: Boolean = false,
        txt: String = ""
    ) {

        ll_convert.isVisible = !(isLoading || hasError)
        ll_loading.isVisible = isLoading || hasError
        pb_progressbar.isVisible = isLoading
        tv_error.isVisible = hasError
        tv_error.text = txt

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}