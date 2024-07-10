package com.example.attendancemanagement.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class StudentsRepository {
    private val db = FirebaseFirestore.getInstance()
    private val userDataCollection = db.collection("UserData")

    fun addUser(data: User): User {
        val document = userDataCollection.document()
        data.userId = document.id
        document.set(data)
            .addOnSuccessListener {
                Log.d("FireStore", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w("FireStore", "Error writing document", e)
            }
        return data
    }

    fun getStudentById(studentId: Int): LiveData<User?> {
        val studentLiveData = MutableLiveData<User?>()

        userDataCollection
            .whereEqualTo("userId", studentId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null || snapshot == null) {
                    Log.e("FireStore", "Error fetching document", exception)
                    studentLiveData.value = null
                    return@addSnapshotListener
                }

                val student = if (snapshot.documents.isNotEmpty()) {
                    try {
                        val document = snapshot.documents[0]
                        val data = document.toObject(User::class.java)
                        data?.apply { userId = document.id }
                    } catch (e: Exception) {
                        Log.e("FireStore", "Error parsing document ${snapshot.documents[0].id}", e)
                        null
                    }
                } else {
                    null
                }

                studentLiveData.value = student
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

    fun updateStudentStatus(data: User,status:Boolean): User {
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
