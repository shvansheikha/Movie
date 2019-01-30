package kr.zagros.shwan.moviemvvm.views.CostumView

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.Log


class LimitedChatTextView : AppCompatTextView {

    var maxTextChar: Int = 0

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun setText(text: CharSequence, type: BufferType) {
        var limitedText = text
        if (maxTextChar < text.length) limitedText = text.toString().substring(0, maxTextChar - 1) + "..."
        Log.d(TAG, "setText: $limitedText")
        super.setText(limitedText, type)
    }

    companion object {
        private val TAG = "LimitedChatTextView"
    }

}
