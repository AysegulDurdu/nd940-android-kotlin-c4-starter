package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.locationreminders.RemindersActivity
import kotlinx.android.synthetic.main.activity_authentication.*

class AuthenticationActivity : AppCompatActivity() {

    private val viewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)


        val buttonLogin = findViewById<Button>(R.id.buttonLogin)



        observe()

    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(), SIGN_IN_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                Log.i(TAG, "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
            } else {
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

    fun observe(){
        viewModel.authenticationState.observe(this, Observer {
            when (it) {
                AuthenticationViewModel.AuthenticationState.AUTHENTICATED -> {
                    val intent = Intent(this@AuthenticationActivity, RemindersActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                AuthenticationViewModel.AuthenticationState.UNAUTHENTICATED -> {
                    buttonLogin.setOnClickListener {
                        launchSignInFlow()
                    }
                    Log.e(TAG, "unauthenticated !")
                }
            }
        })

    }

    companion object {
        val SIGN_IN_REQUEST_CODE = 1001
        val TAG: String = AuthenticationActivity::class.java.simpleName
    }
}
