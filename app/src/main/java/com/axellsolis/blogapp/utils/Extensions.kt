package com.axellsolis.blogapp.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun TextInputEditText.isEmpty(): Boolean {
    return if (text.isNullOrEmpty()) {
        (parent.parent as TextInputLayout).isEndIconVisible = false
        error = "Empty Field"
        true
    } else {
        error = null
        (parent.parent as TextInputLayout).isEndIconVisible = true
        clearFocus()
        false
    }
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}