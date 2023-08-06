package com.example.duckhunt.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.duckhunt.R;
import com.example.duckhunt.models.User;

import java.util.List;

public class MyUserRecyclerViewAdapter extends RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;

    public MyUserRecyclerViewAdapter(List<User> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_ranking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        int pos = position + 1;
        holder.textViewPosition.setText(position + "°");
        holder.textViewvDucks.setText(String.valueOf( mValues.get(position).getDucks()));
        holder.textViewvNick.setText(mValues.get(position).getNick());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textViewPosition;
        public final TextView textViewvDucks;
        public final TextView textViewvNick;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewPosition = view.findViewById(R.id.textViewPosition);
            textViewvDucks = (TextView) view.findViewById(R.id.textViewDuck);
            textViewvNick = view.findViewById(R.id.textViewNick);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewvNick.getText() + "'";
        }
    }
}
