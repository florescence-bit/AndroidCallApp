package com.adivya.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {

    private static final int READ_CONTACTS_PERMISSION_REQUEST_CODE = 1005;

    private RecyclerView contactsRecyclerView;
    private ContactsAdapter contactsAdapter;
    private SearchView searchView;
    private List<Contact> allContacts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        contactsRecyclerView = view.findViewById(R.id.contacts_recycler_view);
        searchView = view.findViewById(R.id.search_view);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_PERMISSION_REQUEST_CODE);
        } else {
            loadContacts();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterContacts(newText);
                return true;
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_CONTACTS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts();
            } else {
                Toast.makeText(requireContext(), "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadContacts() {
        allContacts = new ArrayList<>();
        Cursor cursor = requireActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int photoUriIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);

            while (cursor.moveToNext()) {
                String name = cursor.getString(nameIndex);
                String number = cursor.getString(numberIndex);
                String photoUri = cursor.getString(photoUriIndex);
                allContacts.add(new Contact(name, number, photoUri));
            }
            cursor.close();
        }

        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        contactsAdapter = new ContactsAdapter(allContacts);
        contactsRecyclerView.setAdapter(contactsAdapter);
    }

    private void filterContacts(String text) {
        List<Contact> filteredContacts = new ArrayList<>();
        for (Contact contact : allContacts) {
            if (contact.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredContacts.add(contact);
            }
        }
        contactsAdapter = new ContactsAdapter(filteredContacts);
        contactsRecyclerView.setAdapter(contactsAdapter);
    }
}
