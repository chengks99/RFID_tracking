package com.example.rfid.ui.locate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rfid.RFIDHandler;
import com.example.rfid.databinding.FragmentLocateTagBinding;
import com.zebra.rfid.api3.TagData;

public class LocateTagFragment extends Fragment{

    private FragmentLocateTagBinding binding;
    RFIDHandler rfidHandler;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LocateTagViewModel locateTagViewModel =
                new ViewModelProvider(this).get(LocateTagViewModel.class);

        binding = FragmentLocateTagBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        rfidHandler = new RFIDHandler(requireContext(),this);


//        final TextView textView = binding.textDashboard;
//        locateTagViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}