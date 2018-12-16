package com.example.androidlabs.Protocols

interface ISignIn {
    fun signIn(login : String, password: String) : Boolean
}