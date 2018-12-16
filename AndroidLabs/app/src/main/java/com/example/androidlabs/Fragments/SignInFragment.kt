package com.example.androidlabs.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.androidlabs.Protocols.ISignIn
import com.example.androidlabs.R
import kotlinx.android.synthetic.main.sign_in_fragment.*
import android.app.ProgressDialog
import android.widget.ProgressBar


class SignInFragment: Fragment() {

    private var signInService : ISignIn? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sign_in_fragment, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is ISignIn)
            signInService = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpButton.setOnClickListener {
            val action = SignInFragmentDirections.actionDestinationSignInToDestinationSignUp()
            Navigation.findNavController(it).navigate(action)
        }

        signInButton.setOnClickListener {

            val progressDialog = ProgressBar(this.activity)
            progressDialog.isIndeterminate = true
            progressDialog.progress

            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()

            val isSignedIn = signInService?.signIn(login, password)
            if (isSignedIn != null && !isSignedIn){
                signInError.visibility = View.VISIBLE
            }
            else {
                signInError.visibility = View.INVISIBLE
            }
        }
    }
}