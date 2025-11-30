package com.example.projetappmobile.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projetappmobile.data.database.AppDatabase
import com.example.projetappmobile.data.entity.User
import com.example.projetappmobile.data.repository.UserRepository
import com.example.projetappmobile.ui.navigation.Route
import com.example.projetappmobile.ui.screens.home.RajaGreen
import com.example.projetappmobile.viewmodel.AuthViewModel
import com.example.projetappmobile.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Instanciation du ViewModel avec la Factory
    val db = remember { AppDatabase.getInstance(context) }
    val userRepository = remember { UserRepository(db.userDao()) }
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(userRepository))

    // Collecter les états depuis le ViewModel de manière idiomatique
    val registrationStatus by authViewModel.registrationStatus.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()

    // Observer le statut d'inscription pour la navigation
    LaunchedEffect(registrationStatus) {
        registrationStatus?.let { status ->
            if (status > 0) {
                // Inscription réussie, naviguer vers Login
                navController.navigate(Route.Login.route) {
                    popUpTo(Route.Signup.route) { inclusive = true }
                }
            }
        }
    }

    // Observer les erreurs pour afficher la Snackbar
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            authViewModel.clearError() // Réinitialiser l'erreur après l'avoir affichée
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Inscription",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = RajaGreen
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Champ Nom complet
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Nom complet") },
                placeholder = { Text("Votre nom complet") },
                leadingIcon = {
                    Icon(Icons.Filled.Person, contentDescription = null, tint = RajaGreen)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RajaGreen,
                    focusedLabelColor = RajaGreen
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Champ Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                placeholder = { Text("votre@email.com") },
                leadingIcon = {
                    Icon(Icons.Filled.Email, contentDescription = null, tint = RajaGreen)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RajaGreen,
                    focusedLabelColor = RajaGreen
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Champ Mot de passe
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mot de passe") },
                placeholder = { Text("••••••••") },
                leadingIcon = {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = RajaGreen)
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RajaGreen,
                    focusedLabelColor = RajaGreen
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Champ Confirmation mot de passe
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmer le mot de passe") },
                placeholder = { Text("••••••••") },
                leadingIcon = {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = RajaGreen)
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RajaGreen,
                    focusedLabelColor = RajaGreen
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Bouton d'inscription
            Button(
                onClick = {
                    coroutineScope.launch {
                        if (password != confirmPassword) {
                            snackbarHostState.showSnackbar("Les mots de passe ne correspondent pas")
                        } else if (password.length < 6) {
                            snackbarHostState.showSnackbar("Le mot de passe doit contenir au moins 6 caractères")
                        } else {
                            val user = User(
                                email = email,
                                password = password, // ⚠️ Dans un vrai projet, hachez le mot de passe !
                                username = fullName
                            )
                            authViewModel.register(user)
                        }
                    }
                },
                enabled = fullName.isNotEmpty() && email.isNotEmpty() &&
                        password.isNotEmpty() && confirmPassword.isNotEmpty() &&
                        registrationStatus == null, // Utilisation de l'état collecté
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RajaGreen,
                    disabledContainerColor = Color.Gray
                )
            ) {
                if (registrationStatus != null) { // Utilisation de l'état collecté
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        "Créer un compte",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lien vers la connexion
            TextButton(onClick = { navController.navigate(Route.Login.route) }) {
                Text(
                    "Déjà un compte ? Se connecter",
                    color = RajaGreen,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    val navController = rememberNavController()
    SignUpScreen(navController)
}
