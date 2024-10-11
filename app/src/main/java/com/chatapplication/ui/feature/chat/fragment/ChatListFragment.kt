package com.chatapplication.ui.feature.chat.fragment

import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chatapplication.MainActivity
import com.chatapplication.databinding.FragmentChatListBinding
import com.chatapplication.permission_manager.PermissionManager
import com.chatapplication.permission_manager.Permissions
import com.chatapplication.ui.feature.chat.adapter.ChatsListAdapter
import com.chatapplication.ui.feature.chat.viewmodel.ChatListViewModel
import com.chatapplication.util.FabClickListener
import com.chatapplication.util.SharedPreferenceManager

class ChatListFragment : Fragment(), FabClickListener {

    private lateinit var binding: FragmentChatListBinding
    private lateinit var sharedPreference: SharedPreferenceManager
    private lateinit var adapter: ChatsListAdapter
    private lateinit var permissionManager : PermissionManager
    private lateinit var pickContact : ActivityResultLauncher<Void?>
    private val chatListViewModel: ChatListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatListBinding.inflate(layoutInflater)
        sharedPreference = SharedPreferenceManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.setFabClickListener(this)
        permissionManager = PermissionManager.from(this)
        pickContact = this.registerForActivityResult(ActivityResultContracts.PickContact()) { uri ->
            uri?.let { fetchContactDetails(it) }
        }
        adapter = ChatsListAdapter {chatId ->
            val action = ChatListFragmentDirections.actionChatListFragmentToChatFragment(chatId,null,null)
            findNavController().navigate(action)
        }

        binding.rvChats.adapter = adapter

        // Observe the chat list data
        chatListViewModel.fetchChatList()
        chatListViewModel.chatList.observe(viewLifecycleOwner) {chatList ->
            adapter.submitList(chatList)
        }
    }
    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setFabClickListener(this)
    }
    override fun onFabClick() {
        permissionManager
            .request(Permissions.ContactsPerm)
            .rationale(
                title = "Permission Required",
                description = "This app needs permission to access contacts"
            )
            .permissionPermanentlyDeniedContent(description = "You have permanently denied the permission. Please enable it from settings")
            .checkAndRequestPermission {
                if (it) {
                    pickContact.launch(null)
                } else {
                    Toast.makeText(this.context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Fetch selected contact details from phonebook
    private fun fetchContactDetails(uri: Uri) {
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val name = it.getString(nameIndex)

                val contactIdIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
                val contactId = it.getString(contactIdIndex)
                val phoneCursor = requireContext().contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                    arrayOf(contactId),
                    null
                )
                phoneCursor?.use { pCursor ->
                    if (pCursor.moveToFirst()) {
                        val phoneIndex = pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val phoneNumber = pCursor.getString(phoneIndex)

                        // After retrieving contact, navigate to ChatFragment with the contact's data
                        val action = ChatListFragmentDirections.actionChatListFragmentToChatFragment(
                            chatId = "", // chatId is empty since it's a new chat
                            contactName = name,
                            contactPhoneNumber = phoneNumber
                        )
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

}