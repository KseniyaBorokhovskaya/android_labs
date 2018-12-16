package com.example.androidlabs.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidlabs.Protocols.ISignUp
import com.example.androidlabs.R
import kotlinx.android.synthetic.main.sign_up_fragment.*

class SignUpFragment: Fragment() {

    private var signUpService : ISignUp? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sign_up_fragment, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is ISignUp)
            signUpService = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerButton.setOnClickListener {
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (password == confirmPassword) {
                val isRegistered = signUpService?.signUp(login, password)
                if (isRegistered != null && !isRegistered) {
                    registerError.text = getString(R.string.existing_user)
                    registerError.visibility = View.VISIBLE
                }
                else {
                    registerError.visibility = View.INVISIBLE
                }
            }
            else {
                registerError.text = getString(R.string.wrong_password)
                registerError.visibility = View.VISIBLE
            }
        }
    }
}