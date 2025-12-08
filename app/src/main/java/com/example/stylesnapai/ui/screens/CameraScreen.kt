package com.example.stylesnapai.ui.screens

import android.Manifest
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.stylesnapai.ui.CameraViewModel
import com.example.stylesnapai.ui.Screen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    navController: NavController,
    cameraViewModel: CameraViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by cameraViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var permissionRequested by remember { mutableStateOf(false) }
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted && !permissionRequested) {
            permissionRequested = true
            cameraPermissionState.launchPermissionRequest()
        }
    }

    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val capture = imageCapture ?: return@FloatingActionButton
                    takePhoto(context, capture) { uri ->
                        uri?.let {
                            cameraViewModel.generateFromUri(it)
                        }
                    }
                },
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text("Snap")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (cameraPermissionState.status.isGranted) {
                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }
                            val capture = ImageCapture.Builder().build()
                            imageCapture = capture
                            val selector = CameraSelector.DEFAULT_BACK_CAMERA
                            try {
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    selector,
                                    preview,
                                    capture
                                )
                            } catch (e: Exception) {
                                Log.e("CameraScreen", "Camera bind failed", e)
                            }
                        }, ContextCompat.getMainExecutor(ctx))
                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                PermissionRationale(onRequestPermission = {
                    cameraPermissionState.launchPermissionRequest()
                })
            }

            BottomControls(
                onOpenLibrary = { navController.navigate(Screen.Library.route) },
                onOpenSettings = { navController.navigate(Screen.Settings.route) }
            )

            if (uiState.isProcessing) {
                ProcessingDialog()
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 120.dp)
            )
        }
    }

    uiState.errorMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message)
            cameraViewModel.clearError()
        }
    }

    uiState.latestResult?.let {
        LaunchedEffect(it) {
            navController.navigate(Screen.Results.route)
        }
    }
}

@Composable
private fun BottomControls(
    onOpenLibrary: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.size(64.dp).clickable { onOpenLibrary() },
            shape = MaterialTheme.shapes.medium
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Collections, contentDescription = "Library")
            }
        }
        Spacer(modifier = Modifier.size(64.dp))
        IconButton(onClick = onOpenSettings) {
            Icon(Icons.Default.Settings, contentDescription = "Settings")
        }
    }
}

@Composable
private fun ProcessingDialog() {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {},
        text = {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(Modifier.height(8.dp))
                Text("Processing with AI…")
            }
        }
    )
}

@Composable
private fun PermissionRationale(onRequestPermission: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Warning, contentDescription = null)
                Text("Camera permission is required to capture photos.")
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = onRequestPermission) { Text("Grant permission") }
            }
        }
    }
}

private fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onResult: (Uri?) -> Unit
) {
    val photoFile = File(
        context.cacheDir,
        SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
            .format(System.currentTimeMillis()) + ".jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exception: ImageCaptureException) {
                onResult(null)
            }

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    photoFile
                )
                onResult(uri)
            }
        }
    )
}
