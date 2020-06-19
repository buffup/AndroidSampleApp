package com.buffup.app.ui.splashactivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.buffup.app.R
import com.buffup.app.model.SignInResult
import com.buffup.app.ui.streamactivity.StreamActivity
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent

class SplashActivity : AppCompatActivity(), KoinComponent {

    private val viewModel: SplashViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        observeViewModel()
        setClickListener()
    }


    private fun setClickListener() {
        guestButton.setOnClickListener {
            viewModel.signInAsGuest()
        }
    }

    private fun observeViewModel() {
        viewModel.signInResult.observe(this, Observer { signInResult ->
            when (signInResult) {
                is SignInResult.Loading -> onLoading()
                is SignInResult.Success -> onSuccess()
                is SignInResult.Error -> onError(signInResult.error)
            }
        })
    }

    private fun onError(error: Throwable?) {
        progressBar.visibility = INVISIBLE
        guestButton.visibility = VISIBLE
        Log.w("Sign in", error)
        Toast.makeText(applicationContext, getString(R.string.data_error), Toast.LENGTH_LONG).show()
    }

    private fun onSuccess() {
        progressBar.visibility = INVISIBLE
        guestButton.visibility = VISIBLE
        startActivity(Intent(this, StreamActivity::class.java))
    }

    private fun onLoading() {
        progressBar.visibility = VISIBLE
        guestButton.visibility = INVISIBLE
    }

}
