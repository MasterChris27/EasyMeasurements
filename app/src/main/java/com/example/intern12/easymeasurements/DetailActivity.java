package com.example.intern12.easymeasurements;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.intern12.easymeasurements.data.Masurari;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    ImageView imagine;
    TextView textViewName, textViewCultura, textViewSuprafata, textViewLastUpdate, textViewProprietar;
    EditText editTextName, editTextCultura, editTextSuprafata, editTextProprietar;
    Button btnReset, btnConfirm,btnDelete;
    String key;
    int pozitie;
    com.example.intern12.easymeasurements.data.LatLng startPoint;
    private AlertDialog alertDialogConfirm,alertDialogDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initializeContent();
        alertDialogSetup();
        alertDialogDeleteSetup();


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogConfirm.show();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogDelete.show();
            }
        }
        );

        textViewName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (textViewName.getText() != Integer.toString(R.string.text_no_info)) {   //Verifica daca stringul existent este diferit de cel default
                    editTextName.setText(textViewName.getText());
                }
                editTextName.setVisibility(View.VISIBLE);
                textViewName.setVisibility(View.GONE);
                return false;
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                onResetPressed();
            }
        });

        editTextName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    textViewName.setText(editTextName.getText());
                    editTextName.setVisibility(View.GONE);
                    textViewName.setVisibility(View.VISIBLE);
                    hideKeyboard(editTextName);
                    return true;
                }
                return false;
            }
        });


        imagine.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
                intent.putExtra("startPointLat",startPoint.getLatitude());
                intent.putExtra("startPointLng",startPoint.getLongitude());
                intent.putExtra("key", key);

                intent.putExtra("numeZona",textViewName.getText());
                intent.putExtra("numeCultura", textViewCultura.getText());
                intent.putExtra("numeProprietar", textViewProprietar.getText());
//                intent.putExtra("lastUpdate", masurari.getLastUpdated();
                startActivity(intent);
                return false;
                }
            }
        );

        textViewCultura.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!textViewCultura.getText().equals(Integer.toString(R.string.text_no_info))) {
                    editTextCultura.setText(textViewCultura.getText());
                }
                editTextCultura.setVisibility(View.VISIBLE);
                textViewCultura.setVisibility(View.GONE);
                return false;
            }
        });

        editTextCultura.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    textViewCultura.setText(editTextCultura.getText());
                    editTextCultura.setVisibility(View.GONE);
                    textViewCultura.setVisibility(View.VISIBLE);
                    hideKeyboard(editTextCultura);
                    return true;
                }
                return false;
            }
        });

        textViewSuprafata.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (textViewSuprafata.getText() != Integer.toString(R.string.text_no_info)) {
                    editTextSuprafata.setText(textViewSuprafata.getText());
                }
                editTextSuprafata.setVisibility(View.VISIBLE);
                textViewSuprafata.setVisibility(View.GONE);
                return false;
            }
        });

        editTextSuprafata.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    textViewSuprafata.setText(editTextSuprafata.getText());
                    editTextSuprafata.setVisibility(View.GONE);
                    textViewSuprafata.setVisibility(View.VISIBLE);
                    hideKeyboard(editTextSuprafata);
                    return true;
                }
                return false;
            }
        });


        textViewProprietar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (textViewProprietar.getText() != Integer.toString(R.string.text_no_info)) {
                    editTextProprietar.setText(textViewProprietar.getText());
                }
                editTextProprietar.setVisibility(View.VISIBLE);
                textViewProprietar.setVisibility(View.GONE);
                return false;
            }
        });

        editTextProprietar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    textViewProprietar.setText(editTextProprietar.getText());
                    editTextProprietar.setVisibility(View.GONE);
                    textViewProprietar.setVisibility(View.VISIBLE);

                    hideKeyboard(editTextProprietar);
                    return true;
                }


                return false;
            }
        });


    }

    public void initializeContent() {
        imagine = (ImageView) findViewById(R.id.detail_imgview);
        textViewName = (TextView) findViewById(R.id.text_view_detail_name);
        textViewCultura = (TextView) findViewById(R.id.text_view_detail_cultura);
        textViewSuprafata = (TextView) findViewById(R.id.text_view_detail_suprafata);
        textViewProprietar = (TextView) findViewById(R.id.text_view_detail_owner);
        //  textViewLastUpdate = (TextView) findViewById(R.id.text_view_detail_date);
        editTextName = (EditText) findViewById(R.id.edit_text_name);
        editTextCultura = (EditText) findViewById(R.id.edit_text_cultura);
        editTextSuprafata = (EditText) findViewById(R.id.edit_text_size);
        editTextProprietar = (EditText) findViewById(R.id.edit_text_owner);

        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnReset = (Button) findViewById(R.id.btn_reset);
        btnDelete= (Button) findViewById(R.id.btn_delete);

        key = getIntent().getStringExtra("key");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(key==null){
            key = mDatabase.push().getKey();
        }
        pozitie = getIntent().getIntExtra("pozitie", 0);
        startPoint= new com.example.intern12.easymeasurements.data.LatLng(getIntent().getDoubleExtra("startPointLat",4.0),getIntent().getDoubleExtra("startPointLng",0.0));

        textViewCultura.setText(getIntent().getStringExtra("numeCultura"));
        textViewName.setText(getIntent().getStringExtra("numeZona"));
        textViewProprietar.setText(getIntent().getStringExtra("numeProprietar"));
        textViewSuprafata.setText(getIntent().getDoubleExtra("suprafata", 0.29) + "");

    }

    public void hideKeyboard(EditText ET){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ET.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public void alertDialogSetup() {
        alertDialogConfirm = new AlertDialog.Builder(DetailActivity.this).create();
        alertDialogConfirm.setTitle(getString(R.string.alert_title));
        alertDialogConfirm.setMessage(getString(R.string.alert_message));
        alertDialogConfirm.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.alert_pozitiv), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                onChangeConfirm();
            }
        });
        alertDialogConfirm.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.alert_negativ), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialogConfirm.dismiss();
            }
        });
    }

    public void alertDialogDeleteSetup(){
        alertDialogDelete = new AlertDialog.Builder(DetailActivity.this).create();
        alertDialogDelete.setTitle(getString(R.string.alert_delete_title));
        alertDialogDelete.setMessage(getString(R.string.alert_delete_message));
        alertDialogDelete.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.alert_pozitiv), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDeleteConfirm();
            }
        });
        alertDialogDelete.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.alert_negativ), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialogDelete.dismiss();
            }
        });


    }

    public void onResetPressed() {
        textViewName.setText(getIntent().getStringExtra("numeZona"));
        textViewCultura.setText(getIntent().getStringExtra("numeCultura"));
        textViewProprietar.setText(getIntent().getStringExtra("numeProprietar"));
        textViewSuprafata.setText(getIntent().getDoubleExtra("suprafata", 0.29) + "");
    }

    public void onChangeConfirm() {

        mDatabase.child(key).setValue(new Masurari(key, textViewName.getText().toString(),Double.parseDouble( textViewSuprafata.getText().toString()),
                textViewCultura.getText().toString(),textViewProprietar.getText().toString(),startPoint.getLatitude(),startPoint.getLongitude()));
//
        Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
        intent.putExtra("cod",1);
        startActivity(intent);
    }
     public void onDeleteConfirm(){
         if(key!=null){
             mDatabase.child(key).removeValue();
             Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
             intent.putExtra("cod",1);
             startActivity(intent);
         }
     }


    public interface EditMeasureList {
        void changeObjectArrayList(int pozitie, Masurari m);
    }

}
