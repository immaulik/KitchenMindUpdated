package com.example.kitchenmind.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.example.kitchenmind.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class categoryAdapter extends RecyclerView.Adapter<categoryAdapter.categoryAdapterView> {

    ArrayList<String> catID;
    ArrayList<String> catName;

    OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public categoryAdapter(ArrayList<String> catID, ArrayList<String> catName) {

        this.catID = catID;
        this.catName = catName;

    }

    @NonNull
    @Override
    public categoryAdapterView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catlist_file, parent, false);

        return new categoryAdapterView(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final categoryAdapterView holder, int position) {

        holder.catTextView.setText(catName.get(position));
    }

    @Override
    public int getItemCount() {
        return catID.size();
    }

    public static class categoryAdapterView extends RecyclerView.ViewHolder {

        TextView catTextView;

        public categoryAdapterView(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            catTextView = itemView.findViewById(R.id.catIDList);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener!=null){
                        int position = getAdapterPosition();

                        if (position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }

                    }
                }
            });
        }
    }
}
