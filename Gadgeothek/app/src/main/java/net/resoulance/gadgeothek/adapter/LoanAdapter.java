package net.resoulance.gadgeothek.adapter;


import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.resoulance.gadgeothek.R;
import net.resoulance.gadgeothek.domain.Loan;
import net.resoulance.gadgeothek.service.ItemSelectionListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.ViewHolder>{
    private ArrayList<Loan> dataset;
    ItemSelectionListener selectionListener;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View itemRoot;
        private final TextView tvGadgetName;
        private final TextView tvDate;
        private final TextView tvDeadline;

        public ViewHolder(View itemRoot, TextView tvGadgetName, TextView tvDate, TextView tvDeadline) {
            super(itemRoot);
            this.itemRoot = itemRoot;
            this.tvGadgetName = tvGadgetName;
            this.tvDate = tvDate;
            this.tvDeadline = tvDeadline;
        }
    }

    public LoanAdapter(List<Loan> loansList, ItemSelectionListener selectionListener) {
        dataset = new ArrayList<>();
        for(int i = 0; i < loansList.size(); i++) {
            dataset.add(loansList.get(i));
        }
        this.selectionListener = selectionListener;
    }

    @Override
    public LoanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.rowlayout, parent, false);
        TextView tvGadgetName = (TextView)v.findViewById(R.id.tvRowGadgetName);
        tvGadgetName.setTextColor(Color.parseColor("#0D47A1"));
        TextView tvDate = (TextView)v.findViewById(R.id.tvRowDate);
        tvDate.setTextColor(Color.parseColor("#1976D2"));
        TextView tvDeadline = (TextView)v.findViewById(R.id.tvWaitingPosition);
        LoanAdapter.ViewHolder viewHolder = new LoanAdapter.ViewHolder(v, tvGadgetName, tvDate, tvDeadline);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //throw new AssertionError("not implemented");
        final Loan loan = dataset.get(position);
        holder.tvGadgetName.setText(loan.getGadget().getName());
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(loan.getPickupDate());
        calendar.add(Calendar.DATE, 7);
        holder.tvDate.setText(("Zurück bis: " + (loan.getPickupDate() != null ? df.format(calendar.getTime()) : "unbefristet")));
        if(!loan.isOverdue()) {
            holder.tvDeadline.setText("Fällig");
            holder.tvDeadline.setTextColor(Color.parseColor("#F44336"));
        } else {
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(loan.getPickupDate());
            calendar2.add(Calendar.DATE, 7);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(new Date());
            int diffInDays = (int)(Math.round(calendar2.getTime().getTime() - calendar3.getTime().getTime()) / (1000.0 * 60.0 * 60.0 * 24.0) + 0.5f);
            if(diffInDays <= 1) {
                holder.tvDeadline.setText("Heute");
            } else {
                holder.tvDeadline.setText("Noch " + diffInDays + " Tage");
            }
            holder.tvDeadline.setTextColor(Color.parseColor("#1976D2"));
        }

        View.OnClickListener loanEntry = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDetails(v, holder.getAdapterPosition());
            }
        };

        holder.tvDate.setOnClickListener(loanEntry);
        holder.tvGadgetName.setOnClickListener(loanEntry);
        holder.itemRoot.setOnClickListener(loanEntry);
    }

    @Override
    public int getItemCount() {
        //throw new AssertionError("not implemented");
        return dataset.size();
    }


    private void showDetails(View view, int pos) {

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataset.get(pos).getPickupDate());


        String loanID = "Ausleih-ID: " + dataset.get(pos).getLoanId();
        String manufacturer = "Hersteller: " + dataset.get(pos).getGadget().getManufacturer();
        String gadget = dataset.get(pos).getGadget().getName();
        String price = "Preis: " + dataset.get(pos).getGadget().getPrice() + " Fr.";
        String pickupDate = "Ausleihdatum: " + df.format(calendar.getTime());
        calendar.setTime(dataset.get(pos).getPickupDate());
        calendar.add(Calendar.DATE, 7);
        String returnDate = "Rückgabedatum: " + df.format(calendar.getTime());

        ArrayList<String> details = new ArrayList<>();
        details.add(loanID);
        details.add(manufacturer);
        details.add(price);
        details.add(pickupDate);
        details.add(returnDate);

        CharSequence[] items = details.toArray(new CharSequence[details.size()]);


        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(gadget);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });
        String closeText = "Schliessen";
        builder.setPositiveButton(closeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        //
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public ArrayList<String> getLoanedGadgets(){
        ArrayList<String> loanedGadgets = new ArrayList();
        for(Loan loan : dataset){
            loanedGadgets.add(loan.getGadget().getName());
        }
        return loanedGadgets;
    }

}