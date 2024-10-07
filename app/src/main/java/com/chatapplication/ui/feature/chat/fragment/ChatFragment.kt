package com.chatapplication.ui.feature.chat.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chatapplication.MainActivity
import com.chatapplication.R
import com.chatapplication.databinding.FragmentChatBinding
import com.chatapplication.ui.feature.chat.adapter.ChatsListAdapter
import com.chatapplication.ui.feature.chat.model.ChatList
import com.chatapplication.util.Util.KeyboardHelper.hideKeyboard
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView


class ChatFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentChatBinding
    private lateinit var btnBack: ImageView
    private lateinit var profileImage: ShapeableImageView
    private lateinit var name: TextView
    private lateinit var videoCall: ImageView
    private lateinit var call: ImageView
    private lateinit var menu : ImageView
    private lateinit var rvMessage: RecyclerView
    private lateinit var ibEmoji: ImageButton
    private lateinit var ibAttach: ImageButton
    private lateinit var ibCamera: ImageButton
    private lateinit var fabMic: FloatingActionButton
    private lateinit var fabSend: FloatingActionButton
    private lateinit var navController: NavController
//    private var keyboardMargin: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(layoutInflater)
        btnBack = binding.ivBack
        navController = findNavController()
        setupWindowInsets()
        (activity as? MainActivity)?.setViewPagerSwipeEnabled(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        keyboardMargin = resources.getDimensionPixelSize(R.dimen.keyboard_margin)
        (activity as? MainActivity)?.hideMainContent()
        handleBackPress()
        setListeners()
//        observeKeyboardVisibility()
        val dummyChats = List(10) { index ->
            ChatList(
                name = "Person ${index+1}",
                message = "This is a dummy message for person ${index+1}.",
                timeStamp = "10:0${index+1} AM",
                unreadMessageCount = index+1
            )
        }

        binding.rvMessage.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ChatsListAdapter(dummyChats)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.hideMainContent()
        (activity as? MainActivity)?.setViewPagerSwipeEnabled(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as? MainActivity)?.setViewPagerSwipeEnabled(true)
    }
    private fun setupWindowInsets() {
        val mainLayout = binding.layoutChat
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val keyboardInsets = insets.getInsets(WindowInsetsCompat.Type.ime())

            view.setPadding(
                resources.getDimensionPixelSize(R.dimen.screen_padding_horizontal),
                resources.getDimensionPixelSize(R.dimen.header_padding_vertical),
                resources.getDimensionPixelSize(R.dimen.screen_padding_horizontal),
                systemBars.bottom + keyboardInsets.bottom
            )
            insets
        }
    }

    private fun setListeners() {
        btnBack.setOnClickListener(this)
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.popBackStack()
                (activity as? MainActivity)?.showMainContent()

            }
        })
    }

//    private fun observeKeyboardVisibility() {
//        val rootView = binding.root
//        rootView.viewTreeObserver.addOnGlobalLayoutListener {
//            val rect = Rect()
//            rootView.getWindowVisibleDisplayFrame(rect)
//            val screenHeight = rootView.rootView.height
//            val keypadHeight = screenHeight - rect.bottom
//
//            if (keypadHeight > screenHeight * 0.15) {
//                // Keyboard is visible
//                binding.layoutTextField.setPadding(0, 0, 0, keypadHeight - keyboardMargin)
//            } else {
//                // Keyboard is hidden
//                binding.layoutTextField.setPadding(0, 0, 0, 0)
//            }
//        }
//    }

    override fun onClick(v: View?) {
        when (v) {
            btnBack -> {
                this.activity?.let { hideKeyboard(it) }
                navController.popBackStack()
                (activity as? MainActivity)?.showMainContent()
            }
        }
    }
}