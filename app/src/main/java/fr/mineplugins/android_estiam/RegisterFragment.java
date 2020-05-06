package fr.mineplugins.android_estiam;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RegisterFragment extends Fragment {

    String email, password;

    TextView title_register;

    TextInputLayout field_email;
    TextInputLayout field_password;

    Button button_login;
    Button button_register;

    private Context context;
    private Session session;
    private final ArrayList<User> users = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_register, container, false);

        context = view.getContext();
        session = new Session(context);

        title_register = (TextView) view.findViewById((R.id.title_register));
        field_email = (TextInputLayout) view.findViewById(R.id.field_email);
        field_password = (TextInputLayout) view.findViewById(R.id.field_password);
        button_login = (Button) view.findViewById(R.id.button_login);
        button_register = (Button) view.findViewById(R.id.button_register);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getAndTestInputValues()) {
                    login();
                }
            }
        });

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getAndTestInputValues()) {
                    register();
                }
            }
        });

        return view;
    }

    private boolean getAndTestInputValues() {
        email = String.valueOf(field_email.getEditText().getText());
        password = String.valueOf(field_password.getEditText().getText());

        Log.d("Request", "Error: "+ email);
        if (TextUtils.isEmpty(email)) {
            field_email.setError("Vous devez entrer une adresse email");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            field_password.setError("Vous devez entrer un mot de passe");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            field_email.setError("Vous devez entrer une adresse email valide");
            return false;
        }

        return true;
    }

    private void register() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = "http://www.vasedhonneurofficiel.com/ws/registration.php";
        StringRequest objectRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Gson g = new Gson();
                        try {
                            RegisterResponse register = g.fromJson(response, RegisterResponse.class);
                            if (register.success == 200) {
                                session.setEmail(register.email);
                                resetTextMessage();
                            } else {
                                setTextMessage(register.message);
                            }
                        } catch (JsonParseException e) {
                            setTextMessage("Une erreur interne est survenue");
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Request", "Error: "+ error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        requestQueue.add(objectRequest);
    }

    private void login() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = "http://www.vasedhonneurofficiel.com/ws/authentication.php";
        StringRequest objectRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Gson g = new Gson();
                        try {
                            User[] userArr = g.fromJson(response, User[].class);
                            Collections.addAll(users, userArr);
                            session.setEmail(users.get(0).email);
                            resetTextMessage();
                        } catch (JsonParseException e) {
                            setTextMessage("Email ou mot de passe invalide");
                            field_email.setError("Veuillez vérifier");
                            field_password.setError("Veuillez vérifier");
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Request", "Error: "+ error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        requestQueue.add(objectRequest);
    }

    private void setTextMessage(String message) {
        title_register.setText(message);
        title_register.setTextColor(Color.RED);
    }

    private void resetTextMessage() {
        title_register.setText("Formulaire de connexion");
        title_register.setTextColor(Color.BLACK);
        field_email.setError("");
        field_password.setError("");
    }
}
