package ru.netology.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FCMService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        println("Token: $token")
        // eqiAYZTDRjq94-Lqz_bz4_:APA91bFwVSy2OsMW0HWB3sEveEteRUmLNOOA3teKJL3UJt76eg1a1MwOkg-ivSq2QaLPVhEE129Fv1EA6bq6cc2ZgIaFfa9QUgPkFjCJK12Lm0emauIZuRc
    }
    override fun onMessageReceived(message: RemoteMessage) {
        println("Message: $message")
    }
}
