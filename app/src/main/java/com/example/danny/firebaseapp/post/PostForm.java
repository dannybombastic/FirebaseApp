package com.example.danny.firebaseapp.post;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContentResolverCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.danny.firebaseapp.FirebaseActivity;
import com.example.danny.firebaseapp.R;
import com.example.danny.firebaseapp.camera.Camera;
import com.example.danny.firebaseapp.database.Db_Handler;
import com.example.danny.firebaseapp.storage.StorageFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.FileNotFoundException;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




import static android.R.attr.bitmap;
import static android.R.attr.dateTextAppearance;

public class PostForm extends AppCompatActivity implements PostInterarctor.PostCallBack,View.OnClickListener{


    // phtoto service requieres
    private static final int RC_TAKE_PICTURE = 101;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String KEY_DOWNLOAD_URL = "extra_download_url";
    private static final String KEY_FILE_URI = "key_file_uri";
    private Uri mDownloadUrl = null;
    private Uri mFileUri = null;
    private BroadcastReceiver mBroadcastReceiver;
    private boolean foto = false;
     Db_Handler db;

    // components fields

      private  String p_one,p_two,p_three,localidad;
      private  Location loc = new Location("espana");
      private  PostInterarctor interarctor;
      private  ImageView pic_one,pic_two,pic_three;
      private  EditText price,telefono,description,title;

      private  boolean bandera;
      private  boolean upload=  true;
      private  Button fab;
      private  SharedPreferences sharedPref;
      private  SharedPreferences.Editor editor;
      private   String mCurrentPhotoPath;
      private  File photoFile;
      private  Uri photoURI;
      private  Uri contentUri;
      private  int recurso;
      private ProgressBar pro_uno,pro_dos,pro_tres;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_wall);
        getSupportActionBar().setTitle("");

        sharedPref = getApplication().getSharedPreferences("fire",Context.MODE_PRIVATE);
        editor = sharedPref.edit();
           interarctor = new PostInterarctor(this.getApplicationContext(),db, FirebaseAuth.getInstance(), FirebaseDatabase.getInstance());

           new Adress2().execute("1");
            pro_uno = (ProgressBar)findViewById(R.id.progressBar2);
            pro_dos = (ProgressBar)findViewById(R.id.progressBar4);
            pro_tres = (ProgressBar)findViewById(R.id.progressBar5);
            pic_one = (ImageView) findViewById(R.id.imageView5);
            pic_two = (ImageView)findViewById(R.id.imageView4);
            pic_three =(ImageView)findViewById(R.id.imageView3);
            description = (EditText)findViewById(R.id.editText6);
            price = (EditText)findViewById(R.id.editText5);
            telefono = (EditText)findViewById(R.id.editText4);
            title = (EditText)findViewById(R.id.editText7);
                   fab = (Button)findViewById(R.id.floatingActionButton);
            fab.setOnClickListener(this);
        if (savedInstanceState != null) {
            mFileUri = savedInstanceState.getParcelable(KEY_FILE_URI);

        }

        if(sharedPref.getBoolean("tel",false)) {

            telefono.setText(sharedPref.getString("telefono","659..."));

        }

        if (savedInstanceState != null) {
            mFileUri = savedInstanceState.getParcelable(KEY_FILE_URI);
            mDownloadUrl = savedInstanceState.getParcelable(StorageFirebase.EXTRA_DOWNLOAD_URL);
            Db_Handler.PATH = savedInstanceState.getString("path");
        }




        pic_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recurso = v.getId();

             dialogoCamera();
                pro_uno.setVisibility(ProgressBar.VISIBLE);

            }
        });
        pic_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recurso = v.getId();
                dialogoCamera();
                pro_dos.setVisibility(ProgressBar.VISIBLE);
            }
        });
        pic_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recurso = v.getId();
                dialogoCamera();
                pro_tres.setVisibility(ProgressBar.VISIBLE);
            }
        });

       description.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
                description.setError(null);
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
      title.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {

          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
               title.setError(null);
          }

          @Override
          public void afterTextChanged(Editable s) {

          }
      });


                    price.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            price.setError(null);

                        }


                        @Override
                        public void afterTextChanged(Editable s) {


                        }
                    });
                    telefono.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                             telefono.setError(null);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

        telefono.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            boolean procesado = false;
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (!sharedPref.getBoolean("tel", false)) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(PostForm.this);
                        builder1.setMessage("Quiere registrar su telefono?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        editor.putString("telefono", telefono.getText().toString()).apply();
                                        editor.putBoolean("tel", true).apply();
                                        dialog.cancel();
                                    }
                                });

                        builder1.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();


                        alert11.getWindow().getDecorView().getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFFCFCFCF));
                        procesado = true;
                    }else if(sharedPref.getBoolean("tel", false) && !sharedPref.getString("telefono","659...").equalsIgnoreCase(telefono.getText().toString()) ){  AlertDialog.Builder builder1 = new AlertDialog.Builder(PostForm.this);
                        builder1.setMessage("Quiere registrar su telefono?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        editor.putString("telefono", telefono.getText().toString()).apply();
                                        editor.putBoolean("tel", true).apply();
                                        dialog.cancel();
                                    }
                                });

                        builder1.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();


                        alert11.getWindow().getDecorView().getBackground().setColorFilter(new LightingColorFilter(0xFFFFFF, 0xFFCFCFCF));
                        alert11.getWindow().getDecorView().setFitsSystemWindows(true);
                        procesado = true;}

                    }



                    return false;
                }

        });

          price.setOnEditorActionListener(new TextView.OnEditorActionListener() {

              boolean procesado = false;
              @Override
              public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                  if (actionId == EditorInfo.IME_ACTION_DONE) {
                      // Ocultar teclado virtual
                      InputMethodManager imm =
                              (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                      imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                      procesado = true;
                      }



                  return procesado;
              }
          });












        picBroadcast();
    }


