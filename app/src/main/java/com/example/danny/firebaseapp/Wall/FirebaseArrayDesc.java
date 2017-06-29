package com.example.danny.firebaseapp.Wall;

import com.example.danny.firebaseapp.post.PostForm;
import com.example.danny.firebaseapp.post.PostUsers;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Vspc-PoisonRiver on 20/02/2017.
 */

public class FirebaseArrayDesc implements ChildEventListener, ValueEventListener {

    public interface OnChangedListener {
        enum EventType {ADDED, CHANGED, REMOVED, MOVED}

        void onChildChanged(FirebaseArrayDesc.OnChangedListener.EventType type, int index, int oldIndex);

        void onDataChanged();

        void onCancelled(DatabaseError databaseError);

    }
    private FirebaseArrayDesc.OnChangedListener mListener;
    private Query mQuery;

    public List<DataSnapshot> mSnapshots = new ArrayList<>();
    public  String buscar ="";


    public FirebaseArrayDesc(Query ref,String buscar) {
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

    public int getIndexForKey(String key) {
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


    private String getKeyPost(int item){

        return  getItem(item).getKey();
    }

    public void buscarPost(String busqueda){

        cleanup();

    }


    @Override
    public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
        boolean bandera = false;

         int index = 0;
        PostUsers p = snapshot.getValue(PostUsers.class);
 if(p.getToFind()!=null) {
    String[] a = p.getToFind().split(" ");

    Iterator<String> i = Arrays.asList(a).iterator();
    String [] b = buscar.split(" ");




    while (i.hasNext()){
        String palabra = i.next();
        for (int j =0;j < b.length;j++){
            if(b[j].equalsIgnoreCase(palabra)){
                bandera =true;
            }
        }

    }
}


/*

         if(Arrays.deepEquals(a,b)){
             bandera = true;

         }
        for (String g: a) {

           if( g.equalsIgnoreCase(buscar)){
               bandera = true;

           }
           if(g.contains(buscar)){
               bandera = true;

           }

        }
*/

                 if(bandera) {


                     mSnapshots.add(mSnapshots.size(), snapshot);
                     notifyChangedListeners(FirebaseArrayDesc.OnChangedListener.EventType.ADDED, index);
                     bandera = false;
                 }

    }

    @Override
    public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {



    }
    @Override
    public void onChildRemoved(DataSnapshot snapshot) {
      if(snapshot !=null) {
          int index = getIndexForKey(snapshot.getKey());
          mSnapshots.remove(index);
          notifyChangedListeners(FirebaseArrayDesc.OnChangedListener.EventType.REMOVED, index);
      }
    }

    @Override
    public void onChildMoved(DataSnapshot snapshot, String previousChildKey) {
        int oldIndex = getIndexForKey(snapshot.getKey());
        mSnapshots.remove(oldIndex);
        int newIndex = previousChildKey == null ? 0 : (getIndexForKey(previousChildKey) + 1);
        mSnapshots.add(newIndex, snapshot);
        notifyChangedListeners(FirebaseArrayDesc.OnChangedListener.EventType.MOVED, newIndex, oldIndex);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
      if(mListener!=null){  mListener.onDataChanged();}
    }

    @Override
    public void onCancelled(DatabaseError error) {
        notifyCancelledListeners(error);
    }

    public void setOnChangedListener(FirebaseArrayDesc.OnChangedListener listener) {
        mListener = listener;
    }

    protected void notifyChangedListeners(FirebaseArrayDesc.OnChangedListener.EventType type, int index) {
        notifyChangedListeners(type, index, -1);
    }

    protected void notifyChangedListeners(FirebaseArrayDesc.OnChangedListener.EventType type, int index, int oldIndex) {
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
