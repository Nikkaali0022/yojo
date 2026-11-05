package com.yojo.app.ui.auth

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as? Activity
    val auth = FirebaseAuth.getInstance()

    // Modes: false = Email/Password, true = Phone OTP
    var isPhoneMode by remember { mutableStateOf(false) }
    var isLoginMode by remember { mutableStateOf(true) }

    // Email/Password states
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Phone OTP states
    var phoneNumber by remember { mutableStateOf("+92") }
    var otpCode by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Web Client ID from Firebase
    val webClientId = "809869263099-b3a8jgt6uup18bhdludtbiq25pcq1a9e.apps.googleusercontent.com"

    LaunchedEffect(Unit) {
        if (auth.currentUser != null) {
            navController.navigate("main_app") {
                popUpTo("auth") { inclusive = true }
            }
        }
    }

    // Google Sign-In Launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (idToken != null) {
                isLoading = true
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(credential)
                    .addOnSuccessListener {
                        isLoading = false
                        Toast.makeText(context, "Google Login Successful! ✅", Toast.LENGTH_SHORT).show()
                        navController.navigate("main_app") {
                            popUpTo("auth") { inclusive = true }
                        }
                    }
                    .addOnFailureListener { e ->
                        isLoading = false
                        Toast.makeText(context, "Firebase Auth Failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
            }
        } catch (e: ApiException) {
            Toast.makeText(context, "Google Sign-In Failed (Code: ${e.statusCode})", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isPhoneMode) "📱 YOJO Phone Login" else if (isLoginMode) "📝 YOJO Login" else "📝 YOJO Register",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to YOJO",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Connect, Game, and Live Chat globally",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mode Switch Tabs (Email vs Phone OTP)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { isPhoneMode = false },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isPhoneMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (!isPhoneMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text("Email")
                }
                Button(
                    onClick = { isPhoneMode = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPhoneMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isPhoneMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text("Phone OTP")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (!isPhoneMode) {
                // EMAIL & PASSWORD UI
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank()) {
                            isLoading = true
                            if (isLoginMode) {
                                auth.signInWithEmailAndPassword(email.trim(), password)
                                    .addOnSuccessListener {
                                        isLoading = false
                                        Toast.makeText(context, "Login Successful! ✅", Toast.LENGTH_SHORT).show()
                                        navController.navigate("main_app") {
                                            popUpTo("auth") { inclusive = true }
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        isLoading = false
                                        Toast.makeText(context, "Login Failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                    }
                            } else {
                                auth.createUserWithEmailAndPassword(email.trim(), password)
                                    .addOnSuccessListener {
                                        isLoading = false
                                        Toast.makeText(context, "Account Created! ✅", Toast.LENGTH_LONG).show()
                                        navController.navigate("main_app") {
                                            popUpTo("auth") { inclusive = true }
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        isLoading = false
                                        Toast.makeText(context, "Register Failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                    }
                            }
                        } else {
                            Toast.makeText(context, "Please enter Email and Password", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = if (isLoading) "Please wait..." else if (isLoginMode) "Login" else "Create Account",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { isLoginMode = !isLoginMode }) {
                    Text(
                        text = if (isLoginMode) "Don't have an account? Register" else "Already have an account? Login"
                    )
                }
            } else {
                // PHONE OTP UI
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number (e.g. +923001234567)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = verificationId == null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (verificationId != null) {
                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = { otpCode = it },
                        label = { Text("Enter 6-Digit OTP") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Button(
                    onClick = {
                        if (verificationId == null) {
                            // Step 1: Send OTP
                            if (phoneNumber.length < 10 || !phoneNumber.startsWith("+")) {
                                Toast.makeText(context, "Number country code (+92) ke sath likhein", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (activity == null) {
                                Toast.makeText(context, "Activity error", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            isLoading = true
                            val options = PhoneAuthOptions.newBuilder(auth)
                                .setPhoneNumber(phoneNumber.trim())
                                .setTimeout(60L, TimeUnit.SECONDS)
                                .setActivity(activity)
                                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                        auth.signInWithCredential(credential)
                                            .addOnSuccessListener {
                                                isLoading = false
                                                Toast.makeText(context, "Phone Verified Automatically! ✅", Toast.LENGTH_SHORT).show()
                                                navController.navigate("main_app") {
                                                    popUpTo("auth") { inclusive = true }
                                                }
                                            }
                                    }

                                    override fun onVerificationFailed(e: FirebaseException) {
                                        isLoading = false
                                        Toast.makeText(context, "OTP Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                    }

                                    override fun onCodeSent(verId: String, token: PhoneAuthProvider.ForceResendingToken) {
                                        isLoading = false
                                        verificationId = verId
                                        Toast.makeText(context, "OTP Code Bhej Diya Gaya Hai! 📩", Toast.LENGTH_LONG).show()
                                    }
                                })
                                .build()
                            PhoneAuthProvider.verifyPhoneNumber(options)
                        } else {
                            // Step 2: Verify OTP Code
                            if (otpCode.length == 6) {
                                isLoading = true
                                val credential = PhoneAuthProvider.getCredential(verificationId!!, otpCode.trim())
                                auth.signInWithCredential(credential)
                                    .addOnSuccessListener {
                                        isLoading = false
                                        Toast.makeText(context, "Phone Login Successful! ✅", Toast.LENGTH_SHORT).show()
                                        navController.navigate("main_app") {
                                            popUpTo("auth") { inclusive = true }
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        isLoading = false
                                        Toast.makeText(context, "Invalid OTP: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                    }
                            } else {
                                Toast.makeText(context, "6-digit OTP code enter karein", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = if (isLoading) "Processing..." else if (verificationId == null) "Send OTP Code 📲" else "Verify & Login ✅",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                if (verificationId != null) {
                    TextButton(onClick = {
                        verificationId = null
                        otpCode = ""
                    }) {
                        Text("Change Phone Number")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            // GOOGLE SIGN-IN BUTTON
            OutlinedButton(
                onClick = {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(webClientId)
                        .requestEmail()
                        .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    googleSignInClient.signOut().addOnCompleteListener {
                        googleSignInLauncher.launch(googleSignInClient.signInIntent)
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Continue with Google 🌐",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
