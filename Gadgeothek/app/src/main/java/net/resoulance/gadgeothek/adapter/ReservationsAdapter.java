package net.resoulance.gadgeothek.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.resoulance.gadgeothek.R;
import net.resoulance.gadgeothek.domain.Reservation;
import net.resoulance.gadgeothek.service.Callback;
import net.resoulance.gadgeothek.service.ItemSelectionListener;
import net.resoulance.gadgeothek.service.LibraryService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ViewHolder> {

    private ArrayList<Reservation> dataset;
    ItemSelectionListener selectionListener;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View itemRoot;
        private final TextView textViewGadgetName;
        private final TextView textViewDate;
        private final TextView textWaitingPosition;

        public ViewHolder(View itemRoot, TextView textViewGadgetName, TextView textViewDate, TextView textWaitingPosition) {
            super(itemRoot);
            this.itemRoot = itemRoot;
            this.textViewGadgetName = textViewGadgetName;
            this.textViewDate = textViewDate;
            this.textWaitingPosition = textWaitingPosition;
        }
    }


    public ReservationsAdapter(List<Reservation> reservationList, ItemSelectionListener selectionListener) {
        dataset = new ArrayList<>();
        for(int i = 0; i < reservationList.size(); i++) {
            dataset.add(reservationList.get(i));
        }
        this.selectionListener = selectionListener;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.rowlayout, parent, false);
        TextView textViewGadgetName = (TextView)v.findViewById(R.id.tvRowGadgetName);
        textViewGadgetName.setTextColor(Color.parseColor("#0D47A1"));
        TextView textViewDate = (TextView)v.findViewById(R.id.tvRowDate);
        textViewDate.setTextColor(Color.parseColor("#1976D2"));
        TextView textWaitingPosition = (TextView)v.findViewById(R.id.tvWaitingPosition);
        ViewHolder viewHolder = new ViewHolder(v, textViewGadgetName, textViewDate, textWaitingPosition);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Reservation reservation = dataset.get(position);
        holder.textViewGadgetName.setText(reservation.getGadget().getName());
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reservation.getReservationDate());
        holder.textViewDate.setText(("Reserviert am: " + df.format(calendar.getTime())));
        if(reservation.getWatingPosition() == 0) {
            holder.textWaitingPosition.setText("Abholbereit");
            holder.textWaitingPosition.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            holder.textWaitingPosition.setText("Warteposition: " + reservation.getWatingPosition());
            holder.textWaitingPosition.setTextColor(Color.parseColor("#1976D2"));
        }
    }

    public void deleteEntry(int position) {
        Reservation toDelete = dataset.get(position);
        dataset.remove(position);
        LibraryService.deleteReservation(toDelete, new Callback<Boolean>() {
            @Override
            public void onCompletion(Boolean input) {
                ReservationsAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public ArrayList<String> getReservedGadgets(){
        ArrayList<String> reservedGadgets = new ArrayList();
        for(Reservation reservation : dataset){
            reservedGadgets.add(reservation.getGadget().getName());
        }
        return reservedGadgets;
    }
}