package com.example.danny.firebaseapp.data;

import android.content.Context;
import android.os.Environment;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Repositorio de push notifications
 */
public final class PushNotificationsRepository {
    private ObjectOutputStream writer;
    private FileOutputStream flujo;
    private ObjectInputStream reader;
    private FileInputStream  focus;
    private final String ARCHIVO = "notificaciones.obt";
    private File file;
    private static Context contex;
    private static ArrayMap<String, PushNotification> LOCAL_PUSH_NOTIFICATIONS = new ArrayMap<>();
    private static PushNotificationsRepository INSTANCE;

    private PushNotificationsRepository() {





        Log.d("si"," :"+ LOCAL_PUSH_NOTIFICATIONS.size());

    }

    public static PushNotificationsRepository getInstance(Context context) {

        contex = context;
        if (INSTANCE == null) {
            return new PushNotificationsRepository();
        } else {

            return INSTANCE;
        }
    }

    public void getPushNotifications(LoadCallback callback) {
        creaArchivo();
        callback.onLoaded(new ArrayList<>(LOCAL_PUSH_NOTIFICATIONS.values()));
    }

    public void savePushNotification(PushNotification notification) {
        LOCAL_PUSH_NOTIFICATIONS.put(notification.getId(), notification);
            creaArchivo();

    }

    public interface LoadCallback {
        void onLoaded(ArrayList<PushNotification> notifications);
    }

    public void creaArchivo(){


                   Iterator<PushNotification> iterator = LOCAL_PUSH_NOTIFICATIONS.values().iterator();
        file = new File(contex.getFilesDir(),ARCHIVO);
        if(!file.exists()) {
            try {



                   file.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

            try {
                flujo = new FileOutputStream(file);
                writer = new ObjectOutputStream(flujo);

                while (iterator.hasNext()) {
                    PushNotification c = iterator.next();

                    Log.d("grabando", "gtabando"+c.getId()+" "+ LOCAL_PUSH_NOTIFICATIONS.size()+" "+file.getFreeSpace());
                    writer.writeObject(c);

                }

                writer.close();

                 cargarMapa();

            } catch (IOException e) {
                e.printStackTrace();
            }


    }

    public boolean cargarMapa(){
            file = new File(contex.getFilesDir(),ARCHIVO);
        Log.d("mapa","cargando ");

       if(file.exists()){
           try {

               focus = new FileInputStream(file);
               reader = new ObjectInputStream(focus);
               PushNotification test = (PushNotification) reader.readObject();

               while (test!=null){
                   LOCAL_PUSH_NOTIFICATIONS.put(test.getId(),test);
                   test = (PushNotification) reader.readObject();
                   Log.d("leer","leyendo "+ LOCAL_PUSH_NOTIFICATIONS.size());
                   }

           reader.close();


           } catch (EOFException eof) {
               try{
                   reader.close();
           }catch(IOException x){

                                }
               Log.d("leer","leyendo final del archivo :"+eof.getMessage());

           }catch (IOException |ClassNotFoundException e){


                 e.printStackTrace();
               return true;
           }

          return true;
       }else {
            return false;

       }




    }

}
