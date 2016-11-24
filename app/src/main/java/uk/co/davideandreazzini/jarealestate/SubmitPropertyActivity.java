package uk.co.davideandreazzini.jarealestate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Helpers.FilePath;
import Helpers.ImgUtils;
import Objects.Property;

public class SubmitPropertyActivity extends DrawerActivity {

    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String selectedFilePath;
    // url that handles the file upload
    private String SERVER_URL = "https://www.geekmesh.com/upload.php";

    ImageView ivAttachment;
    TextView tvFileName;
    ProgressDialog dialog;
    String mainImageSrc = "";
    String propertyType, propertyFrequency;
    EditText title ;
    EditText summary;
    EditText address;
    EditText bedrooms;
    EditText price;
    TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.content_property_sell);

        tvFileName = (TextView) findViewById(R.id.fileNameTxt);
        ivAttachment = (ImageView) findViewById(R.id.fileImage);
        ivAttachment.setOnClickListener(e->onClickImageUpload(ivAttachment));
        errorText = (TextView) findViewById(R.id.errorText);
        title = (EditText) findViewById(R.id.propertyTitle);
        summary = (EditText) findViewById(R.id.propertySummary);
        address = (EditText) findViewById(R.id.propertyAddress);
        bedrooms = (EditText) findViewById(R.id.PropertyBedrooms);
        price = (EditText) findViewById(R.id.propertyPrice);

        Button btnSave = (Button) findViewById(R.id.btnSave);
        Spinner frequencySpinner = (Spinner) findViewById(R.id.frequencySpinner);
        Spinner typeSpinner = (Spinner) findViewById(R.id.typeSpinner);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,R.array.propertyType, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                propertyType = adapterView.getItemAtPosition(i).toString();
                frequencySpinner.setVisibility((propertyType.equals("BUY") ? View.GONE: View.VISIBLE));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                propertyType = adapterView.getItemAtPosition(0).toString();
            }
        });


        ArrayAdapter<CharSequence> frequencyAdapter = ArrayAdapter.createFromResource(this,R.array.paymentFrequency, android.R.layout.simple_spinner_item);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(frequencyAdapter);
        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                propertyFrequency = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                propertyFrequency = adapterView.getItemAtPosition(0).toString();
            }
        });
        frequencySpinner.setVisibility(View.GONE);



        // save a property in the database
        btnSave.setOnClickListener(e->{

            if(!checkFields()) return;
            Property prop = new Property();
            prop.displayAddress = address.getText().toString();
            prop.summary = summary.getText().toString();
            prop.amount= Long.parseLong(price.getText().toString());
            prop.bedrooms = Integer.parseInt(bedrooms.getText().toString());
            prop.propertyTypeFullDescription = title.getText().toString();
            prop.mainImageSrc = mainImageSrc;
            prop.type = propertyType;
            prop.frequency = propertyFrequency;

            DatabaseReference mRef= db.mDb.getReference(propertyType);
            // create a new record in the db and get the id
            String key = mRef.push().getKey();
            // create an hashMap from the Property Object
            Map<String, Object> propValues = prop.toMap();
            mRef.child(key).setValue(propValues);
            // Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            // Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Property successfully sent")
                    .setTitle("Submit properties");

            // Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();

        });


    }

    private boolean checkFields(){
        // check required fields using a map
        HashMap<String, EditText>  map = new HashMap();
        map.put("title", title);
        map.put("summary", summary);
        map.put("address", address);
        map.put("price", price);
        map.put("bedrooms", bedrooms);
        String errorMsg;
        // iterate over the map, if the field is equal ""
        // show the error in the error message
        Iterator<String> keySetIterator = map.keySet().iterator();
        while(keySetIterator.hasNext()){
            String key = keySetIterator.next();
            if(map.get(key).getText().toString().equals("")){
                errorText.setVisibility(View.VISIBLE);
                errorMsg = "The field " + key + " is mandatory";
                errorText.setText(errorMsg);
                Toast.makeText(SubmitPropertyActivity.this,errorMsg,Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }


    public void onClickImageUpload(View v) {
        if(v== ivAttachment){
            //on attachment icon click
            showFileChooser();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PICK_FILE_REQUEST){
                if(data == null){
                    //no data present
                    return;
                }
                Uri selectedFileUri = data.getData();
                // FilePath.getPath is a static method and
                // return the file path of Gallery image/ Document / Video / Audio
                selectedFilePath = FilePath.getPath(this,selectedFileUri);
                if(selectedFilePath != null && !selectedFilePath.equals("")){
                    tvFileName.setText(selectedFilePath);
                    //on upload button Click
                    if(selectedFilePath != null){
                        dialog = ProgressDialog.show(SubmitPropertyActivity.this,"","Uploading File...",true);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //creating new thread to handle Http Operations
                                uploadFile(selectedFilePath);
                            }
                        }).start();
                    }else{
                        Toast.makeText(SubmitPropertyActivity.this,"Please choose a File First",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,"Cannot upload file to server",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // upload file to server
    public int uploadFile(final String selectedFilePath){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                }
            });
            return 0;
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file",selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainImageSrc = "https://geekmesh.com/uploads/" + fileName;
                            Bitmap img = ImgUtils.loadBitmap(mainImageSrc);
                            ivAttachment.setImageBitmap(img);
                            tvFileName.setText("File Upload completed.");
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();



            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SubmitPropertyActivity.this,"File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(SubmitPropertyActivity.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(SubmitPropertyActivity.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            return serverResponseCode;
        }

    }
}
