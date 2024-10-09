package com.chatapplication.ui.feature.chat.fragment

import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.chatapplication.MainActivity
import com.chatapplication.R
import com.chatapplication.databinding.FragmentChatBinding
import com.chatapplication.ui.feature.chat.adapter.ChatsListAdapter
import com.chatapplication.ui.feature.chat.model.ChatList
import com.chatapplication.util.Util.KeyboardHelper.hideKeyboard
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText


class ChatFragment : Fragment(), View.OnClickListener, TextWatcher {
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
    private lateinit var edtMessage: TextInputEditText
    private lateinit var fabMic: FloatingActionButton
    private lateinit var fabSend: FloatingActionButton
    private lateinit var navController: NavController
    private lateinit var clRootEditTextFieldContainer: ConstraintLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(layoutInflater)
        btnBack = binding.ivBack
        profileImage = binding.ivProfile
        name = binding.tvName
        videoCall = binding.ivVideoCall
        call = binding.ivVideoCall
        menu = binding.ivMenu
        rvMessage = binding.rvMessage
        ibEmoji = binding.ibEmoji
        ibAttach = binding.ibAttachment
        ibCamera = binding.ibCamera
        edtMessage = binding.edtMessage
        fabMic = binding.fabMic
        fabSend = binding.fabSend
        clRootEditTextFieldContainer = binding.layoutTextField
        navController = findNavController()
        setupWindowInsets()
        (activity as? MainActivity)?.setViewPagerSwipeEnabled(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.hideMainContent()
        handleBackPress()
        setListeners()
//        val dummyChats = List(10) { index ->
//            ChatList(
//                name = "Person ${index+1}",
//                message = "This is a dummy message for person ${index+1}.",
//                timeStamp = "10:0${index+1} AM",
//                unreadMessageCount = index+1
//            )
//        }
//
//        binding.rvMessage.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = ChatsListAdapter(dummyChats)
//        }
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
        val textFieldLayout = binding.layoutTextField
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())

            val layoutParams = textFieldLayout.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.bottomMargin = if (isKeyboardVisible) {
                insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            } else {
                systemBars.bottom
            }
            textFieldLayout.layoutParams = layoutParams

            insets
        }
    }


    private fun setListeners() {
        btnBack.setOnClickListener(this)
        edtMessage.addTextChangedListener(this)
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.popBackStack()
                (activity as? MainActivity)?.showMainContent()

            }
        })
    }

    override fun onClick(v: View?) {
        when (v) {
            btnBack -> {
                this.activity?.let { hideKeyboard(it) }
                navController.popBackStack()
                (activity as? MainActivity)?.showMainContent()
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(s?.isNotEmpty() == true){
            if(s.toString().trim().isNotEmpty() && ibCamera.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(clRootEditTextFieldContainer, ChangeBounds())
                ibCamera.visibility = View.GONE
                TransitionManager.beginDelayedTransition(clRootEditTextFieldContainer, Fade())
                fabSend.visibility = View.VISIBLE
                fabMic.visibility = View.INVISIBLE
            }
        }
        else{

            clRootEditTextFieldContainer.post {

                val transitionSet = TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(Fade())
                    .setDuration(200)

                TransitionManager.beginDelayedTransition(clRootEditTextFieldContainer, transitionSet)

                ibCamera.visibility = View.VISIBLE
                fabSend.visibility = View.INVISIBLE
                fabMic.visibility = View.VISIBLE
            }

        }
    }

    override fun afterTextChanged(s: Editable?) {
    }
}