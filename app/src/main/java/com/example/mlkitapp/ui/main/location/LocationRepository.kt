package com.example.mlkitapp.ui.main.location

import com.google.firebase.auth.FirebaseUser

interface LocationRepository {
    val userId: FirebaseUser?
}
