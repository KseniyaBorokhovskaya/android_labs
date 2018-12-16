package com.example.androidlabs.Protocols

interface IAboutService {
    fun getImei(): String?
    fun getVersion(): String
}