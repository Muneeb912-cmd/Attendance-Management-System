package com.example.attendancemanagement.models.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.attendancemanagement.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class StudentsRepository {
    private val db = FirebaseFirestore.getInstance()
    private val userDataCollection = db.collection("UserData")

    suspend fun addUser(data: User): User? {
        return try {
            val existingUser = checkUserExists(data.userEmail)
            if (existingUser != null) {
                Log.d("FireStore", "User already exists: ${existingUser.userId}")
                existingUser
            } else {
                val document = userDataCollection.document()
                data.userId = document.id
                document.set(data).await()
                Log.d("FireStore", "DocumentSnapshot successfully written!")
                data
            }
        } catch (e: Exception) {
            Log.e("FireStore", "Error writing document", e)
            null
        }
    }

    private suspend fun checkUserExists(email: String): User? {
        return try {
            val documents = userDataCollection.whereEqualTo("userEmail", email).get().await()
            if (documents.isEmpty) {
                null
            } else {
                val user = documents.documents[0].toObject(User::class.java)
                user?.userId = documents.documents[0].id
                user
            }
        } catch (e: Exception) {
            Log.e("FireStore", "Error checking if user exists", e)
            null
        }
    }

    fun getStudentByEmail(studentEmail: String): LiveData<User?> {
        val studentLiveData = MutableLiveData<User?>()

        userDataCollection
            .whereEqualTo("userEmail", studentEmail)
            .limit(1)  // Limit to one document since we expect only one user with a specific email
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("FireStore", "Error fetching document", exception)
                    studentLiveData.value = null
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    try {
                        val document = snapshot.documents[0]
                        val data = document.toObject(User::class.java)
                        data?.apply { userId = document.id }
                        studentLiveData.value = data
                    } catch (e: Exception) {
                        Log.e("FireStore", "Error parsing document ${snapshot.documents[0].id}", e)
                        studentLiveData.value = null
                    }
                } else {
                    studentLiveData.value = null
                }
            }

        return studentLiveData
    }


    fun getRegisteredStudents(): LiveData<List<User>> {
        val studentLiveData = MutableLiveData<List<User>>()
        userDataCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e("FireStore", "Error fetching documents", exception)
                studentLiveData.value = emptyList()
                return@addSnapshotListener
            }
            val tasks = snapshot.documents.mapNotNull { document ->
                try {
                    val data = document.toObject(User::class.java)
                    data?.userId = document.id
                    data
                } catch (e: Exception) {
                    Log.e("FireStore", "Error parsing document ${document.id}", e)
                    null
                }
            }
            studentLiveData.value = tasks
        }
        return studentLiveData
    }

    fun getUnAssignedStudents(): LiveData<List<User>> {
        val studentLiveData = MutableLiveData<List<User>>()
        userDataCollection
            .whereEqualTo("addedToClass", false) // Example filter: fetch users where "assigned" field is false
            .addSnapshotListener { snapshot, exception ->
                if (exception != null || snapshot == null) {
                    Log.e("FireStore", "Error fetching documents", exception)
                    studentLiveData.value = emptyList()
                    return@addSnapshotListener
                }
                val tasks = snapshot.documents.mapNotNull { document ->
                    try {
                        val data = document.toObject(User::class.java)
                        data?.userId = document.id
                        data
                    } catch (e: Exception) {
                        Log.e("FireStore", "Error parsing document ${document.id}", e)
                        null
                    }
                }
                studentLiveData.value = tasks
            }
        return studentLiveData
    }

    fun updateStudentStatus(data: User, status: Boolean): User {
        val document = userDataCollection.document(data.userId)
        val updateMap = mapOf(
            "addedToClass" to status,
        )
        document.update(updateMap)
            .addOnSuccessListener {
                Log.d("FireStore", "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.w("FireStore", "Error updating document", e)
            }
        return data
    }

}
