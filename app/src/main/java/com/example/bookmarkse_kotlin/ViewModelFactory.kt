package com.example.bookmarkse_kotlin

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookmarkse_kotlin.addeditbookmark.AddEditBookmarkViewModel
import com.example.bookmarkse_kotlin.bookmark.BookmarkViewModel
import com.example.bookmarkse_kotlin.bookmarkdetail.BookmarkDetailViewModel
import com.example.bookmarkse_kotlin.data.Injection
import com.example.bookmarkse_kotlin.data.source.ItemsRepository
import com.example.bookmarkse_kotlin.deletebookmark.DeleteBookmarkViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory private constructor(
    private val itemsRepository: ItemsRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
           when {
               isAssignableFrom(BookmarkViewModel::class.java) ->
                   BookmarkViewModel(itemsRepository)
               isAssignableFrom(AddEditBookmarkViewModel::class.java) ->
                   AddEditBookmarkViewModel(itemsRepository)
               isAssignableFrom(BookmarkDetailViewModel::class.java) ->
                   BookmarkDetailViewModel(itemsRepository)
               isAssignableFrom(DeleteBookmarkViewModel::class.java) ->
                   DeleteBookmarkViewModel(itemsRepository)
               else ->
                   throw IllegalArgumentException("Unknown ViewModel Class: ${modelClass.name}")
           }
        } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideBookmarkRepository(application.applicationContext)
                ).also { INSTANCE = it }
            }

        @VisibleForTesting fun destroyInstance() {
            INSTANCE = null
        }
    }
}