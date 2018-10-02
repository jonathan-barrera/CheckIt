package com.example.android.checkit.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.checkit.EditCheckOutActivity;
import com.example.android.checkit.R;
import com.example.android.checkit.models.CheckOutEvent;
import com.example.android.checkit.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonathanbarrera on 9/30/18.
 */

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.ViewHolder> {

    // Constants
    public static final String CHECKOUT_EXTRA_KEY = "checkout-extra-key";

    // Member variables
    private List<CheckOutEvent> mCheckOutEvents;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.check_out_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // Extract info for current check out
        final CheckOutEvent checkOut = mCheckOutEvents.get(position);
        String accommodation = checkOut.getAccommodation();
        String date = DateUtils.convertDateLongToString(checkOut.getDate());

        // Populate the textviews
        holder.mAccomTextView.setText(accommodation);
        holder.mDateTextView.setText(date);

        // Set an onClickListener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.mAccomTextView.getContext(), EditCheckOutActivity.class);
                intent.putExtra(CHECKOUT_EXTRA_KEY, checkOut);
                holder.mDateTextView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCheckOutEvents == null) {
            return 0;
        } else {
            return mCheckOutEvents.size();
        }
    }

    // Method for setting the list of data
    public void setCheckOutData(List<CheckOutEvent> checkOutData) {
        mCheckOutEvents = checkOutData;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.check_out_view_holder_accom_text_view)
        TextView mAccomTextView;
        @BindView(R.id.check_out_view_holder_date_text_view)
        TextView mDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
