package com.example.androidlabs.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ContentFrameLayout
import androidx.fragment.app.Fragment
import com.example.androidlabs.Protocols.IAboutService
import com.example.androidlabs.R
import kotlinx.android.synthetic.main.about_fragment.*

class AboutFragment: Fragment() {

    private var aboutService: IAboutService? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.about_fragment, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is IAboutService) {
            aboutService = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        IMEI.text = aboutService?.getImei() ?: getString(R.string.no_information)
        version.text = aboutService?.getVersion()
    }



}