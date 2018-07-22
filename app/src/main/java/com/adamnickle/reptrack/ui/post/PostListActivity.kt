package com.adamnickle.reptrack.ui.post

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import com.adamnickle.reptrack.BuildConfig
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.ActivityPostListBinding
import com.adamnickle.reptrack.ui.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class PostListActivity: DaggerAppCompatActivity()
{
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: ActivityPostListBinding
    private lateinit var viewModel: PostListViewModel

    private var errorSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate( savedInstanceState )

        binding = DataBindingUtil.setContentView( this, R.layout.activity_post_list)
        binding.postList.layoutManager = LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false )

        viewModel = ViewModelProviders.of( this, viewModelFactory ).get( PostListViewModel::class.java )
        viewModel.errorMessage.observe( this, Observer { errorMessage ->
            if( errorMessage != null )
            {
                showError( errorMessage )
            }
            else
            {
                hideError()
            }
        } )
        binding.viewModel = viewModel

        if( BuildConfig.DEBUG )
        {
            window.addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON )
        }
    }

    private fun showError( @StringRes errorMessage: Int )
    {
        val snackbar = Snackbar.make( binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE )
        snackbar.setAction( R.string.retry, viewModel.errorClickListener )
        snackbar.show()

        errorSnackbar = snackbar
    }

    private fun hideError()
    {
        errorSnackbar?.dismiss()
    }
}