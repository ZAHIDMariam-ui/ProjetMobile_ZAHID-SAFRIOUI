package com.example.projetappmobile.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projetappmobile.data.database.AppDatabase
import com.example.projetappmobile.data.repository.UserRepository
import com.example.projetappmobile.ui.screens.home.RajaGreen
import com.example.projetappmobile.viewmodel.AuthViewModel
import com.example.projetappmobile.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var isEmailSent by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // ViewModel Instantiation
    val db = remember { AppDatabase.getInstance(context) }
    val userRepository = remember { UserRepository(db.userDao()) }
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(userRepository))

    val resetStatus by authViewModel.resetPasswordStatus.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()

    // Handle state changes
    LaunchedEffect(resetStatus, errorMessage) {
        resetStatus?.let {
            if (it) {
                isEmailSent = true
                authViewModel.clearResetStatus() // Reset status in ViewModel
            }
        }
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            authViewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mot de passe oublié",
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!isEmailSent) {
                // ... UI for entering email (unchanged)
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    placeholder = { Text("votre@email.com") },
                    leadingIcon = {
                        Icon(Icons.Filled.Email, contentDescription = null, tint = RajaGreen)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            coroutineScope.launch {
                                authViewModel.initiatePasswordReset(email, context)
                            }
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Veuillez entrer une adresse email valide")
                            }
                        }
                    },
                    enabled = email.isNotEmpty(),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RajaGreen)
                ) {
                    Text("Envoyer le lien", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                // ... UI for success message (unchanged)
                Text(text = "Email envoyé !")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.popBackStack() }) {
                    Text("Retour à la connexion")
                }
            }
        }
    }
}
