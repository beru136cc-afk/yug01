package com.example.yugved4.utils

import android.util.Log
import com.example.yugved4.models.Doctor
import com.example.yugved4.models.UserProfile
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Helper class for Firebase Firestore operations
 */
object FirestoreHelper {
    
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private const val COLLECTION_USERS = "users"
    private const val SUBCOLLECTION_PROFILE = "profile"
    private const val SUBCOLLECTION_DOCTORS = "doctors"
    private const val DOCUMENT_DATA = "data"
    private const val TAG = "FirestoreHelper"
    
    /**
     * Save user profile to Firestore
     * @param uid Firebase user ID
     * @param userData Map of user data to save
     */
    suspend fun saveUserProfile(uid: String, userData: Map<String, Any>) {
        try {
            db.collection(COLLECTION_USERS)
                .document(uid)
                .set(userData)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
    
    /**
     * Get user profile from Firestore
     * @param uid Firebase user ID
     * @return Map of user data or null if not found
     */
    suspend fun getUserProfile(uid: String): Map<String, Any>? {
        return try {
            val document = db.collection(COLLECTION_USERS)
                .document(uid)
                .get()
                .await()
            
            if (document.exists()) {
                document.data
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Check if user profile exists in Firestore
     * @param uid Firebase user ID
     * @return true if profile exists, false otherwise
     */
    suspend fun hasUserProfile(uid: String): Boolean {
        return try {
            val document = db.collection(COLLECTION_USERS)
                .document(uid)
                .get()
                .await()
            document.exists()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Update specific fields in user profile
     * @param uid Firebase user ID
     * @param updates Map of fields to update
     */
    suspend fun updateUserProfile(uid: String, updates: Map<String, Any>) {
        try {
            db.collection(COLLECTION_USERS)
                .document(uid)
                .update(updates)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
    
    // =============== DETAILED PROFILE SYNC ===============
    
    /**
     * Sync detailed user profile to Firestore (subcollection structure)
     * @param uid Firebase user ID
     * @param profile UserProfile object
     */
    suspend fun syncUserProfileToFirestore(uid: String, profile: UserProfile) {
        try {
            val profileData = hashMapOf(
                "name" to profile.name,
                "age" to profile.age,
                "height" to profile.height,
                "weight" to profile.currentWeight,
                "targetCalories" to profile.targetCalories,
                "gender" to profile.gender,
                "activityLevel" to profile.activityLevel,
                "dietPreference" to profile.dietPreference,
                "firebaseUid" to profile.firebaseUid,
                "updatedAt" to FieldValue.serverTimestamp()
            )
            
            db.collection(COLLECTION_USERS)
                .document(uid)
                .collection(SUBCOLLECTION_PROFILE)
                .document(DOCUMENT_DATA)
                .set(profileData)
                .await()
            
            Log.d(TAG, "Profile synced to Firestore for user: $uid")
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing profile to Firestore", e)
            // Don't throw - allow app to continue with local data
        }
    }
    
    /**
     * Load user profile from Firestore
     * @param uid Firebase user ID
     * @return UserProfile object or null if not found
     */
    suspend fun loadUserProfileFromFirestore(uid: String): UserProfile? {
        return try {
            val document = db.collection(COLLECTION_USERS)
                .document(uid)
                .collection(SUBCOLLECTION_PROFILE)
                .document(DOCUMENT_DATA)
                .get()
                .await()
            
            if (document.exists()) {
                UserProfile(
                    id = 1, // Local DB will override this
                    name = document.getString("name"),
                    age = document.getLong("age")?.toInt(),
                    height = document.getDouble("height"),
                    currentWeight = document.getDouble("weight") ?: 0.0,
                    targetCalories = document.getLong("targetCalories")?.toInt() ?: 0,
                    gender = document.getString("gender"),
                    activityLevel = document.getString("activityLevel"),
                    dietPreference = document.getString("dietPreference"),
                    firebaseUid = document.getString("firebaseUid")
                )
            } else {
                Log.d(TAG, "No profile found in Firestore for user: $uid")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading profile from Firestore", e)
            null
        }
    }
    
    // =============== DOCTOR SYNC ===============
    
    /**
     * Sync a single doctor to Firestore
     * @param uid Firebase user ID
     * @param doctor Doctor object
     */
    suspend fun syncDoctorToFirestore(uid: String, doctor: Doctor) {
        try {
            val doctorData = hashMapOf(
                "name" to doctor.name,
                "specialty" to doctor.specialty,
                "phoneNumber" to doctor.phoneNumber,
                "email" to (doctor.email ?: ""),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            
            db.collection(COLLECTION_USERS)
                .document(uid)
                .collection(SUBCOLLECTION_DOCTORS)
                .document(doctor.id)
                .set(doctorData)
                .await()
            
            Log.d(TAG, "Doctor synced to Firestore: ${doctor.name}")
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing doctor to Firestore", e)
        }
    }
    
    /**
     * Sync all doctors to Firestore (batch operation)
     * @param uid Firebase user ID
     * @param doctors List of Doctor objects
     */
    suspend fun syncDoctorsToFirestore(uid: String, doctors: List<Doctor>) {
        try {
            for (doctor in doctors) {
                syncDoctorToFirestore(uid, doctor)
            }
            Log.d(TAG, "All doctors synced to Firestore (${doctors.size} doctors)")
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing doctors to Firestore", e)
        }
    }
    
    /**
     * Load all doctors from Firestore
     * @param uid Firebase user ID
     * @return List of Doctor objects
     */
    suspend fun loadDoctorsFromFirestore(uid: String): List<Doctor> {
        return try {
            val querySnapshot = db.collection(COLLECTION_USERS)
                .document(uid)
                .collection(SUBCOLLECTION_DOCTORS)
                .get()
                .await()
            
            val doctors = mutableListOf<Doctor>()
            for (document in querySnapshot.documents) {
                val doctor = Doctor(
                    id = document.id,
                    name = document.getString("name") ?: "",
                    specialty = document.getString("specialty") ?: "",
                    phoneNumber = document.getString("phoneNumber") ?: "",
                    email = document.getString("email")
                )
                doctors.add(doctor)
            }
            
            Log.d(TAG, "Loaded ${doctors.size} doctors from Firestore")
            doctors
        } catch (e: Exception) {
            Log.e(TAG, "Error loading doctors from Firestore", e)
            emptyList()
        }
    }
    
    /**
     * Delete a doctor from Firestore
     * @param uid Firebase user ID
     * @param doctorId Doctor ID to delete
     */
    suspend fun deleteDoctorFromFirestore(uid: String, doctorId: String) {
        try {
            db.collection(COLLECTION_USERS)
                .document(uid)
                .collection(SUBCOLLECTION_DOCTORS)
                .document(doctorId)
                .delete()
                .await()
            
            Log.d(TAG, "Doctor deleted from Firestore: $doctorId")
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting doctor from Firestore", e)
        }
    }
}

