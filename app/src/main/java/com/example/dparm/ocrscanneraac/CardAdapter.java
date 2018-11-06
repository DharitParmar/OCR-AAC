package com.example.dparm.ocrscanneraac;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.dparm.ocrscanneraac.database.CardEntry;

import java.util.List;


public class  CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {


    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    // Class variables for the List that holds card data and the Context
    private List<CardEntry> mCardEntries;
    private Context mContext;


    /**
     * Constructor for the CardAdapter that initializes the Context.
     *
     * @param context  the current Context
     * @param listener the ItemClickListener
     */
    public CardAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new CardViewHolder that holds the view for each task
     */
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.card_layout, parent, false);

        return new CardViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        // Determine the values of the wanted data
        CardEntry cardEntry = mCardEntries.get(position);
        String number = cardEntry.getCardNumber();

        //Set values
        holder.cardNumberView.setText(number);
    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mCardEntries == null) {
            return 0;
        }
        return mCardEntries.size();
    }

    public List<CardEntry> getCards() {
        return mCardEntries;
    }

    /**
     * When data changes, this method updates the list of cardEntries
     * and notifies the adapter to use the new values on it
     */
    public void setCards(List<CardEntry> cardEntries) {
        mCardEntries = cardEntries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    // Inner class for creating ViewHolders
    class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Class variables for the card number
        TextView cardNumberView;


        /**
         * Constructor for the CardViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public CardViewHolder(View itemView) {
            super(itemView);

            cardNumberView = itemView.findViewById(R.id.cardNumber);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = mCardEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}

