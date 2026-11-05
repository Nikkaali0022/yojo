package com.yojo.app.data.repository

import com.google.firebase.auth.FirebaseAuth

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser() = auth.currentUser

    fun loginUser(email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener { 
                onResult(true, null) 
            }
            .addOnFailureListener { e -> 
                onResult(false, e.localizedMessage) 
            }
    }

    fun registerUser(email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { 
                onResult(true, null) 
            }
            .addOnFailureListener { e -> 
                onResult(false, e.localizedMessage) 
            }
    }

    fun logout() {
        auth.signOut()
    }
}
