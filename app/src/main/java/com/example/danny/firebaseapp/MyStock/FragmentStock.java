package com.example.danny.firebaseapp.MyStock;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.danny.firebaseapp.FirebaseActivity;
import com.example.danny.firebaseapp.R;
import com.example.danny.firebaseapp.Wall.FirebaseArray;
import com.example.danny.firebaseapp.post.PostUsers;
import com.example.danny.firebaseapp.usuarios.Comentarios;
import com.example.danny.firebaseapp.usuarios.Users;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentStock.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentStock#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentStock extends Fragment {
    public static class ComentVioHolder extends RecyclerView.ViewHolder {
        ImageView iamgen;
        TextView comentario;
        TextView user;
        RatingBar ratingBar;


        public ComentVioHolder(View itemView) {
            super(itemView);
            comentario = (TextView)itemView.findViewById(R.id.comentario_item);
            user = (TextView)itemView.findViewById(R.id.userComentario);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBarComentarios);
            iamgen = (ImageView) itemView.findViewById(R.id.imagenComentario);


        }


    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static PostUsers user;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView precio,fecha,pieza,name,email,noWasap,localidad;
    boolean gas;
    private  String p_1,p_2,p_3;
    private ViewAnimator viewAnimator;
    private OnFragmentInteractionListener mListener;
    private ImageView p1,p2,p3;
    private RecyclerView recyclerViewComentarios;
    private ImageButton fullScreen;
    private static int resor;
    private Button dejarComent;
    private Query query;
    AdapterComentarios adapterComentarios;



    public FragmentStock() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentStock.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentStock newInstance(String param1, String param2, PostUsers users,int resource) {
        FragmentStock fragment = new FragmentStock();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        user = users;
        resor = resource;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_fragment_stock, container, false);
        p1 = new ImageView(getActivity());
        p2 = new ImageView(getActivity());
        p3 = new ImageView(getActivity());
        dejarComent = (Button)root.findViewById(R.id.dejarComentario);
        recyclerViewComentarios = (RecyclerView)root.findViewById(R.id.reciclerViewComentarios);
        fullScreen = (ImageButton)root.findViewById(R.id.full_screen);
        localidad =(TextView)root.findViewById(R.id.localidad);
        noWasap = (TextView)root.findViewById(R.id.telefono_nowasap);
        name = (TextView)root.findViewById(R.id.telefono_nombre);
        email = (TextView)root.findViewById(R.id.telefono_mail);
        precio =(TextView)root.findViewById(R.id.precio);
        fecha =(TextView)root.findViewById(R.id.fecha);
        pieza =(TextView)root.findViewById(R.id.pieza);
        viewAnimator =(ViewAnimator)root.findViewById(R.id.viewAnimator);


            new TareAsincrona().execute();




          localidad.setText(user.getLocalidad());
           precio.setText(user.getPrice()+"€");

           fecha.setText(user.getYear());
           pieza.setText(user.getDesc());
           name.setText(user.getName());

         if(user.getEmail().contains("@")){
            email.setText(user.getEmail());
         }else{
            email.setText("Email no Facilitado");
         }

           noWasap.setText(user.getTel());

          dejarComent.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  FragmentComentarios frComentarios = (FragmentComentarios)getFragmentManager().findFragmentById(R.id.freagmentComentarios);
                  frComentarios = FragmentComentarios.newInstance(user.getEmail(),"");
                  getFragmentManager()
                          .beginTransaction()
                          .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                          .add(resor, frComentarios)
                          .addToBackStack("")
                          .commit();
              }
          });
       fullScreen.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
            FullScreenVendy   m = (FullScreenVendy) getFragmentManager().findFragmentById(R.id.fragmen_stock);
               m = FullScreenVendy.newInstance("",user);

                            getFragmentManager()
                           .beginTransaction()
                           .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                           .add(resor, m)
                           .addToBackStack("")
                           .commit();
           }
       });

        p_1 = user.getPic_one();
        p_2 =  user.getPic_two();
        p_3 = user.getPic_three();


        if(p_1!=null){
            viewAnimator.addView(p1);
            Glide.with(this.getActivity())
                    .load(p_1)
                    .asBitmap()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(p1);}
        if(p_2!=null){
            viewAnimator.addView(p2);
            Glide.with(this.getActivity())
                    .load(p_2)
                    .asBitmap()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)

                    .into(p2);}
        if(p_3!=null){
            viewAnimator.addView(p3);
            Glide.with(this.getActivity())
                    .load(p_3)
                    .asBitmap()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(p3);}

        p1.setScaleType(ImageView.ScaleType.FIT_XY);
        p2.setScaleType(ImageView.ScaleType.FIT_XY);
        p3.setScaleType(ImageView.ScaleType.FIT_XY);
        viewAnimator.setInAnimation(getContext(),R.anim.fadein);
        viewAnimator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAnimator.showNext();
            }
        });

        noWasap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(noWasap.getText())) {
                    if(!TextUtils.isEmpty(user.getTel())) {
                        dialContactPhone(user.getTel());
                    }
                }
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(email.getText())) {
                    if(!TextUtils.isEmpty(user.getEmail())) {
                        setEmail("El Usuario "+FirebaseActivity.USER_NAME+" esta interesado en de sus articulos ", FirebaseActivity.USER_NAME,user.getEmail());
                    }
                }


            }
        });
