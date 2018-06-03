package com.example.intern12.easymeasurements;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.intern12.easymeasurements.data.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.jar.*;



public class MapFragment extends Fragment implements OnMapReadyCallback {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Intent intent;
    ArrayList<LatLng> listaPozitii = new ArrayList<>();
    ArrayList<Marker> listaMarker= new ArrayList<>();
    LocationManager locationManager;
    GoogleMap mGoogleMap;
    MapView mMapView;
    View rootView;
    int calcOptionSpinner =0;
    String key;
    GPSTracker gpsTracker; //foloseste clasa asta pentru a prelua informatii
    private Button mapClear;
    private Button undoBtn;
    private Button positionBtn;
    private boolean calcPressed;
    private int closePolylineIndex=0;
    private Button calcAreaBtn;
    Marker currentMarker;
    private Polyline polyline,polylineDistance;
    private LatLng zoomPoint;
    private double suprafataCalculata;
    private TextView showCurrentValueTV;

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }


    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            zoomPoint= new LatLng(getArguments().getDouble("startPointLat",0.0),getArguments().getDouble("startPointLng",0.0));
            key = getArguments().getString("key");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        undoBtn = (Button) rootView.findViewById(R.id.btn_undo);
        positionBtn= (Button) rootView.findViewById(R.id.btn_position);
        showCurrentValueTV = (TextView) rootView.findViewById(R.id.text_view_map_show_area);
        mapClear = (Button) rootView.findViewById(R.id.btn_map_clear);
        calcAreaBtn = (Button) rootView.findViewById(R.id.btn_calc);
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
        R.array.calc_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calcOptionSpinner = position;
                mapClear.callOnClick();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) rootView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate((null));
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mGoogleMap = googleMap;
        MapsInitializer.initialize(getContext());
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        positionBtn.setEnabled(true);
        if(this.zoomPoint!=null){
            final CameraPosition cameraPosition = new CameraPosition.Builder().target(this.zoomPoint).zoom(17).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                currentMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLng));
                listaMarker.add(currentMarker);

                if (listaPozitii.size()==0 || listaPozitii.get(listaPozitii.size()-1) != latLng) { ///make more complex check
                    listaPozitii.add(latLng);
                }
                    undoBtn.setEnabled(true);

            }
        });


        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               undoButtonBody();
            }
        });


        calcAreaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (calcOptionSpinner){//we add here more options
                    case 0 :
                        calcAreaBody();
                        break;
                    case 1 :
                        calcDistanceBody();
                        break;
                    default:calcAreaBody();
                }
            }
        }
        );


        mapClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMap.clear();
                mapClearBody();

            }
        });
    }

    private void calcAreaBody(){
        if(calcPressed){
            listaPozitii.remove(closePolylineIndex);
            polyline.remove();
            intent = new Intent(getActivity(), DetailActivity.class);
            if(key!=null){
                intent.putExtra("numeZona",getArguments().getString("numeZona"));
                intent.putExtra("numeCultura",getArguments().getString("numeCultura"));
                intent.putExtra("numeProrietar",getArguments().getString("numeProrietar"));
            }
            intent.putExtra("suprafata",suprafataCalculata);
            intent.putExtra("startPointLat",listaPozitii.get(0).latitude);
            intent.putExtra("startPointLong",listaPozitii.get(0).longitude);
            intent.putExtra("key",key);
            startActivity(intent);
        }
        if(!calcPressed){
            if(listaPozitii.size()>2){
                listaPozitii.add(listaPozitii.get(0));
                closePolylineIndex=listaPozitii.size()-1;
                polyline= mGoogleMap.addPolyline(new PolylineOptions().addAll(listaPozitii).width(3).color(Color.BLUE).geodesic(true));
                suprafataCalculata= SphericalUtil.computeArea(listaPozitii)/10000;
                suprafataCalculata= Math.floor(suprafataCalculata * 100) / 100;
                calcPressed=true;
                showCurrentValueTV.setText(suprafataCalculata+" ha");
                calcAreaBtn.setText("Add area");
                undoBtn.setEnabled(true);
            }else {
                Toast.makeText(getContext(),"Add more than 2 points",Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void calcDistanceBody(){
        if (listaPozitii.size()>1) {
            Double distance = SphericalUtil.computeLength(listaPozitii);
            polylineDistance = mGoogleMap.addPolyline(new PolylineOptions().addAll(listaPozitii).width(3).color(Color.RED).geodesic(true));
            if (distance > 1000) {
                distance = Math.floor(distance / 10) / 100;
                calcPressed=true;
                showCurrentValueTV.setText(distance + " km");
            } else {

                distance = Math.floor(distance * 100) / 100;
                showCurrentValueTV.setText(distance + " m");
            }
        }else{
            Toast.makeText(getContext(),"Add more than 1 point",Toast.LENGTH_SHORT).show();
        }
    }
    private void undoButtonBody(){
        if(calcPressed){
            calcPressed=false;
            switch (calcOptionSpinner){//we add here more options
                case 0 :
                    polyline.remove();
                    listaPozitii.remove(closePolylineIndex);
                    break;
                case 1 :
                    polylineDistance.remove();
                    listaPozitii.remove(polylineDistance);
                    break;
                default:
            }

            calcAreaBtn.setText("Calc");
        }else {
            if(listaMarker.size()>=1) {
                listaPozitii.remove(listaPozitii.size() - 1);
                listaMarker.get(listaMarker.size() - 1).remove();
                listaMarker.remove(listaMarker.size() - 1);
                if(listaMarker.size()==0){
                    undoBtn.setEnabled(false);
                }
            }

        }
    }
    private void mapClearBody(){
        undoBtn.setEnabled(false);
        calcAreaBtn.setText("Calc");
        listaPozitii.clear();
        calcPressed=false;
        showCurrentValueTV.setText("");
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
