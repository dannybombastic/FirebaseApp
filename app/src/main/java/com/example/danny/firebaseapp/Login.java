package com.example.danny.firebaseapp;
import com.bumptech.glide.Glide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.ForegroundLinearLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SearchViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.danny.firebaseapp.login.LoginContract;
import com.example.danny.firebaseapp.login.LoginInteractor;
import com.example.danny.firebaseapp.login.LoginPresenter;
import com.example.danny.firebaseapp.notifications.PushNotificationsFragment;
import com.example.danny.firebaseapp.notifications.PushNotificationsPresenter;
import com.example.danny.firebaseapp.sing_in.FragmentSingIn;
import com.example.danny.firebaseapp.sing_in.SinInteractor;
import com.example.danny.firebaseapp.sing_in.Sing_presenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.Executor;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.example.danny.firebaseapp.FirebaseActivity.JSON;
import static com.example.danny.firebaseapp.R.string.firebase_database_url;


public class Login extends Fragment implements View.OnClickListener,LoginContract.View {
    private LoginContract.Presenter mPresenter;
    private TextInputEditText mEmail;
    private TextInputEditText mPassword;

    private Button mSignInButton;
    private View mLoginForm;
    private View mLoginProgress;
    private Callback mCallback;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FragmentSingIn mSingInFragment;

    public static Login newInstance() {
        Login fragment = new Login();
        // Setup de argumentos en caso de que los haya
        return fragment;
    }

    public Login() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Extracción de argumentos en caso de que los haya
        }

        // Obtener instancia FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    showPushNotifications();

                } else {
                    FirebaseActivity.USER_LOG = false;
                    // El usuario no está logueado
                }
            }
        };
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragmento, container, false);





        mLoginProgress = root.findViewById(R.id.login_progress);

        mEmail = (TextInputEditText) root.findViewById(R.id.tv_email);
        mPassword = (TextInputEditText) root.findViewById(R.id.tv_password);


        mSignInButton = (Button) root.findViewById(R.id.b_sign_in);

        // Eventos
        this.mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mEmail.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
       this.mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        this.mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        return root;
    }

    private void setupLoginBackground(final View root) {
        Glide.with(this)
                .load(R.drawable.banner)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new SimpleTarget<GlideDrawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        final int sdk = android.os.Build.VERSION.SDK_INT;
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            root.setBackgroundDrawable(resource);
                        } else {

                            root.setBackground(resource);
                        }
                    }
                });
    }



    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mCallback = (Callback) context;
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

            mFirebaseAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start(getActivity());
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
        if(mPresenter.attemptLogin(mEmail.getText().toString(),mPassword.getText().toString())){
            showPushNotifications();

        }
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        if (presenter != null) {
            mPresenter = presenter;
        } else {
            throw new RuntimeException("El presenter no puede ser nulo");
        }
    }

    @Override
    public void showProgress(boolean show) {

        mLoginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setEmailError(String error) {
        this.mEmail.setError(error);
    }

    @Override
    public void setPasswordError(String error) {
        this.mPassword.setError(error);
    }

    @Override
    public void showLoginError(String msg) {
        Snackbar.make(getView(),"Log-in Error",Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showPushNotifications() {

        FirebaseActivity.USER_LOG = true;
      //  getFragmentManager().popBackStack();
        startActivity(new Intent(getActivity(), FirebaseActivity.class));
        getActivity().finish();
    }

    @Override
    public void showGooglePlayServicesDialog(int codeError) {
        mCallback.onInvokeGooglePlayServices(codeError);
    }

    @Override
    public void showGooglePlayServicesError() {
        Snackbar.make(getView(),"Se requiere Google Play Services para usar la app",Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void showNetworkError() {
        Snackbar.make(getView(),"La red no está disponible. Conéctese y vuelva a intentarlo",Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View v) {






    }

    interface Callback {
        void onInvokeGooglePlayServices(int codeError);
    }
}
