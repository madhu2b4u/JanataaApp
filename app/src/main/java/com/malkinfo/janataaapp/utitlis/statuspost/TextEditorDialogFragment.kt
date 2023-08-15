package com.malkinfo.janataaapp.utitlis.statuspost

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.malkinfo.janataaapp.R


/**
 * ------------------------------------------
 * Created by Farida Shekh.
 * This Community App Home Page Fragment.
 * ------------------------------------------
 */
class TextEditorDialogFragment : DialogFragment() {
    private var mAddTextEditText: EditText? = null
    private var mInputMethodManager: InputMethodManager? = null
    private var mColorCode = 0
    private var mTextEditor: TextEditor? = null

    interface TextEditor {
        fun onDone(inputText: String?, colorCode: Int)
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        //Make dialog full screen with transparent background
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.add_text_dialog, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAddTextEditText = view.findViewById(R.id.add_text_edit_text)
        mInputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        val mAddTextDoneTextView = view.findViewById<TextView>(R.id.add_text_done_tv)
        val colorPickerViewDialog: VerticalSlideColorPicker =
            view.findViewById(R.id.color_picker_view_dialog)
        colorPickerViewDialog.setOnColorChangeListener(object : VerticalSlideColorPicker.OnColorChangeListener{
            override fun onColorChange(selectedColor: Int) {
                mColorCode = selectedColor
                mAddTextEditText!!.setTextColor(selectedColor)
            }
        })
        mAddTextEditText!!.setText(requireArguments().getString(EXTRA_INPUT_TEXT))
        mColorCode = requireArguments().getInt(EXTRA_COLOR_CODE)
        mAddTextEditText!!.setTextColor(mColorCode)
        mInputMethodManager!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        //Make a callback on activity when user is done with text editing
        mAddTextDoneTextView.setOnClickListener { view1 ->
            mInputMethodManager!!.hideSoftInputFromWindow(view1.windowToken, 0)
            this@TextEditorDialogFragment.dismiss()
            val inputText = mAddTextEditText!!.text.toString()
            if (!TextUtils.isEmpty(inputText) && mTextEditor != null) {
                mTextEditor!!.onDone(inputText, mColorCode)
            }
        }
    }

    //Callback to listener if user is done with text editing
    fun setOnTextEditorListener(textEditor: TextEditor?) {
        mTextEditor = textEditor
    }

    companion object {
        val TAG = TextEditorDialogFragment::class.java.simpleName
        const val EXTRA_INPUT_TEXT = "extra_input_text"
        const val EXTRA_COLOR_CODE = "extra_color_code"

        //Show dialog with provide text and text color
        fun show(
            @NonNull fragmentManager: FragmentManager,
            inputText: String?,
            @ColorInt colorCode: Int
        ): TextEditorDialogFragment {
            val args = Bundle()
            args.putString(EXTRA_INPUT_TEXT, inputText)
            args.putInt(EXTRA_COLOR_CODE, colorCode)
            val fragment = TextEditorDialogFragment()
            fragment.setArguments(args)
            fragment.show(fragmentManager, TAG)
            return fragment
        }

        //Show dialog with default text input as empty and text color white
        fun show(@NonNull fragmentManager: FragmentManager,context: Context): TextEditorDialogFragment {
            return show(
                fragmentManager,
                "", ContextCompat.getColor(context,R.color.white)
            )
        }
    }
}