package uk.co.davideandreazzini.jarealestate;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import Models.Property;
import Observables.PropertyDetailObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PropertyActivity extends FirebaseActivity {

    Property mProp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        setToolbar();
        String propKey = getIntent().getStringExtra("property");
        String collection = getIntent().getStringExtra("collection");
        PropertyDetailObservable pdo = new PropertyDetailObservable(collection,propKey);
        rx.Observable.create(pdo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(property->{
                    Log.d("d", property.toString());
                    setPropertyUI(property);
                });

        Button btnPhone = (Button) findViewById(R.id.btnPhone);
        btnPhone.setOnClickListener(e->{
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:+447473778189"));
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                startActivity(callIntent);
            }

        });
        Button btnEmail = (Button) findViewById(R.id.btnEmail);
        btnEmail.setOnClickListener(e->{
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"info@geekmesh.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Info about:" + mProp.propertyTypeFullDescription);
            i.putExtra(Intent.EXTRA_TEXT   , "");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(uk.co.davideandreazzini.jarealestate.R.id.fab);
        fab.setOnClickListener(e->shareContent());
    }

    protected void setPropertyUI(Property prop){
        mProp = prop;
        TextView title = (TextView) findViewById(R.id.PropertyTitle);
        TextView summary = (TextView) findViewById(R.id.PropertySummary);
        TextView address = (TextView) findViewById(R.id.PropertyAddress);
        TextView bedrooms = (TextView) findViewById(R.id.PropertyBedrooms);
        TextView price = (TextView) findViewById(R.id.PropertyPrice);
        TextView type = (TextView) findViewById(R.id.PropertyType);
        ImageView img = (ImageView) findViewById(R.id.PropertyMainImg);

        img.setImageBitmap(prop.bmp);

        title.setText(prop.propertyTypeFullDescription);
        summary.setText( prop.summary);
        address.setText("Address: " + prop.displayAddress);
        price.setText("Price: " + prop.displayAmount);
        bedrooms.setText("N. of bedrooms:" + prop.bedrooms);
        type.setText("Type: "+ prop.type);

        setSharedContent(prop.propertyTypeFullDescription, prop.summary, prop.bmp);
    }


    private class sharingContent{
        String subj, text;
        Bitmap image;
    }

    sharingContent sContent;

    public void setSharedContent(String subj, String text, Bitmap image){
        sContent = new sharingContent();
        sContent.subj = subj;
        sContent.text = text;
        sContent.image = image;
    }


    public void shareContent(){
        if(sContent==null) return;
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {

            Intent i=new Intent(android.content.Intent.ACTION_SEND);
            i.setType("*/*");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT,sContent.subj);
            if(sContent.image!=null){

                String path = MediaStore.Images.Media.insertImage(getContentResolver(), sContent.image, "Image I want to share", null);
                Uri uri = Uri.parse(path);
                i.putExtra(android.content.Intent.EXTRA_STREAM, uri);
            }

            i.putExtra(android.content.Intent.EXTRA_TEXT, sContent.text + "\n\nSent with JA Real Estate Android App ");
            startActivity(Intent.createChooser(i,"Share via"));
        }

    }

}
