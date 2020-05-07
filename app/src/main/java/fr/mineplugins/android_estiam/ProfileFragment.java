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
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    String email, name, password;

    TextView title_profile;

    TextInputLayout field_email;
    TextInputLayout field_name;
    TextInputLayout field_password;

    Button button_edit;

    private Context context;
    private Session session;
    private final ArrayList<User> users = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_profile, container, false);

        context = view.getContext();
        session = new Session(context);

        title_profile = view.findViewById(R.id.title_profile);
        field_email = view.findViewById(R.id.field_edit_email);
        field_name = view.findViewById(R.id.field_edit_name);
        field_password = view.findViewById(R.id.field_edit_password);
        button_edit = view.findViewById(R.id.button_edit);

        field_email.getEditText().setText(session.getEmail());
        field_name.getEditText().setText(session.getName());

        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getAndTestInputValues()) {
                    edit();
                }
            }
        });

        return view;
    }

    private boolean getAndTestInputValues() {
        email = String.valueOf(field_email.getEditText().getText());
        password = String.valueOf(field_password.getEditText().getText());
        name = String.valueOf(field_name.getEditText().getText());

        Log.d("Request", "Error: "+ email);
        if (TextUtils.isEmpty(email)) {
            field_email.setError("Vous devez entrer une adresse email");
            return false;
        }

        if (TextUtils.isEmpty(name)) {
            field_name.setError("Vous devez entrer un nom");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            field_email.setError("Vous devez entrer une adresse email valide");
            return false;
        }

        return true;
    }

    public void edit() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = "http://www.vasedhonneurofficiel.com/ws/modifyUserAccount.php";
        StringRequest objectRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Gson g = new Gson();
                        try {
                            EditResponse edit = g.fromJson(response, EditResponse.class);
                            if (edit.success == 200) {
                                resetTextMessage();
                                session.setEmail(email);
                                session.setName(name);
                            } else {
                                setTextMessage(edit.message);
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
                params.put("id", String.valueOf(session.getId()));
                params.put("email", email);
                params.put("name", name);
                if (!TextUtils.isEmpty(password)) {
                    params.put("password", password);
                }
                return params;
            }
        };
        requestQueue.add(objectRequest);
    }

    private void setTextMessage(String message) {
        title_profile.setText(message);
        title_profile.setTextColor(Color.RED);
    }

    private void resetTextMessage() {
        title_profile.setText("Modifier mon profil");
        title_profile.setTextColor(Color.BLACK);
        field_email.setError("");
        field_name.setError("");
        field_password.setError("");
    }
}
