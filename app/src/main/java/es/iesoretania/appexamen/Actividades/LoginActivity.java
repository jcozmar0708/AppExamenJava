package es.iesoretania.appexamen.Actividades;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import es.iesoretania.appexamen.Miscelanea.AdminSQLiteOpenHelper;
import es.iesoretania.appexamen.R;
import es.iesoretania.appexamen.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AdminSQLiteOpenHelper adminSQLiteOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);

        binding.botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = String.valueOf(binding.EditTextUsuario.getText());
                String contrasenia = String.valueOf(binding.EditTextPassword.getText());

                if (usuario.isEmpty() || contrasenia.isEmpty()) {
                    mostrarAlerta("Se deben rellenar todos los campos");
                } else {
                    Intent intent = new Intent(view.getContext(), RegistrarActivity.class);

                    SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("AppSharedPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("usuario",usuario);
                    editor.putString("contrasenia",contrasenia);
                    editor.apply();

                    startActivity(intent);
                }
            }
        });

        binding.botonAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = String.valueOf(binding.EditTextUsuario.getText());
                String contrasenia = String.valueOf(binding.EditTextPassword.getText());

                if (usuario.isEmpty() || contrasenia.isEmpty()) {
                    mostrarAlerta("Se deben rellenar todos los campos");
                } else {
                    if (adminSQLiteOpenHelper.usuarioExiste(usuario)) {
                        Intent intent = new Intent(view.getContext(), HomeActivity.class);
                        startActivity(intent);
                    } else {
                        mostrarAlerta("El usuario no existe");
                    }
                }
            }
        });
    }

    private void mostrarAlerta(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje)
                .setTitle("Error")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create()
                .show();
    }
}