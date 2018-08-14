package com.adamnickle.reptrack.ui.common

import android.content.Context
import android.content.DialogInterface
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.EditText
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.InputDialogBinding

object InputDialog
{
    fun showInputDialog( context: Context, title: String, onInput: ( String, DialogInterface, EditText ) -> Unit )
    {
        showDialog<InputDialogBinding>( context, title, R.layout.input_dialog ) { binding, dialogInterface ->
            val text = binding.input.text.toString()
            onInput( text, dialogInterface, binding.input )
        }
    }

    fun <T : ViewDataBinding> showDialog( context: Context, title: String, @LayoutRes layout: Int, onPositiveButtonClick: ( T, DialogInterface ) -> Unit )
    {
        val binding = DataBindingUtil
                .inflate<T>(
                        LayoutInflater.from( context ),
                        layout,
                        null,
                        false
                )

        AlertDialog.Builder( context )
                .setTitle( title )
                .setView( binding.root )
                .setPositiveButton( android.R.string.ok, null )
                .setNegativeButton( android.R.string.cancel, null )
                .create()
                .also { dialog ->
                    dialog.window.setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE )
                    dialog.setOnShowListener { dialogInterface ->
                        dialog.getButton( AlertDialog.BUTTON_POSITIVE ).setOnClickListener {
                            onPositiveButtonClick( binding, dialogInterface )
                        }
                    }
                }
                .show()
    }
}