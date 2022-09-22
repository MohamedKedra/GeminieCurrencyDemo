package com.example.geminiecurrencydemo.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.geminiecurrencydemo.R
import com.example.geminiecurrencydemo.databinding.FragmentFirstBinding
import com.example.geminiecurrencydemo.network.models.Currency
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

        binding.apply {

            btnDetails.setOnClickListener {
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }

            btnSwap.setOnClickListener {
                val swap = currencyFrom.selectedItemPosition
                currencyFrom.setSelection(currencyTo.selectedItemPosition)
                currencyTo.setSelection(swap)
            }

            etFrom.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(txt: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    observeConvertCurrency(
                        currencyFrom.selectedItem.toString(),
                        currencyTo.selectedItem.toString(),
                        txt?.toString()!!
                    )
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })

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
                        initDropdownList(list, binding.currencyFrom, isFromCurrency = true)
                        initDropdownList(list, binding.currencyTo, isFromCurrency = false)
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


    private fun observeConvertCurrency(fromCurrency: String, toCurrency: String, amount: String) {

        firstViewModel.convertCurrency(fromCurrency, toCurrency, amount)
            .observe(viewLifecycleOwner) { response ->

                when (response.getStatus()) {

                    DataState.DataStatus.LOADING -> {
                        showHideLoading(isLoading = true)
                    }
                    DataState.DataStatus.SUCCESS -> {
                        showHideLoading()
                        response.getData()?.let {
                            binding.etTo.setText(it.result?.toString())
                        }
                    }
                    DataState.DataStatus.ERROR -> {
                        showHideLoading(
                            hasError = true,
                            txt = response.getError()?.message.toString()
                        )
                    }
                    DataState.DataStatus.NO_INTERNET -> {
                        showHideLoading(
                            hasError = true,
                            txt = "No Internet Connection"
                        )
                    }
                }
            }
    }

    private fun initDropdownList(
        currencies: ArrayList<Currency>,
        sp_currencies: AppCompatSpinner,
        isFromCurrency: Boolean
    ): AppCompatSpinner {

        val names = ArrayList<String>()
        currencies.forEach { names.add(it.code) }

        val adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                names
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_currencies.adapter = adapter
        sp_currencies.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

                val item = parent.getItemAtPosition(position).toString()
                if (isFromCurrency) {
                    observeConvertCurrency(
                        item,
                        binding.currencyTo.selectedItem.toString(),
                        binding.etFrom.text.toString()
                    )
                } else {
                    observeConvertCurrency(
                        binding.currencyFrom.selectedItem.toString(),
                        item,
                        binding.etFrom.text.toString()
                    )
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        return sp_currencies
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