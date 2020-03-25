package com.example.android.walkmyandroid;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//Tambahkan file class baru dengan subclass AsyncTask dengan input location dan outputnya string
public class FetchAddressTask extends AsyncTask<Location, Void, String> {
    private Context mContext;
    private OnTaskCompleted mListener;
    //tambahkan konstruktor
    FetchAddressTask(Context context, OnTaskCompleted listener){
        mContext = context;
        mListener = listener;
    }

    @Override
    protected String doInBackground(Location... locations) {
        //tambahkan code untuk convert data latitude longitude menjadi alamat real dengan fitur Geocoder
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        Location location = locations[0];
        List<Address> address = null;
        String resultMessage = "";
        try {
            address = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1
            );
        }catch (IOException e){
            e.printStackTrace();
        }
        //tambahkan code untuk mengubah List<Address> yang berisi alamat real menjadi string yang mudah dibaca
        if (address == null || address.size() == 0){
            resultMessage = "No Address Found";
        }else{
            Address address1 = address.get(0);
            ArrayList<String> addressParts = new ArrayList<>();
            for(int i = 0; i<=address1.getMaxAddressLineIndex();i++){
                addressParts.add(address1.getAddressLine(i));
            }
            resultMessage = TextUtils.join("\n", addressParts);
        }
        return resultMessage;
    }

    @Override
    protected void onPostExecute(String s) {
        mListener.onTaskCompleted(s);   //untuk mengirimkan string alamat real
        super.onPostExecute(s);
    }

    //tambahkan interface OnTaskCompleted
    interface OnTaskCompleted{
        void onTaskCompleted(String result);
    }
}
