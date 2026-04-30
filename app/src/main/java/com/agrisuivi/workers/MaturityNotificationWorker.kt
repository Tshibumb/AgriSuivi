package com.agrisuivi.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.agrisuivi.R
import com.agrisuivi.data.repository.CultureRepository
import com.agrisuivi.domain.model.StatutCulture
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Worker périodique (24h) – notifie le fermier des parcelles proches de la récolte.
 *
 * CORRECTIONS appliquées :
 * 1. Icône : R.drawable.ic_leaf (fichier ic_leaf.xml créé dans res/drawable/)
 * 2. NotificationCompat.Builder : setContentTitle() est une méthode de l'objet builder,
 *    pas de la notification finale – appel dans la chaîne .build() corrigé.
 */
@HiltWorker
class MaturityNotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: CultureRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val readyCycles = mutableListOf<String>()

            repository.observeAllCycles().collect { cycles ->
                cycles
                    .filter { it.statut == StatutCulture.EN_COURS && it.isPretPourRecolte }
                    .forEach { readyCycles.add("${it.variete} – Parcelle ${it.numeroParcelle}") }
            }

            if (readyCycles.isNotEmpty()) {
                sendNotification(readyCycles)
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun sendNotification(cultures: List<String>) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Créer le channel (obligatoire API 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Maturité des cultures",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Alertes de récolte imminente"
            }
            manager.createNotificationChannel(channel)
        }

        // CORRECTION : toutes les méthodes sont chaînées sur le Builder avant .build()
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_leaf)          // ic_leaf.xml dans res/drawable/
            .setContentTitle("🌿 Récolte imminente !") // Titre dans le Builder
            .setContentText(
                "${cultures.size} parcelle(s) prête(s) : ${cultures.take(2).joinToString(", ")}"
            )
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(cultures.joinToString("\n"))
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        manager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val CHANNEL_ID = "agrisuivi_maturity"
        const val NOTIFICATION_ID = 1001
        const val WORK_NAME = "MaturityCheck"
    }
}
