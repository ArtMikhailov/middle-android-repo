package com.example.androidpracticumcustomview.ui.theme

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout

private const val ALPHA_ANIMATION_DURATION = 2000L
private const val TRANSLATION_ANIMATION_DURATION = 5000L

class CustomContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var topView: View? = null
    private var bottomView: View? = null
    private val startTopViewAnimationHandler = Handler(Looper.getMainLooper())
    private val startBottomViewAnimationHandler = Handler(Looper.getMainLooper())
    private var isAnimationEnabled = true

    fun setAnimationEnabled(enabled: Boolean) {
        isAnimationEnabled = enabled
    }

    /*
    We handle children in onLayout instead of addView.
    This way we can handle added views in xml as well as programmatically.
     */
    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        super.onLayout(changed, left, top, right, bottom)
        if (childCount > 2) {
            throw IllegalStateException("CustomContainer can only have two child views.")
        }
        val childZero = getChildAt(0)
        val childOne = getChildAt(1)
        if (topView == null && childZero != null) {
            topView = childZero
            placeChild(childZero, isTopView = true)
        }
        if (bottomView == null && childOne != null) {
            bottomView = childOne
            placeChild(childOne, isTopView = false)
        }
    }

    private fun placeChild(child: View, isTopView: Boolean) {
        child.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = if (isAnimationEnabled) {
                Gravity.CENTER
            } else {
                if (isTopView) Gravity.TOP or Gravity.CENTER_HORIZONTAL
                else Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            }
        }
        if (isAnimationEnabled) {
            child.alpha = 0f
            if (isTopView) {
                startTopViewAnimationHandler.postDelayed({
                    animateTopView()
                }, 10)
            } else {
                startBottomViewAnimationHandler.postDelayed({
                    animateBottomView()
                }, 10)
            }
        }
    }

    private fun animateTopView() {
        val topView = this.topView ?: return
        val verticalCenterPosition = calculateVerticalCenterPosition()
        val viewHeight = topView.height
        val translationYBy: Float = - (verticalCenterPosition - (viewHeight / 2))
        animateView(topView, translationYBy)
    }

    private fun animateBottomView() {
        val bottomView = this.bottomView ?: return
        val verticalCenterPosition = calculateVerticalCenterPosition()
        val viewHeight = bottomView.height
        val translationYBy: Float = verticalCenterPosition - (viewHeight / 2)
        animateView(bottomView, translationYBy)
    }

    private fun calculateVerticalCenterPosition(): Float {
        return (this.height - this.paddingTop - this.paddingBottom) / 2.toFloat()
    }

    private fun animateView(view: View, translationYBy: Float) {
        view.animate()
            .alpha(1.0f)
            .setDuration(ALPHA_ANIMATION_DURATION)
            .start()
        view.animate()
            .translationYBy(translationYBy)
            .setDuration(TRANSLATION_ANIMATION_DURATION)
            .start()
    }

    override fun addView(child: View) {
        if (childCount >= 2) {
            throw IllegalStateException("CustomContainer can only have two child views.")
        }
        super.addView(child)
    }

    override fun onViewRemoved(child: View?) {
        super.onViewRemoved(child)
        if (child == topView) {
            startTopViewAnimationHandler.removeCallbacksAndMessages(null)
            topView = null
        } else if (child == bottomView) {
            startBottomViewAnimationHandler.removeCallbacksAndMessages(null)
            bottomView = null
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        startTopViewAnimationHandler.removeCallbacksAndMessages(null)
        startBottomViewAnimationHandler.removeCallbacksAndMessages(null)
    }
}