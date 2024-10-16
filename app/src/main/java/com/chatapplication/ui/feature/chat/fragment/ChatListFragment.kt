package com.chatapplication.ui.feature.chat.fragment

import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chatapplication.MainActivity
import com.chatapplication.databinding.FragmentChatListBinding
import com.chatapplication.permission_manager.PermissionManager
import com.chatapplication.permission_manager.Permissions
import com.chatapplication.ui.feature.chat.adapter.ChatsListAdapter
import com.chatapplication.ui.feature.chat.model.ContactInfo
import com.chatapplication.ui.feature.chat.viewmodel.ChatListViewModel
import com.chatapplication.util.FabClickListener
import com.chatapplication.util.SharedPreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatListFragment : Fragment(), FabClickListener {

    private lateinit var binding: FragmentChatListBinding
    private lateinit var sharedPreference: SharedPreferenceManager
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
            if (uri != null) {
                // Fetch contact details after picking the contact
                val contactInfo = fetchContactDetails(uri)
                saveContactToFirebase(contactInfo)
            }
        }

        val chatListAdapter = ChatsListAdapter(emptyList()) { chat ->
            val action = ChatListFragmentDirections.actionChatListFragmentToChatFragment(chat.chatId, chat.name, null)
            findNavController().navigate(action)
        }

        binding.rvChats.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatListAdapter
        }

        chatListViewModel.chatList.observe(viewLifecycleOwner) { chatList ->
            chatListAdapter.updateList(chatList)
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            chatListViewModel.fetchChatList(it.uid)
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
    private fun fetchContactDetails(contactUri: Uri): ContactInfo  {
        var contactName: String? = null
        var contactPhone: String? = null

        val cursor = requireContext().contentResolver.query(
            contactUri, null, null, null, null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                contactName = it.getString(nameIndex)

                val idIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
                val contactId = it.getString(idIndex)

                val phoneCursor = requireContext().contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                    arrayOf(contactId),
                    null
                )

                phoneCursor?.use { phoneCur ->
                    if (phoneCur.moveToFirst()) {
                        val phoneIndex =
                            phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        contactPhone = phoneCur.getString(phoneIndex)
                    }
                }
            }
        }

        return ContactInfo(contactName ?: "", contactPhone ?: "")
    }

    // Save contact to Firebase Firestore
    private fun saveContactToFirebase(contact: ContactInfo) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val firestore = FirebaseFirestore.getInstance()

        if (currentUser != null && contact.phoneNumber.isNotEmpty()) {
            val contactData = hashMapOf(
                "name" to contact.name,
                "phoneNumber" to contact.phoneNumber,
                "timestamp" to System.currentTimeMillis()
            )

            firestore.collection("users").document(currentUser.uid)
                .collection("contacts").document(contact.phoneNumber)
                .set(contactData)
                .addOnSuccessListener {
                    // Navigate to the ChatFragment after adding the contact
                    val action = ChatListFragmentDirections.actionChatListFragmentToChatFragment(null,contact.name,contact.phoneNumber)
                    findNavController().navigate(action)
                }
                .addOnFailureListener { e ->
                    // Handle error
                    Log.e("Firestore", "Error adding contact", e)
                }
        }
    }
}