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
}
