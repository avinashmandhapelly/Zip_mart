package com.zipmart.app

import android.app.Activity
import android.os.Bundle
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.*
import android.graphics.drawable.GradientDrawable

class MainActivity : Activity() {
    private val prefs by lazy { getSharedPreferences("zipmart_local", Context.MODE_PRIVATE) }
    private var cartCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showCurrentScreen()
    }

    private fun showCurrentScreen() {
        if (prefs.getBoolean("guest_logged_in", false)) showHome() else showWelcome()
    }

    private fun showWelcome() {
        val root = verticalRoot()
        root.gravity = Gravity.CENTER
        root.setPadding(dp(24), dp(24), dp(24), dp(24))

        val logo = text("ZipMart", 36f, Color.WHITE, true)
        logo.gravity = Gravity.CENTER
        val logoBox = FrameLayout(this).apply {
            background = rounded(getColor(R.color.zipmart_purple), 24)
            addView(logo, FrameLayout.LayoutParams(-1, dp(130)))
        }
        root.addView(logoBox, LinearLayout.LayoutParams(-1, dp(130)))

        addSpace(root, 28)
        root.addView(text(getString(R.string.welcome_title), 26f, getColor(R.color.zipmart_text), true))
        addSpace(root, 8)
        root.addView(text(getString(R.string.welcome_subtitle), 16f, getColor(R.color.zipmart_muted)))
        addSpace(root, 30)

        val guest = button(getString(R.string.continue_guest))
        root.addView(guest, LinearLayout.LayoutParams(-1, dp(54)))
        guest.setOnClickListener {
            prefs.edit().putBoolean("guest_logged_in", true).apply()
            showHome()
        }
        addSpace(root, 14)
        val note = text(getString(R.string.sign_in_note), 13f, getColor(R.color.zipmart_muted))
        note.gravity = Gravity.CENTER
        root.addView(note)
        setContentView(root)
    }

    private fun showHome() {
        val root = verticalRoot()
        root.setPadding(dp(18), dp(18), dp(18), dp(18))

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val titleBox = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        titleBox.addView(text(getString(R.string.home_title), 28f, getColor(R.color.zipmart_purple), true))
        titleBox.addView(text(getString(R.string.home_subtitle), 14f, getColor(R.color.zipmart_muted)))
        header.addView(titleBox, LinearLayout.LayoutParams(0, -2, 1f))
        val cart = button("${getString(R.string.cart)} ($cartCount)")
        header.addView(cart)
        cart.setOnClickListener {
            Toast.makeText(this, if (cartCount == 0) getString(R.string.cart_empty) else "Items in cart: $cartCount", Toast.LENGTH_SHORT).show()
        }
        root.addView(header)
        addSpace(root, 18)

        val search = EditText(this).apply {
            hint = getString(R.string.search_hint)
            textSize = 15f
            singleLine = true
            setPadding(dp(14), dp(10), dp(14), dp(10))
            background = rounded(Color.WHITE, 16)
        }
        root.addView(search, LinearLayout.LayoutParams(-1, dp(50)))
        addSpace(root, 18)

        val categories = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        listOf(getString(R.string.category_grocery), getString(R.string.category_fruits),
            getString(R.string.category_dairy), getString(R.string.category_snacks)).forEach { label ->
            val chip = TextView(this).apply {
                text = label
                textSize = 12f
                setTextColor(getColor(R.color.zipmart_purple_dark))
                gravity = Gravity.CENTER
                setPadding(dp(8), dp(10), dp(8), dp(10))
                background = rounded(0xFFEDE5FF.toInt(), 14)
            }
            categories.addView(chip, LinearLayout.LayoutParams(0, dp(42), 1f).apply {
                setMargins(dp(3), 0, dp(3), 0)
            })
        }
        root.addView(categories)
        addSpace(root, 20)
        root.addView(text(getString(R.string.section_popular), 20f, getColor(R.color.zipmart_text), true))
        addSpace(root, 10)

        val products = listOf(
            Triple("Fresh Milk", "500 ml • Dairy", "₹32"),
            Triple("Bananas", "6 pcs • Fruits", "₹40"),
            Triple("Basmati Rice", "1 kg • Grocery", "₹95"),
            Triple("Potato Chips", "Classic • Snacks", "₹20")
        )
        products.forEach { product ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(dp(12), dp(12), dp(12), dp(12))
                background = rounded(Color.WHITE, 16)
            }
            val info = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
            info.addView(text(product.first, 17f, getColor(R.color.zipmart_text), true))
            info.addView(text(product.second, 13f, getColor(R.color.zipmart_muted)))
            info.addView(text(product.third, 16f, getColor(R.color.zipmart_purple), true))
            card.addView(info, LinearLayout.LayoutParams(0, -2, 1f))
            val add = button(getString(R.string.add_to_cart))
            card.addView(add)
            add.setOnClickListener {
                cartCount++
                cart.text = "${getString(R.string.cart)} ($cartCount)"
                Toast.makeText(this, getString(R.string.cart_added), Toast.LENGTH_SHORT).show()
            }
            root.addView(card, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(10) })
        }

        addSpace(root, 8)
        val profile = button(getString(R.string.profile))
        root.addView(profile, LinearLayout.LayoutParams(-1, dp(48)))
        profile.setOnClickListener { showProfile() }
        setContentView(root)
    }

    private fun showProfile() {
        val root = verticalRoot()
        root.setPadding(dp(22), dp(22), dp(22), dp(22))
        root.addView(text(getString(R.string.profile), 28f, getColor(R.color.zipmart_purple), true))
        addSpace(root, 20)
        root.addView(text(getString(R.string.guest_account), 18f, getColor(R.color.zipmart_text), true))
        addSpace(root, 20)
        val back = button("Back to home")
        root.addView(back, LinearLayout.LayoutParams(-1, dp(50)))
        back.setOnClickListener { showHome() }
        addSpace(root, 12)
        val logout = button(getString(R.string.logout))
        root.addView(logout, LinearLayout.LayoutParams(-1, dp(50)))
        logout.setOnClickListener {
            prefs.edit().putBoolean("guest_logged_in", false).apply()
            cartCount = 0
            Toast.makeText(this, getString(R.string.logout_confirm), Toast.LENGTH_SHORT).show()
            showWelcome()
        }
        setContentView(root)
    }

    private fun verticalRoot() = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setBackgroundColor(getColor(R.color.zipmart_background))
    }

    private fun text(value: String, size: Float, color: Int, bold: Boolean = false) = TextView(this).apply {
        text = value
        textSize = size
        setTextColor(color)
        if (bold) typeface = Typeface.DEFAULT_BOLD
    }

    private fun button(label: String) = Button(this).apply {
        text = label
        textSize = 14f
        isAllCaps = false
        setTextColor(Color.WHITE)
        background = rounded(getColor(R.color.zipmart_purple), 14)
        minHeight = dp(44)
        stateListAnimator = null
    }

    private fun rounded(color: Int, radius: Int) = GradientDrawable().apply {
        setColor(color)
        cornerRadius = dp(radius).toFloat()
    }

    private fun addSpace(parent: LinearLayout, height: Int) {
        parent.addView(View(this), LinearLayout.LayoutParams(1, dp(height)))
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
