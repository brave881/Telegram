package com.example.telegram.ui.fragments

import android.os.Bundle
import android.view.*
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.example.telegram.R
import com.example.telegram.databinding.FragmentSettingsBinding
import com.example.telegram.utilites.*


class SettingsFragment : BaseFragment(R.layout.fragment_settings) {
    private lateinit var binding: FragmentSettingsBinding


    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        val uri = result.originalUri
        val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
            .child(CURRENT_UID)

        if (uri != null) {
            putImageToStorage(uri, path) {
                getUrlFromStorage(path) {
                    pathUrlToDatabase(it) {
                        binding.photoUser.photoDownloadAndSet(it)
                        USER.photoUrl = it
                        showToast(getString(R.string.update_data_toastt))
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                    }
                }
            }
        }

    }


    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
    }

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initFields() {
        binding.settingsBtnUsername.setOnClickListener {
            replaceFragment(ChangeUsernameFragment())
        }
        binding.settingsBtnBio.setOnClickListener {
            replaceFragment(ChangeBioFragment())
        }
        binding.settingsPhotoChange.setOnClickListener {
            changePhoto()
        }

        binding.settingsBioText.text = USER.bio
        binding.settingsNumber.text = USER.phone
        binding.settingsUsername.text = USER.username
        binding.settingsNameProfile.text = USER.fullname
        binding.statusName.text = USER.state
        binding.photoUser.photoDownloadAndSet(USER.photoUrl)
    }


    private fun changePhoto() {
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
                setCropShape(CropImageView.CropShape.OVAL)
                setRequestedSize(600, 600)
                setAspectRatio(1, 1)
            }
        )
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_action_menu_logout -> {
                AUTH.signOut()
                AppStates.updateState(AppStates.OFFLINE)
                restartActivity()
            }
            R.id.settings_action_menu_edit_name -> replaceFragment(ChangeNameFragment())
        }
        return true
    }

}
