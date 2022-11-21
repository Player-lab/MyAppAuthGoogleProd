package angers.bonneau.myappauthgoogle;





import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {
    //ici on set elements afin d'interagir avec l'image notement lorsqu'on clique dessus
    //et on set egalement des element qui permettrons la connexion au compte google
    ImageView googleAuthImg;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //on recherche notre image par un id pour le mettre dans l'element googleAuthImg
        googleAuthImg = findViewById(R.id.GoogleAuth);

        // Configurer l'ouverture de session pour demander l'ID, l'adresse électronique et le profil de base de l'utilisateur.
        // profil de base. L'ID et le profil de base sont inclus dans DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        // Construit un GoogleSignInClient avec les options spécifiées par gso.

        gsc = GoogleSignIn.getClient(this, gso);

        //lorsqu'on clique sur le bouton se connecter on lance la methode signIn
        googleAuthImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });
    }
    //on récupere les infos du client et on met code de requette (pas compris pk) vérif en mode debug
    private void SignIn() {
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, 100);

    }
    // on reecriris la methode onAction machin
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //si code requete = 100 alors on lance la demande avec la liste deroulabte de compte (à vérif en mode debug)
        if (requestCode==100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            //lance la methode signingoogleokActiv...
            SignGoogleOkActivity();
            try {
                task.getResult(ApiException.class);

            } catch (ApiException e) {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //on lance la deuxieme activite (SignGoogleOkActivity.class)
    private void SignGoogleOkActivity() {
        finish();
        Intent intent = new Intent(getApplicationContext(), SignGoogleOkActivity.class);
        startActivity(intent);
    }





}