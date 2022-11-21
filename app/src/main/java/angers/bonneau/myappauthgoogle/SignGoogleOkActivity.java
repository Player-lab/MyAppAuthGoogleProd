package angers.bonneau.myappauthgoogle;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;

import java.text.DateFormat;
import java.util.Date;
import java.util.Calendar;
public class SignGoogleOkActivity extends AppCompatActivity {
    //pareil que tt à l'heure
    TextView name,mail;
    Button logout;


    GoogleSignInOptions gso;
    GoogleSignInClient gsc;


    // creation de variables pour traduire notre texte
    private EditText courseNameEdt, courseDurationEdt, courseDescriptionEdt;

    // creation de variable pour boutton
    private Button submitCourseBtn;

    // creation de variable string pour stocker
    // nos valeurs pour le champ de texte.
    private String courseName, courseDuration, courseDescription;
    // creation de variable
    // pour le firebase firestore.
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_google_ok);
        //same que les autres fois
        String time = now();
        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);
        logout = findViewById(R.id.logout);



        // initialisation de notre texte et de nos bouttons.
        courseNameEdt = findViewById(R.id.idEdtCourseName);
        courseDescriptionEdt = findViewById(R.id.idEdtCourseDescription);
        courseDurationEdt = findViewById(R.id.idEdtCourseDuration);
        submitCourseBtn = findViewById(R.id.idBtnSubmitCourse);

        //initialisation qui servira lorsqu'on voudra log out
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(this);




        //si une personne est actuellement co on remplace les textes par défaut par les infos de la personne
        if(account!=null){
            String Name = account.getDisplayName();
            String Mail = account.getEmail();
            name.setText(Name);
            mail.setText(Mail);
        }


        // optention de l'instancee
        // du Firebase Firestore.


        // Ajout clicklistener pour boutton.
        submitCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // optention de données par le champ de texte.
                courseName = courseNameEdt.getText().toString();
                courseDescription = courseDescriptionEdt.getText().toString();
                courseDuration = courseDurationEdt.getText().toString();

                // vérification du champ de texte (si vide ou pas).
                if (TextUtils.isEmpty(courseName)) {
                    courseNameEdt.setError("Merci d'entrer le nom du cours");
                } else if (TextUtils.isEmpty(courseDescription)) {
                    courseDescriptionEdt.setError("Merci d'entrer la description du cours");
                } else if (TextUtils.isEmpty(courseDuration)) {
                    courseDurationEdt.setError("Merci d'entrer la durée");
                } else {
                    // Méthode pour ajouter des données dans le Firebase Firestore.
                    getCurrentIdAnWriteData(time, account.getDisplayName().toString(),account.getEmail().toString());
                }
            }
        });
        //si on clique sur le log out on lance la methode qui corresponf
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignOut();
            }
        });
    }

    //on récupère la dernière data saisie grace à
    //order by id desc de la collection Cours.... et on limite à un le nombre de résultat
    private void getCurrentIdAnWriteData(String time, String dataName, String dataMail) {
        db = FirebaseFirestore.getInstance();
        db.collection("CoursInformation")
                .orderBy("id", Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //si l'id est supérieur à 0 alors on lui met actue+1 sinon 0
                                //on pourrait vérifier si on ne récupère rien mais si c'était le cas on pourrait pas faire d'oder by id
                                Long id = ((Long) document.get("id") >= 0) ? ((Long) document.get("id") + 1) : 0;
                                addDataToFirestore(courseName, courseDescription, courseDuration, dataName,dataMail, time, id);
                            }
                        } else {
                            Log.d(TAG, "Impossible de récupérer ds données ", task.getException());
                        }

                    }
                });

    }


    private void addDataToFirestore(String courseName, String courseDescription, String courseDuration,String name, String mail, String time, Long id) {
        db = FirebaseFirestore.getInstance();
        // creation d'une collection de référence
        // pour Firebase Firetore database.
        CollectionReference dbCourses = db.collection("CoursInformation");

        // ajout de données dans la classe d'objet de notre base de donnée.
        Courses courses = new Courses(courseName, courseDescription, courseDuration,name,mail, time, id);

        // below method permet d'ajouter des données dans le Firebase Firestore.
        dbCourses.add(courses).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // après que l'ajout de données soit un succès
                // affichage message succès.
                Toast.makeText(SignGoogleOkActivity.this, "Le cours a été ajouté", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(SignGoogleOkActivity.this, "Impossible d'ajouter le cours" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String now(){
        //sert à récuperer la date actuel
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        String todayAsString = df.format(today);
        return todayAsString;
    }



    private void SignOut() {
        //on se deco
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                //on lance la première methode
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}