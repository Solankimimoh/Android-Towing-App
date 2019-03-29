package com.example.towinapp;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TowingVehicleListPoliceAdapter extends RecyclerView.Adapter<TowingVehicleListPoliceAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<AddVehicleModel> addVehicleModelArrayList;
    private TowingVehiclePoliceItemClickListener towingVehiclePoliceItemClickListener;


    public TowingVehicleListPoliceAdapter(Context context, ArrayList<AddVehicleModel> addVehicleModelArrayList, TowingVehiclePoliceItemClickListener towingVehiclePoliceItemClickListener) {
        this.context = context;
        this.addVehicleModelArrayList = addVehicleModelArrayList;
        this.towingVehiclePoliceItemClickListener = towingVehiclePoliceItemClickListener;
    }

    public void updateList(ArrayList<AddVehicleModel> searchArrayList) {
        addVehicleModelArrayList = new ArrayList<>();
        addVehicleModelArrayList.addAll(searchArrayList);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AddVehicleModel addVehicleModel;
        private TextView vehicleNumber;
        private TextView date;
        private TextView time;
        private TextView towingArea;
        private TextView uniqueChallan;
        private TextView fineAmount;
        private TextView verifyStatus;
        private ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicleNumber = itemView.findViewById(R.id.row_layout_towing_vehicle_number);
            date = itemView.findViewById(R.id.row_layout_towing_vehicle_date);
            time = itemView.findViewById(R.id.row_layout_towing_vehicle_time);
            towingArea = itemView.findViewById(R.id.row_layout_towing_vehicle_towing_area);
            uniqueChallan = itemView.findViewById(R.id.row_layout_towing_vehicle_unique_challan);
            fineAmount = itemView.findViewById(R.id.row_layout_towing_vehicle_fine_amount);
            verifyStatus = itemView.findViewById(R.id.row_layout_towing_vehicle_verify_status);
            imageView = itemView.findViewById(R.id.row_layout_towing_vehicle_img);

            itemView.setOnClickListener(this);
        }

        public void setData(AddVehicleModel data) {
            this.addVehicleModel = data;
            vehicleNumber.setText(data.getVehicleNumber());
            date.setText(data.getDate());
            time.setText(data.getTime());
            towingArea.setText(data.getTowinArea());
            uniqueChallan.setText(data.getUniqueChallan());
            fineAmount.setText(data.getFineAmount());
            if (data.isVerifyVehicle()) {
                verifyStatus.setText("Reached the zone");
                verifyStatus.setTextColor(Color.GREEN);
            } else {
                verifyStatus.setText("On the way to zone");
                verifyStatus.setTextColor(Color.RED);
            }


            Glide.with(context)
                    .load(data.getPhotoUrl())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(imageView);

        }

        @Override
        public void onClick(View v) {
            if (towingVehiclePoliceItemClickListener != null) {
                towingVehiclePoliceItemClickListener.onTowingVehiclePoliceItemClick(addVehicleModel,v);
            }
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_towing_vehicle, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        AddVehicleModel addVehicleModel = addVehicleModelArrayList.get(i);
        myViewHolder.setData(addVehicleModel);
    }


    @Override
    public int getItemCount() {
        return addVehicleModelArrayList.size();
    }

}
