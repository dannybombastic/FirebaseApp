package com.example.danny.firebaseapp.Wall;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.danny.firebaseapp.FirebaseActivity;
import com.example.danny.firebaseapp.MyStock.FragmentStock;
import com.example.danny.firebaseapp.R;
import com.example.danny.firebaseapp.database.Db_Handler;
import com.example.danny.firebaseapp.login.LogFrag;
import com.example.danny.firebaseapp.post.PostUsers;
import com.example.danny.firebaseapp.usuarios.Comentarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Wall extends AppCompatActivity {

    EditText area;
    Button boton_enviar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

          area = (EditText) findViewById(R.id.area_texto_comentarios);
          boton_enviar = (Button)findViewById(R.id.boton_enviar);
        final ComentariosUsuarios coment = new ComentariosUsuarios();

         boton_enviar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Db_Handler.addComentario(area.getText().toString(),FirebaseActivity.USER_EMAIL,coment);

             }
         });

    }



}
