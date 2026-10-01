package ir.keyvanadili.karmakhodro.ui.screens

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.keyvanadili.karmakhodro.backup.BackupManager
import ir.keyvanadili.karmakhodro.data.Repository
import ir.keyvanadili.karmakhodro.ui.components.FormSection
import ir.keyvanadili.karmakhodro.ui.theme.KarmaSpacing
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    repository: Repository,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val backupManager = remember { BackupManager(repository) }
    var isWorking by remember { mutableStateOf(false) }

    val versionName = remember {
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pInfo.versionName ?: "1.0.0"
        } catch (e: PackageManager.NameNotFoundException) {
            "1.0.0"
        }
    }

    val backupFileName = remember {
        val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        "karma_khodro_backup_$stamp.json"
    }

    val exportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            isWorking = true
            scope.launch {
                try {
                    backupManager.exportBackup(context, uri)
                    Toast.makeText(context, "پشتیبان‌گیری با موفقیت انجام شد", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "خطا در پشتیبان‌گیری: ${e.message}", Toast.LENGTH_LONG).show()
                } finally {
                    isWorking = false
                }
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            isWorking = true
            scope.launch {
                try {
                    backupManager.importBackup(context, uri)
                    Toast.makeText(context, "بازیابی اطلاعات با موفقیت انجام شد", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "خطا در بازیابی: ${e.message}", Toast.LENGTH_LONG).show()
                } finally {
                    isWorking = false
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("تنظیمات") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowForward, contentDescription = "بازگشت")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(KarmaSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(KarmaSpacing.xl)
        ) {
            FormSection(title = "پشتیبان‌گیری و بازیابی اطلاعات") {
                ElevatedCard(
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(KarmaSpacing.lg),
                        verticalArrangement = Arrangement.spacedBy(KarmaSpacing.sm)
                    ) {
                        Text(
                            "از اطلاعات همه خودروها و سرویس‌ها یک فایل پشتیبان تهیه کنید یا فایل قبلی را بازیابی کنید.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(KarmaSpacing.xs))

                        Button(
                            onClick = { exportLauncher.launch(backupFileName) },
                            enabled = !isWorking,
                            shape = MaterialTheme.shapes.large,
                            contentPadding = PaddingValues(vertical = 12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Filled.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("تهیه فایل پشتیبان", maxLines = 1)
                        }

                        OutlinedButton(
                            onClick = { importLauncher.launch(arrayOf("application/json")) },
                            enabled = !isWorking,
                            shape = MaterialTheme.shapes.large,
                            contentPadding = PaddingValues(vertical = 12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Filled.CloudDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("بازیابی از فایل پشتیبان", maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                        }

                        if (isWorking) {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(MaterialTheme.shapes.extraSmall)
                            )
                        }

                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = MaterialTheme.colorScheme.errorContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(KarmaSpacing.sm),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Filled.WarningAmber,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "بازیابی، تمام اطلاعات فعلی برنامه را با اطلاعات فایل پشتیبان جایگزین می‌کند.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            AboutFooter(versionName = versionName)

            Spacer(modifier = Modifier.height(KarmaSpacing.sm))
        }
    }
}

@Composable
private fun AboutFooter(versionName: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.DirectionsCar,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )
                Icon(
                    Icons.Filled.Build,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(KarmaSpacing.sm))
        Text("کارما خودرو", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            "طراح و برنامه‌نویس: کیوان عدیلی",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            "نسخه برنامه: $versionName",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            "به سفارش نمایشگاه خودرو کارما",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
    }
}
