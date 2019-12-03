package com.mvp.myapplication.utils

class ColorUtils {

    companion object {

        fun getColorString(red: Int, green: Int, blue: Int): String {

            if(red < 50 && green < 50 && blue < 50) return "Черная"
            if(red > 200 && green > 200 && blue > 200) return "Белая"

            if(red > green && red > blue) return "Красная"
            if(green > red && green > blue) return "Зеленая"
            if(blue > green && blue > red) return "Синяя"

            return ""
        }

    }

}