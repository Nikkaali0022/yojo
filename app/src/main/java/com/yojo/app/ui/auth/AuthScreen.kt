package com.yojo.app.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yojo.app.data.repository.AuthRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoginMode by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authRepository = remember { AuthRepository() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (isLoginMode) "🔐 YOJO Login" else "📝 YOJO Register", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to YOJO",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Connect, Game, and Live Chat globally",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email address") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button (Login / Signup)
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Barah-e-karam email aur password darj karein", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true
                    if (isLoginMode) {
                        authRepository.loginUser(email, password) { success, error ->
                            isLoading = false
                            if (success) {
                                Toast.makeText(context, "Login kamyaab ho gaya!", Toast.LENGTH_SHORT).show()
                                navController.navigate("main_app") {
                                    popUpTo("auth") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, "Login nakam: $error", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        authRepository.registerUser(email, password) { success, error ->
                            isLoading = false
                            if (success) {
                                Toast.makeText(context, "Account kamyaab taur par ban gaya!", Toast.LENGTH_SHORT).show()
                                navController.navigate("main_app") {
                                    popUpTo("auth") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, "Registration nakam: $error", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(
                        text = if (isLoginMode) "Login" else "Create Account",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Switch between Login and Register Mode
            TextButton(
                onClick = { isLoginMode = !isLoginMode }
            ) {
                Text(
                    text = if (isLoginMode) "Don't have an account? Register" else "Already have an account? Login",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
