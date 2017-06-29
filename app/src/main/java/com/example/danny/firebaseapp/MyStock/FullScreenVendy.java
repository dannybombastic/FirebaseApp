package com.example.danny.firebaseapp.MyStock;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewAnimator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.danny.firebaseapp.R;
import com.example.danny.firebaseapp.post.PostUsers;
import com.example.danny.firebaseapp.usuarios.Users;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FullScreenVendy.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FullScreenVendy#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FullScreenVendy extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static PostUsers u;
    private ViewAnimator vA;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FullScreenVendy() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FullScreenVendy.
     */
    // TODO: Rename and change types and number of parameters
    public static FullScreenVendy newInstance(String param1, PostUsers param2) {
        FullScreenVendy fragment = new FullScreenVendy();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        u = param2;
        fragment.setArguments(args);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_full_screen_vendy, container, false);
        ImageView v1 = new ImageView(this.getContext());
        ImageView v2 = new ImageView(this.getContext());
        ImageView v3 = new ImageView(this.getContext());

        vA = (ViewAnimator)root.findViewById(R.id.full_view);
        if(u.getPic_one()!=null){
            vA.addView(v1);
            Glide.with(this.getActivity())
                    .load(u.getPic_one())
                    .asBitmap()

                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(v1);}
        if(u.getPic_two()!=null){
            vA.addView(v2);
            Glide.with(this.getActivity())
                    .load(u.getPic_two())

                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(v2);}
        if(u.getPic_three()!=null){
            vA.addView(v3);
            Glide.with(this.getActivity())
                    .load(u.getPic_three())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(v3);}

        v1.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        v2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        v3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        vA.setInAnimation(getContext(),R.anim.fadein);
        vA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vA.showNext();
            }
        });


        // Inflate the layout for this fragment
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
}
