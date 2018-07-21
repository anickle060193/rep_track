package com.adamnickle.demo.ui.post

import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.adamnickle.demo.R
import com.adamnickle.demo.base.BaseViewModel
import com.adamnickle.demo.model.Post
import com.adamnickle.demo.network.PostApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PostListViewModel: BaseViewModel()
{
    @Inject
    lateinit var postApi: PostApi

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadPosts() }
    val postListAdapter: PostListAdapter = PostListAdapter()

    private lateinit var subscription: Disposable

    init
    {
        loadPosts()
    }

    override fun onCleared()
    {
        super.onCleared()

        subscription.dispose()
    }

    private fun loadPosts()
    {
        subscription = postApi.getPosts()
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .doOnSubscribe { onRetrievePostListStart() }
                .doOnTerminate { onRetrievePostListFinish() }
                .subscribe(
                        { result -> onRetrievePostListSuccess( result ) },
                        { onRetrievePostListError() }
                )
    }

    private fun onRetrievePostListStart()
    {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrievePostListFinish()
    {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListSuccess( postList: List<Post> )
    {
    postListAdapter.updatePostList( postList )
    }

    private fun onRetrievePostListError()
    {
        errorMessage.value = R.string.post_error
    }
}