private void dialogoCamera(){
    AlertDialog.Builder builder1 = new AlertDialog.Builder(PostForm.this);
    builder1.setMessage("Subir Foto");

    builder1.setCancelable(true);

    builder1.setPositiveButton(
            "Camara",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {


                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M){
                        // Do something for lollipop and above versions
                         dispatchPic();


                    } else{
                        // do something for phones running an SDK before lollipop
                        dispatchPic();
                    }
                    dialog.cancel();
                }
            });

    builder1.setNegativeButton(
            "Galeria",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    launchCamera();

                }
            });
    AlertDialog alert11 = builder1.create();

    alert11.show();

    alert11.getWindow().getDecorView().getBackground().setColorFilter(new LightingColorFilter(0xFFFFFF,0xFFCFCFCF));




}

private  void update(){

    StringBuilder toFind = new StringBuilder();
    String busq = toFind.append(price.getText().toString()+" "+localidad+" "+description.getText().toString()+" "+title.getText().toString()).toString().toLowerCase();

    busq = busq.replace("á","a");
    busq = busq.replace("é","e");
    busq = busq.replace("í","i");
    busq = busq.replace("ó","o");
    busq = busq.replace("ú","u");

    interarctor.postWall(price.getText().toString(),localidad,p_one,p_two,p_three,FirebaseActivity.USER_EMAIL,telefono.getText().toString(),bandera,description.getText().toString(),title.getText().toString(),busq,this);

     toFind= null;



}



    private void picBroadcast(){

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {

                Toast.makeText(getApplicationContext(), "broad cast:" + intent.getStringExtra(StorageFirebase.EXTRA_DOWNLOAD_URL) + intent.getStringExtra(StorageFirebase.EXTRA_FILE_URI), Toast.LENGTH_LONG).show();
                String pic =  intent.getStringExtra(StorageFirebase.EXTRA_DOWNLOAD_URL);


                if (recurso == R.id.imageView5) {
                          p_one = intent.getStringExtra(StorageFirebase.EXTRA_DOWNLOAD_URL);

                       Glide.with(getApplicationContext())
                            .load(pic)
                            .asBitmap()
                            .thumbnail(0.5f)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(VectorDrawableCompat.create(getResources(),R.drawable.vendynloading,null))
                            .centerCrop()
                            .into(pic_one);
                    pro_uno.setVisibility(View.INVISIBLE);

                } else if(recurso == R.id.imageView4){

                       p_two = intent.getStringExtra(StorageFirebase.EXTRA_DOWNLOAD_URL);
                    Glide.with(getApplicationContext())
                            .load(pic)
                            .asBitmap()
                            .thumbnail(0.5f)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(VectorDrawableCompat.create(getResources(),R.drawable.vendynloading,null))
                            .centerCrop()
                            .into(pic_two);
                    pro_dos.setVisibility(View.INVISIBLE);
                  }else if(recurso == R.id.imageView3){

                        p_three = intent.getStringExtra(StorageFirebase.EXTRA_DOWNLOAD_URL);
                    Glide.with(getApplicationContext())
                            .load(pic)
                            .asBitmap()
                            .thumbnail(0.5f)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(VectorDrawableCompat.create(getResources(),R.drawable.vendynloading,null))
                            .centerCrop()
                            .into(pic_three);
                    pro_tres.setVisibility(View.INVISIBLE);
                }

                switch (intent.getAction()) {


                    case StorageFirebase.UPLOAD_COMPLETED:
                        foto = true;
                    case StorageFirebase.UPLOAD_ERROR:
                        onUploadResultIntent(intent);
                        break;
                }

            }
        };



    }


    private void onUploadResultIntent(Intent intent) {
        // Got a new intent from MyUploadService with a success or failure
        mDownloadUrl = intent.getParcelableExtra(StorageFirebase.EXTRA_DOWNLOAD_URL);
       if(mFileUri!= null) {
           mFileUri = intent.getParcelableExtra(KEY_DOWNLOAD_URL);
       }

    }

    private void uploadFromUri(Uri fileUri) {
        Log.d("upload", "uploadFromUri:src:" + fileUri.toString());

        // Save the File URI
        mFileUri = fileUri;

        // Clear the last download, if any

        mDownloadUrl = null;

        // Toast message in case the user does not see the notificatio
        Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show();

        // Start MyUploadService to upload the file, so that the file is uploaded
        // even if this Activity is killed or put in the background
        startService(new Intent(this, StorageFirebase.class)

                .putExtra(StorageFirebase.EXTRA_FILE_URI, fileUri)
                .setAction(StorageFirebase.ACTION_UPLOAD));
    }

    private void uploadFromData(Intent data) {
        Log.d("upload", "uploadFromUri:src:" + data.toString());

        // Save the File URI
        mFileUri = data.getData();

        // Clear the last download, if any

        mDownloadUrl = null;

        // Toast message in case the user does not see the notificatio
        Toast.makeText(this, "Uploading...data", Toast.LENGTH_SHORT).show();

        // Start MyUploadService to upload the file, so that the file is uploaded
        // even if this Activity is killed or put in the background
        startService(new Intent(this, StorageFirebase.class)

                .putExtra(StorageFirebase.EXTRA_FILE_URI_DATA, data)
                .setAction(StorageFirebase.ACTION_UPLOAD_DATA));
    }




    private final int REQUEST_CODE_FROM_GALLERY = 01;
    private void launchCamera() {
        Log.d("camera-on", "launchCamera");
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RC_TAKE_PICTURE);

