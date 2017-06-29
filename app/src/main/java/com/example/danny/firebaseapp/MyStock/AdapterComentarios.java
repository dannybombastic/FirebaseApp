package com.example.danny.firebaseapp.MyStock;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Vspc-PoisonRiver on 16/04/2017.
 */

public abstract class AdapterComentarios  <T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{


    private static final String TAG = "FirebaseRecyclerAdapter";

    public FirebaseArrayComentarios mSnapshots;
    private Class<T> mModelClass;
    protected Class<VH> mViewHolderClass;
    protected int mModelLayout;



    AdapterComentarios(Class<T> modelClass,
                 @LayoutRes int modelLayout,
                 Class<VH> viewHolderClass,
                 FirebaseArrayComentarios snapshots) {
        mModelClass = modelClass;
        mModelLayout = modelLayout;
        mViewHolderClass = viewHolderClass;
        mSnapshots = snapshots;


        mSnapshots.setOnChangedListener(new FirebaseArrayComentarios.OnChangedListener() {
            @Override
            public void onChildChanged(EventType type, int index, int oldIndex) {
                switch (type) {
                    case ADDED:
                        notifyItemInserted(index);
                        break;
                    case CHANGED:
                        notifyItemChanged(index);
                        break;
                    case REMOVED:
                        notifyItemRemoved(index);
                        break;
                    case MOVED:
                        notifyItemMoved(oldIndex, index);
                        break;
                    default:
                        throw new IllegalStateException("Incomplete case statement");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                AdapterComentarios.this.onCancelled(databaseError);
            }

            @Override
            public void onDataChanged() {
                AdapterComentarios.this.onDataChanged();
            }
        });
    }

    /**
     * @param modelClass      Firebase will marshall the data at a location into
     *                        an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list.
     *                        You will be responsible for populating an instance of the corresponding
     *                        view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location,
     *                        using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public AdapterComentarios(Class<T> modelClass,
                        int modelLayout,
                        Class<VH> viewHolderClass,
                        Query ref, FirebaseArrayComentarios arg) {
        this(modelClass, modelLayout, viewHolderClass,arg);
    }



    public void clear() {
        int size = this.mSnapshots.mSnapshots.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.mSnapshots.mSnapshots.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }
    public void cleanup() {
        mSnapshots.cleanup();
    }

    @Override
    public int getItemCount() {
        return mSnapshots.getCount();
    }

    public T getItem(int position) {
        return parseSnapshot(mSnapshots.getItem(position));
    }

    /**
     * This method parses the DataSnapshot into the requested type. You can override it in subclasses
     * to do custom parsing.
     *
     * @param snapshot the DataSnapshot to extract the model from
     * @return the model extracted from the DataSnapshot
     */
    protected T parseSnapshot(DataSnapshot snapshot) {
        return snapshot.getValue(mModelClass);
    }

    public DatabaseReference getRef(int position) {
        return mSnapshots.getItem(position).getRef();
    }

    @Override
    public long getItemId(int position) {
        // http://stackoverflow.com/questions/5100071/whats-the-purpose-of-item-ids-in-android-listview-adapter
        return mSnapshots.getItem(position).getKey().hashCode();
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        try {
            Constructor<VH> constructor = mViewHolderClass.getConstructor(View.class);
            return constructor.newInstance(view);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        T model = getItem(position);
        populateViewHolder(viewHolder, model, position);
    }

    @Override
    public int getItemViewType(int position) {
        return mModelLayout;
    }

    /**
     * This method will be triggered each time updates from the database have been completely processed.
     * So the first time this method is called, the initial data has been loaded - including the case
     * when no data at all is available. Each next time the method is called, a complete update (potentially
     * consisting of updates to multiple child items) has been completed.
     * <p>
     * You would typically override this method to hide a loading indicator (after the initial load) or
     * to complete a batch update to a UI element.
     */
    protected void onDataChanged() {
    }

    /**
     * This method will be triggered in the event that this listener either failed at the server,
     * or is removed as a result of the security and Firebase Database rules.
     *
     * @param error A description of the error that occurred
     */
    protected void onCancelled(DatabaseError error) {
        Log.w(TAG, error.toException());
    }

    /**
     * Each time the data at the given Firebase location changes,
     * this method will be called for each item that needs to be displayed.
     * The first two arguments correspond to the mLayout and mModelClass given to the constructor of
     * this class. The third argument is the item's position in the list.
     * <p>
     * Your implementation should populate the view using the data contained in the model.
     *
     * @param viewHolder The view to populate
     * @param model      The object containing the data used to populate the view
     * @param position   The position in the list of the view being populated
     */
    protected abstract void populateViewHolder(VH viewHolder, T model, int position);

}
