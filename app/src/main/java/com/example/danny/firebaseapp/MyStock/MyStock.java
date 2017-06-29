package com.example.danny.firebaseapp.MyStock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.LightingColorFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.danny.firebaseapp.FindByLocation.BuscarByLocation;
import com.example.danny.firebaseapp.FirebaseActivity;
import com.example.danny.firebaseapp.R;
import com.example.danny.firebaseapp.Wall.AdapterPostBusc;
import com.example.danny.firebaseapp.Wall.ReciclerViewClikListener;
import com.example.danny.firebaseapp.Wall.Wall;
import com.example.danny.firebaseapp.camera.Camera;
import com.example.danny.firebaseapp.post.PostUsers;
import com.example.danny.firebaseapp.storage.StorageFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyStock extends AppCompatActivity implements FragmentStock.OnFragmentInteractionListener,FullScreenVendy.OnFragmentInteractionListener,FragmentBusqueda.OnFragmentInteractionListener,BuscarByLocation.OnFragmentInteractionListenerBy,FramentEditionStock.OnFragmentInteractionListener {


    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView iamgen;
        TextView desc;
        TextView price;
        TextView email;
        private Button fab;

        public PostViewHolder(View itemView) {
            super(itemView);
            desc = (TextView)itemView.findViewById(R.id.descCard);
            price = (TextView)itemView.findViewById(R.id.price_item);
            email = (TextView)itemView.findViewById(R.id.email_item);
            iamgen = (ImageView) itemView.findViewById(R.id.artImage);


        }


    }
    private  Uri contentUri;
    private String mParam1,p_one,p_two,p_three;
    int recurso;
    private  Uri photoURI;
    private   String mCurrentPhotoPath;
    private File photoFile;
    private static final int RC_TAKE_PICTURE = 101;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String KEY_DOWNLOAD_URL = "extra_download_url";
    private static final String KEY_FILE_URI = "key_file_uri";
    private Uri mDownloadUrl = null;
    private Uri mFileUri = null;
    private BroadcastReceiver mBroadcastReceiver;
    private   FramentEditionStock z ;
    BuscarByLocation bL;
    private  DatabaseReference data;
    private Query query;
    RecyclerView recycler;
    public FireBaseArrayStock fireBaseArrayStock;
    public  AdapterStock adapterStock;
    private RecyclerView.LayoutManager layoutManager;
    boolean borrar = false;
    boolean editar = false;
    String param;
    private FragmentStock m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stock);

        getSupportActionBar().setTitle("");
        recycler =(RecyclerView)findViewById(R.id.my_stock);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("postCarExpress");
        query = ref.child("pices");

        SharedPreferences sh = getSharedPreferences("fire", Context.MODE_PRIVATE);
     try {
         String user = "";
         if(sh.getBoolean("face",false)){
             user = FirebaseAuth.getInstance().getCurrentUser().getUid();

         }else{

             user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
         }
     }catch (NullPointerException e){
         e.printStackTrace();

     }


        fireBaseArrayStock = new FireBaseArrayStock(query, FirebaseActivity.USER_EMAIL);
        adapterStock = new AdapterStock<PostUsers, MyStock.PostViewHolder>(PostUsers.class, R.layout.item_post_cardview, MyStock.PostViewHolder.class, fireBaseArrayStock) {

            public void populateViewHolder(MyStock.PostViewHolder chatMessageViewHolder, PostUsers post, int position) {
                String s = post.getToFind();
                String pic = post.getPic_one();
                if(pic ==null){
                    pic = post.getPic_two();
                    if(pic==null){
                        pic = post.getPic_three();
                    }
                }
                Glide.with(getApplicationContext())
                        .load(pic)
                        .asBitmap()
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(VectorDrawableCompat.create(getResources(),R.drawable.vendyn,null))
                        .centerCrop()
                        .into(chatMessageViewHolder.iamgen);
                chatMessageViewHolder.price.setText(post.getPrice() + "€");
                chatMessageViewHolder.desc.setText(post.getYear());


                mSnapshots.getItem(chatMessageViewHolder.getAdapterPosition()).getKey();

            }
        };

        recycler.setHasFixedSize(true);


        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        layoutManager.offsetChildrenVertical(20);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapterStock);
        recycler.addOnItemTouchListener(new ReciclerViewClikListener(getParent(), recycler, new ReciclerViewClikListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                data = adapterStock.getRef(position);
                PostUsers post = (PostUsers) adapterStock.getItem(position);
                     if(borrar){
                         borrar =false;
                         recycler.removeView(view);
                         adapterStock.getRef(position).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {

                                 Snackbar.make(getCurrentFocus(),"Borrado con exito ",Snackbar.LENGTH_LONG).show();                     }
                         });


                     }


                if (editar) {
                    z = (FramentEditionStock)getSupportFragmentManager().findFragmentById(R.id.edition_fragment);
                    z = FramentEditionStock.newInstance("",post);

                    Toast.makeText(getApplicationContext(),post.getName(),Toast.LENGTH_SHORT).show();


                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.pop_enter,R.anim.pop_exit)
                            .replace(R.id.stock,z)
                            .addToBackStack("")
                            .commit();

                    editar = false;

                }



                // adapterPostBusc.clear();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        picBroadcast();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_borrar) {
          borrar = !borrar;
            if(borrar){
                Snackbar.make(getCurrentFocus(),"Pulse sobre el Articulo para borrar",Snackbar.LENGTH_LONG).show();
            }else {
                Snackbar.make(getCurrentFocus(), "Borrar cancelado", Snackbar.LENGTH_LONG).show();
            }}

        if (id == R.id.action_editar){
            editar =!editar;
            if(editar){
                Snackbar.make(getCurrentFocus(),"Pulse sobre el Articulo para Editar",Snackbar.LENGTH_LONG).show();
            }else {
                Snackbar.make(getCurrentFocus(), "Editar cancelado", Snackbar.LENGTH_LONG).show();
            }
        }

        if (id == R.id.buscar_stock) {

            if (bL == null) {
                bL = (BuscarByLocation) getSupportFragmentManager().findFragmentById(R.id.frag_busqueda);
                bL = BuscarByLocation.newInstance("", "");

                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.stock, bL)
                        .addToBackStack("")
                        .commit();

            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_stock, menu);
        return true;


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteractionby(String uri) {

         this.param =uri;
        FragmentBusqueda f = (FragmentBusqueda)getSupportFragmentManager().findFragmentById(R.id.frag_busqueda);

         f = FragmentBusqueda.newInstance(param,param);
        f.setAdapAndArray(this.fireBaseArrayStock,uri);
         getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fadein,R.anim.fadeout)
                .replace(R.id.stock,f)
                .addToBackStack("")
                .commit();
        bL = null;
    }





    private class TareaAsincrona extends AsyncTask<String,Void,Void>{


        @Override
        protected Void doInBackground(String... params) {
            return null;
        }
    }


    private void dialogoCamera(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MyStock.this);
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
                            dispatchTakePictureIntent();
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

    private void picBroadcast(){

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {

                Toast.makeText(getApplicationContext(), "broad cast:" + intent.getStringExtra(StorageFirebase.EXTRA_DOWNLOAD_URL) + intent.getStringExtra(StorageFirebase.EXTRA_FILE_URI), Toast.LENGTH_LONG).show();
                String pic =  intent.getStringExtra(StorageFirebase.EXTRA_DOWNLOAD_URL);


                if (recurso == R.id.imagen_uno_edition) {
                    p_one = intent.getStringExtra(StorageFirebase.EXTRA_DOWNLOAD_URL);
                     z.setPhoto_uno(p_one,1);
                     z.uno.setVisibility(ProgressBar.INVISIBLE);

                } else if(recurso == R.id.imagen_dos_edition){

                    p_two = intent.getStringExtra(StorageFirebase.EXTRA_DOWNLOAD_URL);
                   z.setPhoto_uno(p_two,2);
                    z.dos.setVisibility(ProgressBar.INVISIBLE);
                }else if(recurso == R.id.imagen_tres_edition){

                    p_three = intent.getStringExtra(StorageFirebase.EXTRA_DOWNLOAD_URL);
                  z.setPhoto_uno(p_three,3);
                    z.tres.setVisibility(ProgressBar.INVISIBLE);
                }

                switch (intent.getAction()) {


                    case StorageFirebase.UPLOAD_COMPLETED:
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


    private void launchCamera() {
        Log.d("camera-on", "launchCamera");

        // Pick an image from storage
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(intent, RC_TAKE_PICTURE);
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

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(MyStock.this,
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
        startActivityForResult(Camera.photoIntent(MyStock.this, photoFile), REQUEST_IMAGE_CAPTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                mFileUri = data.getData();
                Log.d("uri-fire",mFileUri.toString());

                if (mFileUri != null) {
                    uploadFromUri(mFileUri);
                    Toast.makeText(this,mFileUri.getPath(),Toast.LENGTH_LONG).show();
                } else {
                    Log.w("photo-fali", "File URI is null");
                }
            } else {

                Toast.makeText(this, "Taking picture failed.", Toast.LENGTH_SHORT).show();
            }
        }
        if(resultCode == RESULT_CANCELED){
            z.uno.setVisibility(ProgressBar.INVISIBLE);
            z.dos.setVisibility(ProgressBar.INVISIBLE);
            z.tres.setVisibility(ProgressBar.INVISIBLE);
        }


        if(requestCode == REQUEST_IMAGE_CAPTURE){
            if(resultCode== RESULT_OK){


                // galleryAddPic();
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M){
                    // Do something for lollipop and above versions

                    if(photoFile != null) {
                        uploadFromUri(Uri.fromFile(photoFile));
                    }
                } else{
                    // do something for phones running an SDK before lollipop

                    if(photoURI != null) {
                        galleryAddPic();
                        uploadFromUri(photoURI);
                    }
                }






            }

            if(resultCode == RESULT_CANCELED){
                z.uno.setVisibility(ProgressBar.INVISIBLE);
                z.dos.setVisibility(ProgressBar.INVISIBLE);
                z.tres.setVisibility(ProgressBar.INVISIBLE);
            }
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


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
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
    public void onFragmentInteraction(PostUsers user) {
         data.updateChildren(user.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {
                 Toast.makeText(getApplicationContext(),"Actualizado Correctamente",Toast.LENGTH_SHORT).show();

             }
         });
    }

    @Override
    public void onAccion(View v) {
        recurso = v.getId();
        switch (recurso){

            case R.id.imagen_uno_edition:
                z.uno.setVisibility(ProgressBar.VISIBLE);
                break;
            case R.id.imagen_dos_edition:
                z.dos.setVisibility(ProgressBar.VISIBLE);
                break;
            case R.id.imagen_tres_edition:
                z.tres.setVisibility(ProgressBar.VISIBLE);
                break;
        }
        dialogoCamera();
    }



}
