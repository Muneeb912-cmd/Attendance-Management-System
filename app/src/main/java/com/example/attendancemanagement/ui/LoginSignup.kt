package com.example.attendancemanagement.ui

import StudentListAdapter
import android.app.Activity
import android.app.DirectAction
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.attendancemanagement.R
import com.example.attendancemanagement.models.StudentsRepository
import com.example.attendancemanagement.models.User
import com.example.attendancemanagement.view_model.StudentsViewModel
import com.example.attendancemanagement.view_model.StudentsViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginSignup : Fragment() {

    private lateinit var auth: FirebaseAuth //firebase service
    private lateinit var googleSignInClient: GoogleSignInClient //part of the Google Sign-In library,
    private val tag = "LoginSignup"
    private lateinit var studentsViewModel: StudentsViewModel

    private val signInLauncher = registerForActivityResult( //used to start an activity and get results
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        //This code is used to initiate the Google Sign-In process and handle its result.
        val data = result.data
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                handleSignInResult(account)
            } catch (e: ApiException) {
                Log.w(tag, "Google sign in failed", e)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = StudentsRepository()
        val factory = StudentsViewModelFactory(repository)
        studentsViewModel = ViewModelProvider(this, factory)[StudentsViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login__signup, container, false)
        val loginButton=view.findViewById<Button>(R.id.login)
        val loginWithGoogle = view.findViewById<Button>(R.id.signInWithGoogle)
        val email=view.findViewById<EditText>(R.id.email)
        val password=view.findViewById<EditText>(R.id.password)

        auth = FirebaseAuth.getInstance()

        val gso = googleSignInOptions()

        // requireContext() Return the Context this fragment is currently associated with.
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        loginWithGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            signInLauncher.launch(signInIntent)
        }

        loginButton.setOnClickListener{
            handleLoginClicked(email, password)
        }


        return view
    }

    private fun handleLoginClicked(email: EditText, password: EditText) {
        if (email.text.toString()=="admin@myschool.com" && password.text.toString()=="12345") {
            Toast.makeText(requireContext(), "Welcome Admin!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_login_Signup_to_attendanceMain22)
        } else {
            Toast.makeText(requireContext(), "Invalid Credentials!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun googleSignInOptions() =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.server_client_id))
            .build()

    private fun handleSignInResult(account: GoogleSignInAccount?) {
        try {
            account?.idToken?.let {
                firebaseAuthWithGoogle(it)
            } ?: run {
                Log.w(tag, "Google sign in failed: ID token is null")
                updateUI(null)
            }
        } catch (e: ApiException) {
            Log.w(tag, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(tag, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(requireContext(),"Welcome ${user.displayName}!",Toast.LENGTH_SHORT).show()
            val studentsRepository=StudentsRepository()
            val studentsViewModel=StudentsViewModel(studentsRepository)
            val userData=User(
                userId = user.uid,
                userName = user.displayName.toString(),
                userEmail = user.email.toString(),
                userPhoto =user.photoUrl.toString(),
            )


            studentsViewModel.addUser(userData)

            val action = LoginSignupDirections.actionLoginSignupToStudentDetails(userData)
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(),"User Not Authenticated!",Toast.LENGTH_SHORT).show()
        }
    }


}
