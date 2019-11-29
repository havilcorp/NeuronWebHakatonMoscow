package com.mvp.myapplication.data.models.menu

import androidx.fragment.app.Fragment

data class ModelMenu (
    var fragment: Fragment = Fragment(),
    var name: String = "",
    var title: String = ""
)