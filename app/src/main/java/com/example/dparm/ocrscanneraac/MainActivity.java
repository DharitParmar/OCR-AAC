/* This OCR utilizes Android Architecture Components for end to end development 
    Eg:
    Room ORM
    ViewModel
    LiveData
    Life Cycle
 */
package com.example.dparm.ocrscanneraac;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.dparm.ocrscanneraac.database.AppDatabase;
import com.example.dparm.ocrscanneraac.database.CardEntry;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends AppCompatActivity implements CardAdapter.ItemClickListener {

    // Member Variables
    private RecyclerView mRecyclerView;
    private CardAdapter mAdapter;
    private AppDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerViewCards);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new CardAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // implements swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        // get adapter position
                        int position = viewHolder.getAdapterPosition();
                        List<CardEntry> cards = mAdapter.getCards();
                        mDB.cardDao().deleteCard(cards.get(position));
                        retrieveCards();
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);

        /*
         Set the Floating Action Button (FAB) to its corresponding View.
         Attach an OnClickListener to it, so that when it's clicked, a new intent will be created
         to launch the CardScannerActivity.
         */
        FloatingActionButton fabButton = findViewById(R.id.fab);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an CardScannerActivity
                Intent addCardIntent = new Intent(MainActivity.this, CardScannerActivity.class);
                startActivity(addCardIntent);
            }
        });

        // load the database instance
        mDB = AppDatabase.getInstance(getApplicationContext());
    }

    /* This method calls after activity pause or stop so every time with new entry
    into database through activity_card_scanner it will show an updated list of cards
    */
    @Override
    protected void onResume() {
        super.onResume();
        retrieveCards();


    }

    private void retrieveCards() {
        // retrieves the Card Entry object into database using runnable within diskIO from AppExecutor
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // makes database call logic into runnable thread
                final List<CardEntry> cards = mDB.cardDao().loadAllCards();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setCards(cards);
                    }
                });

            }
        });
    }

    @Override
    public void onItemClickListener(int itemId) {
        // Launch CardScannerActivity adding the itemId as an extra in the intent
        // can put cardId as extra to perform update activity

    }

}
