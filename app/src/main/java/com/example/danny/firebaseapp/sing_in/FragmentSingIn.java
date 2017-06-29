package com.example.danny.firebaseapp.sing_in;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;


import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;

import com.example.danny.firebaseapp.FirebaseActivity;

import com.example.danny.firebaseapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by danny on 24/12/2016.
 */

public class FragmentSingIn extends Fragment implements SingInContract.View {

    public  FirebaseAuth.AuthStateListener mAuthListener;
    private SingInContract.Presenter mPresenter;
    private CallbackSin mCallback;
    private EditText mName,mEmail,mPassword;
    private ProgressBar loadin;
    private Button singButton;
    private FirebaseAuth mFirebaseAuth;
    private static final int RC_GET_TOKEN = 9002;

    public static FragmentSingIn newInstance() {
       FragmentSingIn fragment = new FragmentSingIn();
        // Setup de Argumentos
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        this.getFragmentManager().popBackStack();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() !=null){


        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sing_in_fragmento,container,false);

        singButton = (Button)root.findViewById(R.id.sin_ing);
        mEmail = (EditText) root.findViewById(R.id.edt_mail);
        mName = (EditText)root.findViewById(R.id.edt_name);
        mPassword = (EditText)root.findViewById(R.id.edt_password);
        loadin =(ProgressBar)root.findViewById(R.id.loadin);



        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEmail.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean procesado = false;

                if (i == EditorInfo.IME_ACTION_DONE) {



                    // Ocultar teclado virtual
                    InputMethodManager imm =
                            (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    attemptLogin();
                    procesado = true;
                }
                return procesado;
            }
        });

        singButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attemptLogin();
            }
        });


        return root;
    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
          switch (requestCode) {
            case FirebaseActivity.REQUEST_GOOGLE_PLAY_SERVICES:
                attemptLogin();
                break;
        }
    }



    private void attemptLogin() {
        mPresenter.attemptLogin(mEmail.getText().toString(),mPassword.getText().toString(),mName.getText().toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof CallbackSin) {
            mCallback = (CallbackSin) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " debe implementar Callback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start(getContext());
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }




    @Override
    public void setPresenter(SingInContract.Presenter presenter) {
        if (presenter != null) {
            mPresenter = presenter;
        } else {
            throw new RuntimeException("El presenter no puede ser nulo");
        }
    }

    @Override
    public void showProgress(boolean show) {

        loadin.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setEmailError(String error) {
     mEmail.setError(error);
        loadin.setVisibility(ProgressBar.INVISIBLE);
    }

    @Override
    public void setNameError(String error) {
        mName.setError(error);
        loadin.setVisibility(ProgressBar.INVISIBLE);
    }

    @Override
    public void setPasswordError(String error) {
     mPassword.setError(error);
        loadin.setVisibility(ProgressBar.INVISIBLE);
    }

    @Override
    public void showLoginError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        loadin.setVisibility(ProgressBar.INVISIBLE);
    }

    @Override
    public void showPushNotifications() {
            getActivity().getSupportFragmentManager().popBackStack();
      // startActivity(new Intent(getActivity(), FirebaseActivity.class));
      //  getActivity().finish();

    }

    @Override
    public void showGooglePlayServicesDialog(int errorCode) {
         mCallback.inInvokeGooglePlayServices(errorCode);
        loadin.setVisibility(ProgressBar.INVISIBLE);
    }

    @Override
    public void showGooglePlayServicesError() {
        Toast.makeText(getActivity(),
                "Se requiere Google Play Services para usar la app", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showNetworkError() {
        Toast.makeText(getActivity(),
                "La red no está disponible. Conéctese y vuelva a intentarlo", Toast.LENGTH_LONG)
                .show();
    }




    public interface CallbackSin {
        void inInvokeGooglePlayServices(int codeError);
    }


}
