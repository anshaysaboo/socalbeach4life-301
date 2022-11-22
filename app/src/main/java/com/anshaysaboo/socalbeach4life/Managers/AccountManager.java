package com.anshaysaboo.socalbeach4life.Managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.anshaysaboo.socalbeach4life.Interfaces.ResultHandler;
import com.anshaysaboo.socalbeach4life.Objects.Exceptions;
import com.anshaysaboo.socalbeach4life.Objects.Review;
import com.anshaysaboo.socalbeach4life.Objects.User;
import com.anshaysaboo.socalbeach4life.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

public class AccountManager {

    private SharedPreferences pref;

    public AccountManager(Context context) {
        this.pref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    public AccountManager(SharedPreferences pref) {
        this.pref = pref;
    }

    // Creates a new user with the given name, email, and password, and returns the created user
    // object. Throws ExistingEmail exception if an account with the given email already exists
    public void registerUser(String name, String email, String password, ResultHandler<User> handler) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Check if a user with the given email already exists
        DocumentReference docRef = db.collection("users").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        handler.onFailure(new Exceptions.EmailExistsException("Account with the given email already exists."));
                    } else {
                        User user = new User(name, email, password);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("users").document(email)
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        handler.onSuccess(user);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        handler.onFailure(new Exceptions.RequestException(""));
                                    }
                                });
                    }
                } else {
                    handler.onFailure(new Exceptions.RequestException(task.toString()));
                }
            }
        });
    }

    // Attempts to log in the current user. If successful, updates local storage to reflect the
    // logged in state. Else, throws an LoginException exception.
    public void loginUser(String email, String password, ResultHandler<User> handler) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        logOut();
        // Check if a user with the given email already exists
        DocumentReference docRef = db.collection("users").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    // Check if account with the given email exists
                    if (document.exists()) {
                        User user = document.toObject(User.class);
                        // Check if password matches
                        if (user.getPassword().equals(password)) {
                            // Locally log in the user
                            setLocalLogIn(user);
                            handler.onSuccess(user);
                        } else {
                            handler.onFailure(new Exceptions.LoginException("Invalid password."));
                        }
                    } else {
                        handler.onFailure(new Exceptions.LoginException("Invalid email."));
                    }
                } else {
                    handler.onFailure(new Exceptions.RequestException(task.toString()));
                }
            }
        });
    }

    // Gets all the reviews that this user has written
    public void getReviewsForUser(String email, ResultHandler<List<Review>> handler) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("reviews")
                .whereEqualTo("authorEmail", email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Review> reviews = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                            Review r = documentSnapshot.toObject(Review.class);
                            r.setFirebaseId(documentSnapshot.getId());
                            reviews.add(r);
                        }
                        handler.onSuccess(reviews);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        handler.onFailure(new Exceptions.RequestException(e.getMessage()));
                    }
                });
    }

    // Checks if the local state of the app is logged in/logged out
    public boolean isLoggedIn() {
        return !pref.getString("user_name", "").equals("");
    }

    // Sets the local state of the app to logged in
    public void setLocalLogIn(User user) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user_name", user.getName());
        editor.putString("email", user.getEmail());
        editor.apply();
    }

    public String getUserName() {
        return pref.getString("user_name", "");
    }

    public String getUserEmail() {
        return pref.getString("email", "");
    }

    // Sets the local state of the app to logged out
    public void logOut() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user_name", "");
        editor.putString("email", "");
        editor.apply();
    }
}
