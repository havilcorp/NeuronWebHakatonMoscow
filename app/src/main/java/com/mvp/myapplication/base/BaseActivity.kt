package com.mvp.myapplication.base

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mvp.myapplication.R
import com.mvp.myapplication.mvp.IMvpView
import com.mvp.myapplication.utils.AnimUtils

abstract class BaseActivity: AppCompatActivity(), IMvpView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    abstract fun inject()

    override fun startActivity(clazz: Class<*>, isFinish: Boolean, intent: Intent) {
        intent.setClass(this, clazz)
        startActivity(intent)
        if(isFinish) finish()
    }

    override fun startActivityFResult(intent: Intent, code: Int) {
        startActivityForResult(intent, code)
    }

    override fun startActivityFResult(clazz: Class<*>, code: Int) {
        val intent = Intent()
        intent.setClass(this, clazz)
        startActivityForResult(intent, code)
    }

    override fun onActivityResultOk(intent: Intent) {
        setResult(Activity.RESULT_OK, intent)
        //onActivityResult(requestCode, resultCode, intent)
    }

    override fun backView() {
        finish()
    }

    override fun setBackView() {
        //findViewById<View>(R.id.header_back).setOnClickListener {
        //    backView()
        //}
    }

    override fun hideKayboard(){
        val view = findViewById<View>(android.R.id.content)
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun message(message: String?) {
        message?.apply { toast( message ) }
    }

    override fun showError(message: String?) {
        message?.apply { toast( message ) }
    }

    override fun toast(data: Any){
        Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun toBuffer(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("", text)
        //clipboard.primaryClip = clip
    }

    override fun showProgress() {
        val progress = findViewById<View>(R.id.progress)
        if(progress.visibility != View.VISIBLE) {
            progress.visibility = View.VISIBLE
            progress.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in))
            progress.setOnClickListener { }
        }
    }

    override fun hideProgress() {
        val progress = findViewById<View>(R.id.progress)
        if(progress.visibility != View.GONE) {
            progress.startAnimation(AnimUtils(this).animListener( R.anim.abc_fade_out) {
                progress.visibility = View.GONE
            })
        }
    }

}