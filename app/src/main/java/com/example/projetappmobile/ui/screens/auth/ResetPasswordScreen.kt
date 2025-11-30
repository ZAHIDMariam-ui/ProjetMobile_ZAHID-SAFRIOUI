package com.example.projetappmobile.ui.screens.auth


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projetappmobile.service.EmailService
import com.example.projetappmobile.ui.screens.home.RajaGreen
import com.example.projetappmobile.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    navController: NavController,
    token: String? = null
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isTokenValid by remember { mutableStateOf<Boolean?>(null) }
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Vérifier la validité du token au chargement
    LaunchedEffect(token) {
        if (token != null) {
            isTokenValid = authViewModel.validateResetToken(token)
        } else {
            // Pour le débogage, récupérer le dernier token
            val debugToken = EmailService.getLastResetToken(context)
            if (debugToken != null) {
                isTokenValid = authViewModel.validateResetToken(debugToken)
            }
        }
    }

    // Observer le statut de réinitialisation
    LaunchedEffect(authViewModel.resetPasswordStatus.collectAsState().value) {
        authViewModel.resetPasswordStatus.value?.let { success ->
            if (success) {
                snackbarHostState.showSnackbar("Mot de passe réinitialisé avec succès")
                navController.navigate("login") {
                    popUpTo("forgot_password") { inclusive = true }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nouveau mot de passe",
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (isTokenValid) {
                null -> {
                    // Chargement
                    CircularProgressIndicator(color = RajaGreen)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Vérification du token...")
                }
                false -> {
                    // Token invalide
                    Text(
                        text = "Lien invalide",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Ce lien de réinitialisation est invalide ou a expiré.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { navController.navigate("forgot_password") },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RajaGreen
                        )
                    ) {
                        Text(
                            "Demander un nouveau lien",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                true -> {
                    // Formulaire de réinitialisation
                    Text(
                        text = "Créer un nouveau mot de passe",
                        style = MaterialTheme.typography.headlineSmall,
                        color = RajaGreen,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Veuillez saisir votre nouveau mot de passe",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Champ Nouveau mot de passe
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Nouveau mot de passe") },
                        placeholder = { Text("••••••••") },
                        leadingIcon = {
                            Icon(Icons.Filled.Lock, contentDescription = null, tint = RajaGreen)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RajaGreen,
                            focusedLabelColor = RajaGreen,
                            cursorColor = RajaGreen
                        ),
                        singleLine = true
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
                            focusedLabelColor = RajaGreen,
                            cursorColor = RajaGreen
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Bouton de réinitialisation
                    Button(
                        onClick = {
                            if (newPassword != confirmPassword) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Les mots de passe ne correspondent pas")
                                }
                                return@Button
                            }
                            if (newPassword.length < 6) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Le mot de passe doit contenir au moins 6 caractères")
                                }
                                return@Button
                            }

                            coroutineScope.launch {
                                val resetToken = token ?: EmailService.getLastResetToken(context)
                                if (resetToken != null) {
                                    authViewModel.resetPassword(resetToken, newPassword)
                                } else {
                                    snackbarHostState.showSnackbar("Token de réinitialisation manquant")
                                }
                            }
                        },
                        enabled = newPassword.isNotEmpty() && confirmPassword.isNotEmpty(),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RajaGreen
                        )
                    ) {
                        Text(
                            "Réinitialiser le mot de passe",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}