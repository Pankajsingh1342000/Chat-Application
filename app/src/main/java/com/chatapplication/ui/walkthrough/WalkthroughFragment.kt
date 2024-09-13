package com.chatapplication.ui.walkthrough

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.chatapplication.R
import com.chatapplication.databinding.FragmentWalkthroughBinding

class WalkthroughFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentWalkthroughBinding
    private lateinit var btnWalkthrough: Button
    private lateinit var navController: NavController
    private var backPressedTime: Long = 0
    private lateinit var toast: Toast

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalkthroughBinding.inflate(inflater, container, false)
        btnWalkthrough = binding.btnWalkthrough
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBackPress()
        setListeners()
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    toast.cancel()
                    requireActivity().finish()
                } else {
                    toast = Toast.makeText(requireContext(), "Press again to exit", Toast.LENGTH_SHORT)
                    toast.show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        })
    }

    private fun setListeners() {
        btnWalkthrough.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v){
            btnWalkthrough -> {
                navController.navigate(R.id.action_walkthroughFragment_to_phoneLoginFragment)
            }
        }
    }

}