package com.example.towingapp;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ReceiveVehicleListAdminAdapter extends RecyclerView.Adapter<ReceiveVehicleListAdminAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<ReceiveVehicleDetailsModel> receiveVehicleDetailsModelArrayList;
    private ReceiveVehicleListAdminItemClickListener receiveVehicleListAdminItemClickListener;


    public ReceiveVehicleListAdminAdapter(Context context, ArrayList<ReceiveVehicleDetailsModel> receiveVehicleDetailsModelArrayList, ReceiveVehicleListAdminItemClickListener receiveVehicleListAdminItemClickListener) {
        this.context = context;
        this.receiveVehicleDetailsModelArrayList = receiveVehicleDetailsModelArrayList;
        this.receiveVehicleListAdminItemClickListener = receiveVehicleListAdminItemClickListener;
    }

    public void updateList(ArrayList<ReceiveVehicleDetailsModel> searchArrayList) {
        receiveVehicleDetailsModelArrayList = new ArrayList<>();
        receiveVehicleDetailsModelArrayList.addAll(searchArrayList);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ReceiveVehicleDetailsModel receiveVehicleDetailsModel;
        private TextView personName;
        private TextView email;
        private TextView mobile;
        private TextView address;
        private TextView extraAmount;
        private TextView reasonExtraAmount;
        private TextView totalAmount;
        private TextView date;
        private TextView vehicleNumber;
        private TextView towingArea;
        private TextView zonalOfficer;
        private ImageView documentImageView;
        private ImageView personImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            personName = itemView.findViewById(R.id.row_layout_receive_vehicle_name);
            email = itemView.findViewById(R.id.row_layout_receive_vehicle_email);
            mobile = itemView.findViewById(R.id.row_layout_receive_vehicle_mobile);
            address = itemView.findViewById(R.id.row_layout_receive_vehicle_address);
            extraAmount = itemView.findViewById(R.id.row_layout_receive_vehicle_extra_amount);
            reasonExtraAmount = itemView.findViewById(R.id.row_layout_receive_vehicle_extra_amount_reason);
            totalAmount = itemView.findViewById(R.id.row_layout_receive_vehicle_extra_amount_total_amount);
            date = itemView.findViewById(R.id.row_layout_receive_vehicle_extra_amount_total_date);
            vehicleNumber = itemView.findViewById(R.id.row_layout_receive_vehicle_number);
            towingArea = itemView.findViewById(R.id.row_layout_receive_vehicle_towin_area);
            zonalOfficer = itemView.findViewById(R.id.row_layout_receive_vehicle_zonal_officer);
            documentImageView = itemView.findViewById(R.id.row_layout_receive_vehicle_document);
            personImageView = itemView.findViewById(R.id.row_layout_receive_vehicle_person);

            itemView.setOnClickListener(this);
        }

        public void setData(ReceiveVehicleDetailsModel data) {
            this.receiveVehicleDetailsModel = data;
            personName.setText(data.getName());
            email.setText(data.getEmail());
            mobile.setText(data.getMobile());
            email.setText(data.getEmail());
            address.setText(data.getAddress());
            extraAmount.setText(data.getExtraAmount());
            reasonExtraAmount.setText(data.getReasonExatraAmount());
            totalAmount.setText(data.getTotalAmount());
            vehicleNumber.setText(data.getAddVehicleModel().getVehicleNumber());
            date.setText(data.getDate());
            towingArea.setText(data.getAddVehicleModel().getTowinArea());
            zonalOfficer.setText(data.getZonalOfficerModel().getName() + " | " + data.getZonalOfficerModel().getPoliceid());


            Log.e("HERO", personName.getText().toString());

            Glide.with(context)
                    .load(data.getDocumentPhotoUrl())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(documentImageView);

            Glide.with(context)
                    .load(data.getPersonPhotoUrl())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(personImageView);

        }

        @Override
        public void onClick(View v) {
            if (receiveVehicleListAdminItemClickListener != null) {
                receiveVehicleListAdminItemClickListener.onReceiveVehicleListAdminItemClick(receiveVehicleDetailsModel, v);
            }
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_receive_vehicle, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        ReceiveVehicleDetailsModel receiveVehicleDetailsModel = receiveVehicleDetailsModelArrayList.get(i);
        myViewHolder.setData(receiveVehicleDetailsModel);
        Log.e("HERO", "onBindViewHolder: "+receiveVehicleDetailsModel.getName() );
    }


    @Override
    public int getItemCount() {
        return receiveVehicleDetailsModelArrayList.size();
    }

}
