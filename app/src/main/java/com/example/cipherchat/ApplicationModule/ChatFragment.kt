package com.example.cipherchat.ApplicationModule

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cipherchat.BackendHandler.Contact
import com.example.cipherchat.BackendHandler.ContactAdapter
import com.example.cipherchat.R

class ChatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private val contactsList = mutableListOf<Contact>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        recyclerView = view.findViewById(R.id.all_contacts)
        recyclerView.layoutManager = LinearLayoutManager(context)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
            == PackageManager.PERMISSION_GRANTED) {
            fetchContacts()
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.READ_CONTACTS), 1)
        }

        return view
    }

    private fun fetchContacts() {
        val contentResolver: ContentResolver = requireActivity().contentResolver
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            val uniqueNames = mutableSetOf<String>()

            while (it.moveToNext()) {
                val name = it.getString(nameIndex)
                val number = it.getString(numberIndex)

                if (uniqueNames.add(name)) {
                    contactsList.add(Contact(name, number, R.drawable.avatar))
                }
            }
        }

        contactsList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })

        if (contactsList.isEmpty()) {
            Toast.makeText(requireContext(), "No contacts found", Toast.LENGTH_SHORT).show()
        } else {
            contactAdapter = ContactAdapter(contactsList)
            recyclerView.adapter = contactAdapter
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchContacts()
            } else {
                Toast.makeText(requireContext(), "Permission denied to read contacts", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
