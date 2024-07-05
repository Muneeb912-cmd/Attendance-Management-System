package com.example.attendancemanagement.ui

import android.app.Activity
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
import androidx.navigation.fragment.findNavController
import com.example.attendancemanagement.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginSignup : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                handleSignInResult(account)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
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
        if (email.text.toString()=="m@m.com" && password.text.toString()=="12345") {
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
                Log.w(TAG, "Google sign in failed: ID token is null")
                updateUI(null)
            }
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
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
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(requireContext(),"Welcome ${user.displayName}!",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_login_Signup_to_studentDetails)

        } else {
            Toast.makeText(requireContext(),"User Not Authenticated!",Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "LoginSignup"
    }
}
