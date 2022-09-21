package com.example.geminiecurrencydemo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.geminiecurrencydemo.R
import com.example.geminiecurrencydemo.databinding.FragmentFirstBinding
import dagger.hilt.android.AndroidEntryPoint

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

        firstViewModel.getAllSymbols().observe(viewLifecycleOwner) { res ->
            res?.apply {
                if (success?.toString().toBoolean()) {

                    Toast.makeText(
                        context,
                        "done ${symbols.toString()}",
                        Toast.LENGTH_LONG
                    )
                        .show()

                } else {
                    Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.btnDetails.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}