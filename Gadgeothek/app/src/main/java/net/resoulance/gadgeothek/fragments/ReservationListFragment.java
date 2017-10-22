package net.resoulance.gadgeothek.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.resoulance.gadgeothek.R;
import net.resoulance.gadgeothek.adapter.ReservationsAdapter;
import net.resoulance.gadgeothek.domain.Gadget;
import net.resoulance.gadgeothek.domain.Loan;
import net.resoulance.gadgeothek.domain.Reservation;
import net.resoulance.gadgeothek.service.Callback;
import net.resoulance.gadgeothek.service.ItemSelectionListener;
import net.resoulance.gadgeothek.service.LibraryService;

import java.util.ArrayList;
import java.util.List;

public class ReservationListFragment extends Fragment {
    private List<Gadget> allGadgets = new ArrayList<>();
    private ArrayList<Gadget> reservableGadgets = new ArrayList<>();
    private int itemToReserve;
    private ItemSelectionListener itemSelectionCallback = null;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ReservationsAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reservation_list, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.reservationRecyclerView);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe

                if (direction == ItemTouchHelper.LEFT) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //alert for confirm to delete
                    builder.setMessage("Wollen Sie die Reservierung löschen?");

                    builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() { //when click on DELETE
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyItemRemoved(position);
                            adapter.deleteEntry(position);
                            return;
                        }
                    }).setNegativeButton("Nein", new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyItemRemoved(position + 1);
                            adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                            return;
                        }
                    }).show();
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        getReservations();

        FloatingActionButton floatingActionButton = (FloatingActionButton)rootView.findViewById(R.id.fabReservationAdd);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.getItemCount()>=3){
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Sie haben bereits 3 Gadgets reserviert", Snackbar.LENGTH_LONG)
                            .show();
                }else {
                    showGadgetsDialog();
                }
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.reservationsRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                getReservations();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return rootView;
    }

    private void getReservations(){
     
        LibraryService.getReservationsForCustomer(new Callback<List<Reservation>>() {
            @Override
            public void onCompletion(List<Reservation> reservations) {
                adapter = new ReservationsAdapter(reservations, itemSelectionCallback);
                refreshReservableGadgets();
                recyclerView.setAdapter(adapter);
                if (getUserVisibleHint() && adapter.getReservedGadgets().size() == 0) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Keine Reservation vorhanden", Snackbar.LENGTH_LONG)
                            .setAction("Add", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (adapter.getItemCount() >= 3) {
                                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Sie haben bereits 3 Gadgets reserviert", Snackbar.LENGTH_LONG)
                                                .show();
                                    } else {
                                        showGadgetsDialog();
                                    }
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onError(String message) {

            }
        });

        setGadgets();
    }

    private void setGadgets() {     
        LibraryService.getGadgets(new Callback<List<Gadget>>() {
            @Override
            public void onCompletion(List<Gadget> input) {
                allGadgets.clear();
                allGadgets.addAll(input);
            }

            @Override
            public void onError(String message) {

            }
        });

        setNotLoanedGadgets();
    }
    private void setNotLoanedGadgets(){
        
        LibraryService.getLoansForCustomer(new Callback<List<Loan>>() {
            @Override
            public void onCompletion(List<Loan> input) {
                reservableGadgets.clear();
                ArrayList<String> loanedGadgets = new ArrayList<String>();
                for (Loan loan : input) {
                    loanedGadgets.add(loan.getGadget().getName());
                }
                for (int i = 0; i < allGadgets.size(); i++) {
                    if (!loanedGadgets.contains(allGadgets.get(i).getName())) {
                        reservableGadgets.add(allGadgets.get(i));
                    }
                }
            }

            @Override
            public void onError(String message) {

            }
        });

    }

    private void showGadgetsDialog() {
        refreshReservableGadgets();
        final CharSequence[] gadget = getReservableGadgetNames(reservableGadgets);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Wähle ein Gadget").setSingleChoiceItems(gadget, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                itemToReserve = item;
            }
        });

        String positiveText = "Add";
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                refreshReservableGadgets();
                Gadget toReserve = reservableGadgets.get(itemToReserve);
                final String reservation = toReserve.getName() + " reserviert";
                LibraryService.reserveGadget(toReserve, new Callback<Boolean>() {
                    @Override
                    public void onCompletion(Boolean input) {
                        getReservations();
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            }
        });
        String negativeText = "Cancel";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {}
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void refreshReservableGadgets(){
        setGadgets();
        filterReservedGadgets();
    }
    private void filterReservedGadgets() {
        ArrayList<Gadget> NotReservedGadgets = new ArrayList<>();
        ArrayList<String> reservedGadgets = adapter.getReservedGadgets();
        for(int i = 0; i < reservableGadgets.size(); i++) {
            if(!reservedGadgets.contains(reservableGadgets.get(i).getName())) {
                NotReservedGadgets.add(reservableGadgets.get(i));
            }
        }
        this.reservableGadgets = NotReservedGadgets;
    }

    private CharSequence[] getReservableGadgetNames(List<Gadget> reservableGadgets){
        ArrayList<String> gadgetNameStrings = new ArrayList<>();
        for(Gadget gadget : reservableGadgets) {
            gadgetNameStrings.add(gadget.getName());
        }
        final CharSequence[] gadget = gadgetNameStrings.toArray(new CharSequence[gadgetNameStrings.size()]);
        return gadget;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if (!(activity instanceof ItemSelectionListener)) {
            throw new IllegalStateException("Activity must implement ItemSelectionListener");
        }
        itemSelectionCallback = (ItemSelectionListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        itemSelectionCallback = null;
    }

}
