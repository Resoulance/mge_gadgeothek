package net.resoulance.gadgeothek.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.resoulance.gadgeothek.R;
import net.resoulance.gadgeothek.adapter.LoanAdapter;
import net.resoulance.gadgeothek.domain.Loan;
import net.resoulance.gadgeothek.service.Callback;
import net.resoulance.gadgeothek.service.ItemSelectionListener;
import net.resoulance.gadgeothek.service.LibraryService;

import java.util.List;


public class LoanListFragment extends Fragment {

    private ItemSelectionListener itemSelectionCallback = null;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private LoanAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_loan_list, container, false);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.loanRecyclerView);

        // Eine Optimierung, wenn sich die Displaygroesse der Liste nicht aendern wird.
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        getLoans();


        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.loansRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                getLoans();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return rootView;
    }

    public void getLoans() {
        // Hardcoded Login -> Sollte an dieser Stelle schon gemacht sein
        // Nur noch getLoansForCustomer() nötig dann
        LibraryService.setServerAddress("http://mge7.dev.ifs.hsr.ch/public");
        LibraryService.login("m@hsr.ch", "12345", new Callback<Boolean>() {
            @Override
            public void onCompletion(Boolean input) {
                LibraryService.getLoansForCustomer(new Callback<List<Loan>>() {
                    @Override
                    public void onCompletion(List<Loan> loans) {
                        adapter = new LoanAdapter(loans, itemSelectionCallback);
                        recyclerView.setAdapter(adapter);
                        if(getUserVisibleHint() && adapter.getLoanedGadgets().size() == 0){
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Keine Ausleihe vorhanden", Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            }

            @Override
            public void onError(String message) {

            }
        });

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