package com.fingarpay;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fingarpay.helper.UtilityHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsAddressActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    String addressText;
    private static int requestCode;
    private static boolean hasLocationPermit;
    private static String imei = "";
    SharedPreferences sharedPreferences;
    EditText txtCurrentAddress;
    Button bttnContinue;
    // flag for GPS status
    public boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute

    // Declaring a Location Manager
    // protected LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_address);
        txtCurrentAddress=(EditText)findViewById(R.id.txtCurrentAddress) ;
        bttnContinue=(Button)findViewById(R.id.bttnContinue) ;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bttnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent();
                myIntent.setClass(getApplicationContext(),SetUpFingerScan.class);
                startActivity(myIntent);
            }
        });
        //checkLocationPermission();
        // getLocation();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        checkLocationPermission();
       location= getLocation();
       if (location !=null){

           DisplayAddress();
       }

        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }
    /*void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }*/

    public Location getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext()
                    .getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            Log.v("isGPSEnabled", "=" + isGPSEnabled);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Log.v("isNetworkEnabled", "=" + isNetworkEnabled);
            location = null;
            if (isGPSEnabled == false && isNetworkEnabled == false) {
                // no network provider is enabled
                showSettingsAlert();
                //openPlacesDialog();
            } else {
                this.canGetLocation = true;
                checkLocationPermission();

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {

                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }

                if (isNetworkEnabled) {
                   // location = null;

                   /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return TODO;
                    }*/
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    private void checkLocationPermission(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
        {
            requestCode=1;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},requestCode);

            return;
        }else{


            mMap.setMyLocationEnabled(true);
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hasLocationPermit=true;
                checkLocationPermission();
            } else {
                // Permission was denied. Display an error message.
            }
            requestReadPhoneStatePermission();
        }else if(requestCode==2){
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.READ_PHONE_STATE &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //hasLocationPermit=true;
                requestReadPhoneStatePermission();
            } else {
                // Permission was denied. Display an error message.
            }
            checkLocationPermission();
        }
    }
    private void requestReadPhoneStatePermission(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestCode=2;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},requestCode);

            return;
        }else{
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= 26) {
                imei=telephonyManager.getImei();
            }
            else
            {
                imei=telephonyManager.getDeviceId();
            }
            sharedPreferences.edit()
                    .putString("DeviceId",imei).apply();
        }

    }

    private void DisplayAddress(){
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        double latitude=location.getLatitude();
        double longitude=location.getLongitude();
        List<Address> addresses = null;

        try {
            while (addresses==null){
                addresses = geocoder.getFromLocation(latitude,
                        longitude,1);
            }

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                addressText= address.getAddressLine(0);// + ", " + address.
                //getLocality() + ", " + address.getCountryName();
//                                        addressText = String.format("%s, %s, %s",
//                                                address.getMaxAddressLineIndex() > 0 ? address.
//                                                        getAddressLine(0) : "", address.
//                                                        getLocality(), address.getCountryName());
                //txtFrom.setText(addressText);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(currentLoc).title(addressText));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        Toast.makeText(this, addressText, Toast.LENGTH_SHORT).show();
        txtCurrentAddress.setText(addressText);

    }

   /* @SuppressLint("StaticFieldLeak")
    private  void FindAddress(Geocoder geocoder,Location location){

        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if (UtilityHelper.hasNetwork(this)) {

                final ProgressDialog dialog = new ProgressDialog(this);

                new FindMobileTask() {
                    @Override
                    protected void onPreExecute() {
                        //dialog = new ProgressDialog(DataPreparationActivity.this);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setMessage("Finding Your Location");
                        dialog.show();
                    }
                    @Override
                    protected void onPostExecute(String result) {

                        dialog.dismiss();
                        if (result !=null) {

                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(result.getLatitude())
                                    , Double.valueOf(result.getLongitude()))).title(result.getLocationAddress()));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.valueOf(result.getLatitude())
                                    , Double.valueOf(result.getLongitude()))));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                        }else {

                            builder.setTitle("Mobile Locator!")
                                    .setMessage("Mobile No was not Found. Do you want " +
                                            "to Send Link to the Mobile to Download App?!")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            ShareIt("Mobile Number Tracker","Get the Location of any " +
                                                    "Phone using the Mobile Number. Download mobiletracker :"
                                                    + " https://play.google.com/store/apps/details?id=com.pnt.mobiletracker");
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User cancelled the dialog
                                        }
                                    });
                            // Create the AlertDialog object and return it
                            builder.create();
                            builder.show();
                        }
                        dialog.hide();
                        dialog.dismiss();
                    }
                }.execute(geocoder, location);
            }else{
                builder.setTitle("Mobile Locator!")
                        .setMessage("Please ensure you have a valid internet connection.")
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();

                 *//* AlertMessageBox.Show(this ,
                        "No Network Connection",
                        "Please ensure you have a valid internet connection.",
                        AlertMessageBox.AlertMessageBoxIcon.Error);
*//*
            }
        } catch (Exception e) {
            Log.e("", e.getMessage() + e.getStackTrace());
        }
    }

    private static class FindMobileTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... args){

            try {
                while (addresses==null){
                    addresses = geocoder.getFromLocation(latitude,
                            longitude,1);
                }

                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    addressText= address.getAddressLine(0);// + ", " + address.
                    //getLocality() + ", " + address.getCountryName();
//                                        addressText = String.format("%s, %s, %s",
//                                                address.getMaxAddressLineIndex() > 0 ? address.
//                                                        getAddressLine(0) : "", address.
//                                                        getLocality(), address.getCountryName());
                    //txtFrom.setText(addressText);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

*/

    @Override
    public void onLocationChanged(Location location) {
       // locationText.setText("Current Location: " + location.getLatitude() + ", " + location.getLongitude());

        LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
       // Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            //DisplayAddress();
        }

    public void showSettingsAlert() {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog
                    .setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            getApplicationContext().startActivity(intent);
                        }
                    });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            // Showing Alert Message
            alertDialog.create();
            alertDialog.show();
        }catch(Exception ex){
            Log.e("",ex.getMessage() + ex.getStackTrace());

        }
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
        showSettingsAlert();
    }
}
