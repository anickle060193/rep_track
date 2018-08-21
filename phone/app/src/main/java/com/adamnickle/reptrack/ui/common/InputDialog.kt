package com.adamnickle.reptrack.ui.common

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.InputDialogBinding

object InputDialog
{
    fun showCapWordsInputDialog( context: Context, title: String, onInput: ( String, DialogInterface, EditText ) -> Unit )
    {
        showInputDialog( context, title, InputType.TYPE_TEXT_FLAG_CAP_WORDS, onInput )
    }

    fun showSentencesInputDialog( context: Context, title: String, onInput: ( String, DialogInterface, EditText ) -> Unit )
    {
        showInputDialog( context, title, InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, onInput )
    }

    private fun showInputDialog( context: Context, title: String, inputType: Int, onInput: ( String, DialogInterface, EditText ) -> Unit )
    {
        showDialog( context, title, R.layout.input_dialog, object: OnShowDialogListener<InputDialogBinding>()
        {
            override fun onBind( binding: InputDialogBinding )
            {
                binding.inputType = inputType
            }

            override fun onShow( binding: InputDialogBinding, dialog: Dialog )
            {
                dialog.window?.setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE )
            }

            override fun onPositiveButtonClick( binding: InputDialogBinding, dialogInterface: DialogInterface )
            {
                val text = binding.input.text.toString()
                onInput( text, dialogInterface, binding.input )
            }
        } )
    }

    fun <T: ViewDataBinding> showDialog( context: Context, title: String, @LayoutRes layout: Int, onPositiveButtonClickCallback: ( T, DialogInterface ) -> Unit )
    {
        showDialog( context, title, layout, object: OnShowDialogListener<T>()
        {
            override fun onPositiveButtonClick( binding: T, dialogInterface: DialogInterface )
            {
                onPositiveButtonClickCallback( binding, dialogInterface )
            }
        } )
    }

    abstract class OnShowDialogListener<T: ViewDataBinding>
    {
        open fun onBind( binding: T ) { }
        open fun onShow( binding: T, dialog: Dialog ) { }
        open fun onPositiveButtonClick( binding: T, dialogInterface: DialogInterface ) { }
    }

    fun <T : ViewDataBinding> showDialog( context: Context, title: String, @LayoutRes layout: Int, listener: OnShowDialogListener<T> )
    {
        val binding = DataBindingUtil
                .inflate<T>(
                        LayoutInflater.from( context ),
                        layout,
                        null,
                        false
                )

        listener.onBind( binding )

        AlertDialog.Builder( context )
                .setTitle( title )
                .setView( binding.root )
                .setPositiveButton( android.R.string.ok, null )
                .setNegativeButton( android.R.string.cancel, null )
                .create()
                .also { dialog ->
                    dialog.setOnShowListener { dialogInterface ->
                        listener.onShow( binding, dialog )

                        dialog.getButton( AlertDialog.BUTTON_POSITIVE ).setOnClickListener {
                            listener.onPositiveButtonClick( binding, dialogInterface )
                        }
                    }
                }
                .show()
    }
}