/*
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {


                sC.smoothScrollTo(0,sC.getBottom());
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                viewAnimator.showNext();
                             sC.smoothScrollTo(0,0);
                            }
                        }, 2700);

                    }
                }, 2000);
            }
        }, 1500);
*/
        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
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



    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }



    public void setEmail(String subject,String body,String email ){

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT   , "User Ref :"+" Hola soy el usuario "+body+", ");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }


    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

   class TareAsincrona extends AsyncTask<String,Void,Void>{

       @Override
       protected void onPostExecute(Void aVoid) {


           RecyclerView.LayoutManager mLayoutManager;
           mLayoutManager = new LinearLayoutManager(getContext());


           recyclerViewComentarios.setLayoutManager(mLayoutManager);
           recyclerViewComentarios.setAdapter(adapterComentarios);
       }

       @Override
       protected Void doInBackground(String... params) {

           DatabaseReference ref = FirebaseDatabase.getInstance().getReference("pinkies");
           query = ref.child("comentarios").orderByChild("para").equalTo(user.getEmail());
           FirebaseArrayComentarios firebaseArrayComentarios = new FirebaseArrayComentarios(query,user.getEmail());
             adapterComentarios =  new AdapterComentarios<Comentarios,ComentVioHolder>(Comentarios.class,R.layout.item_comentarios,ComentVioHolder.class,firebaseArrayComentarios) {

               public void populateViewHolder(final FragmentStock.ComentVioHolder chatMessageViewHolder, Comentarios post, int position) {
                   try {
                       Glide.with(getActivity())
                               .load(post.getFoto())
                               .asBitmap()
                               .thumbnail(0.5f)
                               .diskCacheStrategy(DiskCacheStrategy.ALL)
                               .placeholder(R.mipmap.circle_logo)
                               .centerCrop()
                               .into(new BitmapImageViewTarget(chatMessageViewHolder.iamgen) {
                                   @Override
                                   protected void setResource(Bitmap resource) {
                                       RoundedBitmapDrawable circularBitmapDrawable =
                                               RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                                       circularBitmapDrawable.setCircular(true);
                                       chatMessageViewHolder.iamgen.setImageDrawable(circularBitmapDrawable);
                                   }
                               });
                       chatMessageViewHolder.user.setText(post.getUser());
                       chatMessageViewHolder.comentario.setText(post.getCometario());

                       chatMessageViewHolder.ratingBar.setRating(post.getEstrellas());


                       mSnapshots.getItem(chatMessageViewHolder.getAdapterPosition()).getKey();
                   }catch (NullPointerException e){

                   }

               }

           };

           return null;
       }
   }

}
