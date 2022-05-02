package com.example.rxkotlin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.rxkotlin.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*

@AndroidEntryPoint
class MainFragment : Fragment() {

    lateinit var navController : NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewBinding()
    }

    private fun viewBinding() {
        btnCreateOperators.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_createOperatorsFragment)
        }
        btnFilterOperators.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_filterOperatorsFragment)
        }

        btnTransformOperators.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_transformationOperatorsFragment)
        }
    }
}