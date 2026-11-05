package com.yojo.app.ui.auth

import android.widget.Toast
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
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var isLoginMode by remember { mutableStateOf(true) }
    var emailOrPhone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (auth.currentUser != null) {
            navController.navigate("main_app") {
                popUpTo("auth") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isLoginMode) "📝 YOJO Login" else "📝 YOJO Register",
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
                text = if (isLoginMode) "Welcome Back to YOJO" else "Welcome to YOJO",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Connect, Game, and Live Chat globally",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = emailOrPhone,
                onValueChange = { emailOrPhone = it },
                label = { Text("Email or Phone Number") },
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

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it }
                )
                Text("Remember Me")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (emailOrPhone.isNotBlank() && password.isNotBlank()) {
                        if (isLoginMode) {
                            auth.signInWithEmailAndPassword(emailOrPhone, password)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Login Successful! ✅", Toast.LENGTH_SHORT).show()
                                    navController.navigate("main_app") {
                                        popUpTo("auth") { inclusive = true }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Login Nakam: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                }
                        } else {
                            auth.createUserWithEmailAndPassword(emailOrPhone, password)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Account Created! ✅", Toast.LENGTH_LONG).show()
                                    navController.navigate("main_app") {
                                        popUpTo("auth") { inclusive = true }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Registration nakam: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                }
                        }
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = if (isLoginMode) "Login" else "Create Account",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Google Sign-In Triggered 🌐", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Continue with Google 🌐",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { isLoginMode = !isLoginMode }) {
                Text(
                    text = if (isLoginMode) "Don't have an account? Register" else "Already have an account? Login"
                )
            }
        }
    }
}
