package com.xsofty.shared.firebase

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import timber.log.Timber

class FirebaseStorageManager {

    private val storageRef by lazy { Firebase.storage.reference }

    fun imageIdToUrl(imageId: String, onImageLoaded: (String?) -> Unit) {
        val pathRef = storageRef.child(imageId)
        pathRef.downloadUrl.addOnSuccessListener { uri ->
            onImageLoaded(uri.toString())
        }.addOnFailureListener {
            onImageLoaded(null)
        }
    }
}