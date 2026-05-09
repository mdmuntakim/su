package su.android.view

import android.app.Activity
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import su.android.BR
import su.android.R
import su.android.arch.UIActivity
import su.android.databinding.DialogMagiskBaseBinding
import su.android.databinding.DiffItem
import su.android.databinding.ItemWrapper
import su.android.databinding.ObservableHost
import su.android.databinding.RvItem
import su.android.databinding.bindExtra
import su.android.databinding.set
import su.android.databinding.setAdapter
import com.google.android.material.color.MaterialColors
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.CornerFamily

typealias DialogButtonClickListener = (DialogInterface) -> Unit

class MagiskDialog(
    context: Activity, theme: Int = 0
) : android.support.v7.app.AppCompatDialog(context, theme) {

    private val binding: DialogMagiskBaseBinding =
        DialogMagiskBaseBinding.inflate(LayoutInflater.from(context))
    private val data = Data()

    val activity: UIActivity<*> get() = ownerActivity as UIActivity<*>

    init {
        binding.setVariable(BR.data, data)
        setCancelable(true)
        setOwnerActivity(context)
    }

    inner class Data : ObservableHost {
        override var callbacks: android.databinding.PropertyChangeRegistry? = null

        @get:Bindable
        var icon: Drawable? = null
            set(value) = set(value, field, { field = it }, BR.icon)

        @get:Bindable
        var title: CharSequence = ""
            set(value) = set(value, field, { field = it }, BR.title)

        @get:Bindable
        var message: CharSequence = ""
            set(value) = set(value, field, { field = it }, BR.message)

        val buttonPositive = ButtonViewModel()
        val buttonNeutral = ButtonViewModel()
        val buttonNegative = ButtonViewModel()
    }

    enum class ButtonType {
        POSITIVE, NEUTRAL, NEGATIVE
    }

    interface Button {
        var icon: Int
        var text: Any
        var isEnabled: Boolean
        var doNotDismiss: Boolean
        var textColor: Int
        fun onClick(listener: DialogButtonClickListener)
    }

    inner class ButtonViewModel : Button, ObservableHost {
        override var callbacks: android.databinding.PropertyChangeRegistry? = null

        @get:Bindable
        override var icon = 0
            set(value) = set(value, field, { field = it }, BR.icon, BR.gone)

        @get:Bindable
        var message: String = ""
            set(value) = set(value, field, { field = it }, BR.message, BR.gone)

        @get:Bindable
        override var textColor: Int = Color.BLUE
            set(value) = set(value, field, { field = it }, BR.textColor)

        override var text: Any
            get() = message
            set(value) {
                message = when (value) {
                    is Int -> context.getText(value)
                    else -> value
                }.toString()
            }

        @get:Bindable
        val gone get() = icon == 0 && message.isEmpty()

        @get:Bindable
        override var isEnabled = true
            set(value) = set(value, field, { field = it }, BR.enabled)

        override var doNotDismiss = false

        private var onClickAction: DialogButtonClickListener = {}

        override fun onClick(listener: DialogButtonClickListener) {
            onClickAction = listener
        }

        fun clicked() {
            onClickAction(this@MagiskDialog)
            if (!doNotDismiss) {
                dismiss()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(binding.root)

        val surfaceColor = Color.parseColor("#F2F2F2")
        val materialShapeDrawable = MaterialShapeDrawable().apply {
            fillColor = ColorStateList.valueOf(surfaceColor)
            shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, 45f)
                .build()
        }

        val margin = (context.resources.displayMetrics.widthPixels * 0.15).toInt()
        window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)
            setBackgroundDrawable(InsetDrawable(materialShapeDrawable, margin, 0, margin, 0))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
        }
    }

    override fun setTitle(titleId: Int) { data.title = context.getString(titleId) }

    override fun setTitle(title: CharSequence?) { data.title = title ?: "" }

    fun setMessage(msgId: Int, vararg args: Any) {
        data.message = context.getString(msgId, *args)
    }

    fun setMessage(message: CharSequence) { data.message = message }

    fun setIcon(drawableRes: Int) {
        data.icon = android.support.v7.content.res.AppCompatResources.getDrawable(context, drawableRes)
    }

    fun setIcon(drawable: Drawable) { data.icon = drawable }

    fun setButton(buttonType: ButtonType, builder: Button.() -> Unit) {
        val button = when (buttonType) {
            ButtonType.POSITIVE -> data.buttonPositive
            ButtonType.NEUTRAL -> data.buttonNeutral
            ButtonType.NEGATIVE -> data.buttonNegative
        }
        button.apply(builder)
    }

    fun setView(view: View) {
        binding.dialogBaseContainer.removeAllViews()
        binding.dialogBaseContainer.addView(
            view,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    fun resetButtons() {
        ButtonType.values().forEach {
            setButton(it) {
                text = ""
                icon = 0
                isEnabled = true
                doNotDismiss = false
                textColor = Color.BLUE
                onClick {}
            }
        }
    }

    @Deprecated("Please use setView(view)", level = DeprecationLevel.ERROR)
    override fun setContentView(layoutResID: Int) {}
    @Deprecated("Please use setView(view)", level = DeprecationLevel.ERROR)
    override fun setContentView(view: View) {}
    @Deprecated("Please use setView(view)", level = DeprecationLevel.ERROR)
    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {}
}