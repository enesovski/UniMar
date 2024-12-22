package com.x_force.unimar.chat;

import android.app.Fragment;
import com.x_force.unimar.chat.adapters.RecentChat;

public class ChatFragment extends Fragment {

    RecentChat adapter;

    @Override
    public void onStart() {
        super.onStart();
        if(adapter != null){
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter != null){
            adapter.startListening();
        }
    }
}
