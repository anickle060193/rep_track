package com.adamnickle.demo.injection.module

import com.adamnickle.demo.ui.post.PostListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule
{
    @ContributesAndroidInjector
    abstract fun contributePostListActivity(): PostListActivity
}