/*
        // Pick an image from storage
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(intent, RC_TAKE_PICTURE);*/
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = Camera.createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                 photoURI = FileProvider.getUriForFile(PostForm.this,
                        "com.example.android.fileprovider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void dispatchPic(){


        try {
            photoFile = Camera.createImageFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        startActivityForResult(Camera.photoIntent(this, photoFile), REQUEST_IMAGE_CAPTURE);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );
         storageDir.createNewFile();

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


     byte [] datas;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {

                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
                        // Do something for lollipop and above versions

                        if (photoFile != null) {


                            uploadFromUri(Uri.fromFile(photoFile));
                        }
                    } else {
                        // do something for phones running an SDK before lollipop

                        if (photoURI == null) {



                            uploadFromUri(Uri.fromFile(photoFile));


                        }
                    }



            } else {

                Toast.makeText(this, "Taking picture failed.", Toast.LENGTH_SHORT).show();
            }
        }
         if (resultCode == RESULT_CANCELED) {
             pro_uno.setVisibility(ProgressBar.INVISIBLE);
             pro_dos.setVisibility(ProgressBar.INVISIBLE);
             pro_tres.setVisibility(ProgressBar.INVISIBLE);
         }

         if(requestCode == RC_TAKE_PICTURE) {
             if (resultCode == RESULT_OK) {


                 mFileUri = data.getData();
                 Log.d("uri-fire", mFileUri.getPath());
                 if (mFileUri != null) {
                     uploadFromUri(data.getData());

                     Toast.makeText(this, "path real "+getRealPathFromURI(data.getData().toString()),Toast.LENGTH_SHORT).show();
                 } else {
                     Toast.makeText(this, "path real "+getRealPathFromURI(data.getData().toString()),Toast.LENGTH_SHORT).show();
                 }
                 // galleryAddPic();


             }
         }
            if (resultCode == RESULT_CANCELED) {
                pro_uno.setVisibility(ProgressBar.INVISIBLE);
                pro_dos.setVisibility(ProgressBar.INVISIBLE);
                pro_tres.setVisibility(ProgressBar.INVISIBLE);
            }


    }



    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
    public static Bitmap decodeUri(final ContentResolver resolver, final Uri uri) throws IOException {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = false;
        opt.inMutable = true;
        return BitmapFactory.decodeStream(resolver.openInputStream(uri), null, opt);
    }



    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(mBroadcastReceiver, StorageFirebase.getIntentFilter());


    }

    @Override
    protected void onStop() {
        super.onStop();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra(StorageFirebase.EXTRA_DOWNLOAD_URL)) {
            onUploadResultIntent(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle out) {
        out.putParcelable(KEY_FILE_URI, mFileUri);
        out.putParcelable(KEY_DOWNLOAD_URL, mDownloadUrl);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mFileUri = savedInstanceState.getParcelable(KEY_FILE_URI);
        mDownloadUrl = savedInstanceState.getParcelable(KEY_DOWNLOAD_URL);
       }

    @Override
    public void onNetworkConnectFailed() {
        Snackbar.make(getCurrentFocus(),"La red no está disponible. Conéctese y vuelva a intentarlo",Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBeUserResolvableError(int errorCode) {

    }

    @Override
    public void onGooglePlayServicesFailed() {
        Snackbar.make(getCurrentFocus(),"Se requiere Google Play Services para usar la app",Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onTitle(String msg) {
       this.title.setError(msg);

    }

    @Override
    public void onYearTelf(String msg) {
        telefono.setError(msg);
    }

    @Override
    public void onDescError(String msg) {
        this.description.setError(msg);
    }


    @Override
    public void onPiceError(String msg) {
        this.price.setError(msg);

    }

    @Override
    public void onSuccess() {
        Snackbar.make(getCurrentFocus(),"Articulo subido con exito",Snackbar.LENGTH_LONG).show();

     //  fab. setVisibility(FloatingActionButton.INVISIBLE);
        title.setText("");

        price.setText("");
        description.setText("");
        p_one = null;
        p_two = null;
        p_three = null;



        Glide.with(getApplicationContext())
                .load(android.R.drawable.ic_menu_camera)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(android.R.drawable.ic_menu_camera)
                .fitCenter()
                .into(pic_two);
        Glide.with(getApplicationContext())
                .load(android.R.drawable.ic_menu_camera)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(android.R.drawable.ic_menu_camera)
                .fitCenter()
                .into(pic_one);
        Glide.with(getApplicationContext())
                .load(android.R.drawable.ic_menu_camera)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(android.R.drawable.ic_menu_camera)
                .fitCenter()
                .into(pic_three);

    }



    @Override
    public void onClick(View v) {
      if(v.getId() == R.id.floatingActionButton){
              if(foto){
              update();
              }else {
                  Toast.makeText(getApplicationContext(),"Agregue al menos una foto ",Toast.LENGTH_LONG).show();
              }


     }

    }

     class Adress2 extends AsyncTask<String,Void,Void> {



        @Override
        protected  Void doInBackground(String... params) {
            if(params[0]=="1") {
                loc.setLatitude(FirebaseActivity.LATITUD);
                loc.setLongitude(FirebaseActivity.LONGITUD);
                loadAdress(loc);
            }else if(params[0]=="2"){

            }



            return null;
        }

         public void loadAdress(Location loc){
             String strAdd = "";
             Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
             try {
                 List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(),loc.getLongitude(), 1);
                 if (addresses != null) {
                     Address returnedAddress = addresses.get(0);
                     StringBuilder strReturnedAddress = new StringBuilder("");

                     for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                         strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
                     }
                     strAdd = strReturnedAddress.toString();
                     localidad = returnedAddress.getAddressLine(returnedAddress.getMaxAddressLineIndex()-1);
                     // telefono.setText(returnedAddress.getAddressLine(returnedAddress.getMaxAddressLineIndex()-1));
                     //   telefono.invalidate();
                     Log.w("My-Current ", "" + strReturnedAddress.toString());
                 } else {
                     Log.w("My-Current", "No Address returned!");
                 }
             } catch (Exception e) {
                 e.printStackTrace();
                 Log.w("My Current", "Canont get Address!");
             }
         }
    }



}
