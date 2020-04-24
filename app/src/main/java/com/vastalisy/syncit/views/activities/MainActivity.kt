package com.vastalisy.syncit.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.work.*
import com.google.firebase.firestore.CollectionReference
import com.vastalisy.syncit.*
import com.vastalisy.syncit.databinding.ActivityMainBinding
import com.vastalisy.syncit.feature_sync.CloudSyncWorker
import com.vastalisy.syncit.feature_sync.NoteSyncWorkRequest
import com.vastalisy.syncit.feature_sync.SyncNotification
import com.vastalisy.syncit.model.Note
import com.vastalisy.syncit.views.fragments.NoteEditor
import com.vastalisy.syncit.views.fragments.NotesList


class MainActivity : AppCompatActivity(), NavigationListener {
    private lateinit var binding: ActivityMainBinding
    private val bundle = Bundle(1)

    override fun onEditorButtonClicked(note: Note?) {
        val arg: Bundle? = note?.run {
            bundle.apply {
                putInt(NOTE_ID, id!!)
                putString(NOTE_TITLE, title)
                putString(NOTE_TEXT, text)
                putString(NOTE_DATE, date)
            }
        }
        openFragment(NoteEditor(), NOTE_EDITOR, arg)
    }

    override fun onSettingButtonClicked() {
        openActivity(AppSettingActivity::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onPostResume() {
        super.onPostResume()

        if (hasNoFragmentAttached()) {
            openFragment(NotesList(), NOTE_LIST, null)
        }

    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)

        when (fragment) {
            is NotesList -> fragment.navigationActionListener = this
        }

    }

    private fun hasNoFragmentAttached(): Boolean {
        return supportFragmentManager.findFragmentById(binding.fragmentContainer.id) == null
    }

    private fun openFragment(fragment: Fragment, tag: String, arg: Bundle?) {
        val transaction = supportFragmentManager.beginTransaction()

        fragment.arguments = arg
        if (fragment is NotesList) {
            transaction.add(binding.fragmentContainer.id, fragment, tag).commit()
        } else {
            val last = supportFragmentManager.fragments[supportFragmentManager.fragments.size - 1]
            transaction.addToBackStack(tag)
            transaction.hide(last)
            transaction.add(binding.fragmentContainer.id, fragment, tag).commit()
        }


    }

    private fun openActivity(kClass: Class<*>, requestCode: Int? = null) {
        requestCode?.let { code ->
            startActivityForResult(Intent(this, kClass), code)
        } ?: startActivity(Intent(this, kClass))
    }

}
