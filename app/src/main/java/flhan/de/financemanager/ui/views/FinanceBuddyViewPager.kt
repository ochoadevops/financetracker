package flhan.de.financemanager.ui.views

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent

class FinanceBuddyViewPager : ViewPager {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    var isPagingEnabled: Boolean = false

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return this.isPagingEnabled && super.onTouchEvent(ev)
    }

    override fun executeKeyEvent(event: KeyEvent): Boolean {
        return isPagingEnabled && super.executeKeyEvent(event)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return this.isPagingEnabled && super.onInterceptTouchEvent(ev)
    }
}