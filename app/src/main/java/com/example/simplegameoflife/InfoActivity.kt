package com.example.simplegameoflife

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType

class InfoActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!

        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        setTransformer(AppIntroPageTransformerType.Fade)
        showStatusBar(false)

        askForPermissions(
            permissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE),
            slideNumber = 1,
            required = true)
        setTransformer(AppIntroPageTransformerType.Parallax(
            titleParallaxFactor = 1.0,
            imageParallaxFactor = -1.0,
            descriptionParallaxFactor = 2.0
        ))
        addSlide(
            AppIntroFragment.newInstance(
                title = resources.getString(R.string.intro_welcome),
                description = resources.getString(R.string.сonways_model),
                imageDrawable = R.drawable.cell,
                titleColor = Color.WHITE,
                descriptionColor = Color.WHITE,
                backgroundColor = Color.BLACK
        ))
        addSlide(
            AppIntroFragment.newInstance(
                title = resources.getString(R.string.what_is_the_game_of_life),
                description = resources.getString(R.string.game_of_life_description),
                imageDrawable = R.drawable.conway,
                titleColor = Color.WHITE,
                descriptionColor = Color.WHITE,
                backgroundColor = Color.BLACK
            ))
        addSlide(
            AppIntroFragment.newInstance(
                title = resources.getString(R.string.game_rules),
                description = resources.getString(R.string.rules_speak),
                imageDrawable = R.drawable.rules,
                titleColor = Color.WHITE,
                descriptionColor = Color.WHITE,
                backgroundColor = Color.BLACK
            ))

    }
    private fun launchMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        launchMainActivity()
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        launchMainActivity()
        finish()
    }
}