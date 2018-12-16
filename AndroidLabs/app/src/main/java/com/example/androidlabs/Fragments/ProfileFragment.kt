package com.example.androidlabs.Fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.androidlabs.Models.Profile
import com.example.androidlabs.Protocols.IProfileService
import com.example.androidlabs.R
import kotlinx.android.synthetic.main.profile_fragment.*
import java.io.File

class ProfileFragment: Fragment() {

    private var profileService: IProfileService? = null

    private var isChangeMode : Boolean = false

    private lateinit var editViews : List<EditText>
    private lateinit var textViews : List<TextView>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is IProfileService) {
            profileService = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editViews = listOf<EditText>(surnameEditView, nameEditView, phoneEditView,
                emailEditView)
        textViews = listOf<TextView>(surnameTextView, nameTextView, phoneTextView,
                emailTextView)

        take_photo.setOnClickListener {
            profileService?.askPermissionForUsingCamera()
            updateProfileView()
        }

        choose_photo.setOnClickListener {
            profileService?.askPermissionForUsingGallery()
            updateProfileView()
        }

        changeProfileButton.setOnClickListener {
            isChangeMode = !isChangeMode
            if (isChangeMode) changeProfile()
            else saveProfile()
            updateProfileView()
        }

        sign_out.setOnClickListener {
            profileService?.signOut()
        }

        updateProfileView()
    }

    private fun updateProfileView() {
        val newProfile = profileService?.getProfile()
            if (newProfile != null) {
                nameTextView.text =
                        if (newProfile.name == "") activity?.getString(R.string.no_information)
                        else newProfile.name

                nameEditView.text = Editable.Factory.getInstance()
                        .newEditable(newProfile.name)

                nameEditView.hint =
                        if (newProfile.name == "") activity?.getString(R.string.no_information) else ""

                surnameTextView.text =
                        if (newProfile.surname == "") activity?.getString(R.string.no_information)
                        else newProfile.surname

                surnameEditView.text = Editable.Factory.getInstance()
                        .newEditable(newProfile.surname)

                surnameEditView.hint =
                        if (newProfile.surname == "") activity?.getString(R.string.no_information) else ""

                phoneTextView.text =
                        if (newProfile.phone == "") activity?.getString(R.string.no_information)
                        else newProfile.phone

                phoneEditView.text = Editable.Factory.getInstance()
                        .newEditable(newProfile.phone)

                phoneEditView.hint =
                        if (newProfile.phone == "") activity?.getString(R.string.no_information) else ""

                emailTextView.text =
                        if (newProfile.email == "") activity?.getString(R.string.no_information)
                        else newProfile.email

                emailEditView.text = Editable.Factory.getInstance()
                        .newEditable(newProfile.email)

                emailEditView.hint =
                        if (newProfile.email == "") activity?.getString(R.string.no_information) else ""

                profilePhoto.setImageBitmap(getBitmap(newProfile.imagePath))
            }
    }

    private fun getBitmap(filePath: String): Bitmap {
        val file = File(filePath)

        if (!file.exists()) return BitmapFactory.decodeResource(activity?.resources,
                R.drawable.profile)

        return BitmapFactory.decodeFile(filePath)
    }

    private fun changeProfile() {
        for (editView in editViews)
            editView.visibility = View.VISIBLE

        for (textView in textViews)
            textView.visibility = View.GONE

        //profilePhotoEditImage.visibility = View.VISIBLE
        take_photo.visibility = View.VISIBLE
        choose_photo.visibility = View.VISIBLE
        sign_out.visibility = View.GONE
        changeProfileButton.setImageResource(R.drawable.ic_chat)
    }

    private fun saveProfile() {
        for (editView in editViews)
            editView.visibility = View.GONE

        for (textView in textViews)
            textView.visibility = View.VISIBLE

        //profilePhotoEditImage.visibility = View.INVISIBLE
        take_photo.visibility = View.GONE
        choose_photo.visibility = View.GONE
        sign_out.visibility = View.VISIBLE
        changeProfileButton.setImageResource(R.drawable.ic_edit)

        val profile = Profile(surnameEditView.text.toString(), nameEditView.text.toString(),
                emailEditView.text.toString(), phoneEditView.text.toString())
        profileService?.saveProfileInfo(profile)
    }
}