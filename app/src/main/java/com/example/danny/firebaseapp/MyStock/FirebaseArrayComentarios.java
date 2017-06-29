package com.example.danny.firebaseapp.MyStock;

import android.util.Log;

import com.example.danny.firebaseapp.usuarios.Comentarios;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Vspc-PoisonRiver on 16/04/2017.
 */

public class FirebaseArrayComentarios  implements ChildEventListener, ValueEventListener {



    public interface OnChangedListener {
        enum EventType {ADDED, CHANGED, REMOVED, MOVED}

        void onChildChanged(FirebaseArrayComentarios.OnChangedListener.EventType type, int index, int oldIndex);

        void onDataChanged();

        void onCancelled(DatabaseError databaseError);

    }
    private FirebaseArrayComentarios.OnChangedListener mListener;
    private Query mQuery;

    public List<DataSnapshot> mSnapshots = new ArrayList<>();
    public  String buscar = null;
    static boolean banderaBuscar = false;
    static  String pal;

    public FirebaseArrayComentarios(Query ref,String buscar) {
         this.buscar = buscar;
        mQuery = ref;
        mQuery.addChildEventListener(this);
        mQuery.addValueEventListener(this);

    }

    public void cleanup() {
        mQuery.removeEventListener((ValueEventListener) this);
        mQuery.removeEventListener((ChildEventListener) this);
    }

    public int getCount() {
        return mSnapshots.size();
    }

    public DataSnapshot getItem(int index) {
        return mSnapshots.get(index);
    }

    private int getIndexForKey(String key) {
        int index = 0;
        for (DataSnapshot snapshot : mSnapshots) {
            if (snapshot.getKey().equals(key)) {
                return index;
            } else {
                index++;
            }
        }
        throw new IllegalArgumentException("Key not found");
    }

    public static void set(String pala ,boolean ban){
        banderaBuscar = ban;
        pal  = pala;
    }

    private String getKeyPost(int item){

        return  getItem(item).getKey();
    }

    public void buscarPost(String busqueda){

        cleanup();

    }


    @Override
    public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {


        int index = 0;



            Log.d("pasando",snapshot.getKey());
            mSnapshots.add(mSnapshots.size(), snapshot);
            notifyChangedListeners(FirebaseArrayComentarios.OnChangedListener.EventType.ADDED, index);



    }

    @Override
    public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {



    }
    @Override
    public void onChildRemoved(DataSnapshot snapshot) {
        if(snapshot != null) {
            int index = getIndexForKey(snapshot.getKey());
            mSnapshots.remove(index);
            notifyChangedListeners(FirebaseArrayComentarios.OnChangedListener.EventType.REMOVED, index);
        }
    }

    @Override
    public void onChildMoved(DataSnapshot snapshot, String previousChildKey) {
        int oldIndex = getIndexForKey(snapshot.getKey());
        mSnapshots.remove(oldIndex);
        int newIndex = previousChildKey == null ? 0 : (getIndexForKey(previousChildKey) + 1);
        mSnapshots.add(newIndex, snapshot);
        notifyChangedListeners(FirebaseArrayComentarios.OnChangedListener.EventType.MOVED, newIndex, oldIndex);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(mListener!= null ) {
            mListener.onDataChanged();
        }
    }

    @Override
    public void onCancelled(DatabaseError error) {
        notifyCancelledListeners(error);
    }

    public void setOnChangedListener(FirebaseArrayComentarios.OnChangedListener listener) {
        mListener = listener;
    }

    protected void notifyChangedListeners(FirebaseArrayComentarios.OnChangedListener.EventType type, int index) {
        notifyChangedListeners(type, index, -1);
    }

    protected void notifyChangedListeners(FirebaseArrayComentarios.OnChangedListener.EventType type, int index, int oldIndex) {
        if (mListener != null) {
            mListener.onChildChanged(type, index, oldIndex);
        }
    }

    protected void notifyCancelledListeners(DatabaseError databaseError) {
        if (mListener != null) {
            mListener.onCancelled(databaseError);
        }
    }

}
