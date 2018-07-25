package com.adamnickle.reptrack.ui.common

import android.content.Context
import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.EditText
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.InputDialogBinding

object InputDialog
{
    fun showInputDialog( context: Context, title: String, onInput: ( String, DialogInterface, EditText ) -> Unit )
    {
        val inputBinding = DataBindingUtil
                .inflate<InputDialogBinding>(
                        LayoutInflater.from( context ),
                        R.layout.input_dialog,
                        null,
                        false
                )

        AlertDialog.Builder( context )
                .setTitle( title )
                .setView( inputBinding.root )
                .setPositiveButton( android.R.string.ok, null )
                .setNegativeButton( android.R.string.cancel, null )
                .create()
                .also { dialog ->
                    dialog.window.setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE )
                    dialog.setOnShowListener {
                        dialog.getButton( AlertDialog.BUTTON_POSITIVE ).setOnClickListener {
                            val text = inputBinding.input.text.toString()
                            onInput( text, dialog, inputBinding.input )
                        }
                    }
                }
                .show()
    }
}