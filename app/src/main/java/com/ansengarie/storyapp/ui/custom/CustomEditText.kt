package com.ansengarie.storyapp.ui.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText

class CustomEditText : AppCompatEditText {

    private val mInputType = this.inputType

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                val currentText = s.toString()

                when (mInputType) {
                    EMAIL_TYPE -> {
                        if (currentText.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(currentText)
                                .matches()
                        ) {
                            error = "Email Tidak Valid"
                        }
                    }

                    PASS_TYPE -> {
                        if (currentText.length < 6) {
                            error = "Password harus minimal 6 huruf"
                        }
                    }
                }
            }
        })
    }

    companion object {
        const val EMAIL_TYPE = 0x00000021
        const val PASS_TYPE = 0x00000081
    }

}