package com.mvp.myapplication.utils

import com.mvp.myapplication.data.models.api.LangListModel

class LangList() {

    val langList = ArrayList<LangListModel>()

    init {
        langList.add(LangListModel("Anorak","Куртка"))
        langList.add(LangListModel("Blazer","Пиджак"))
        langList.add(LangListModel("Blouse","Блузка"))
        langList.add(LangListModel("Bomber","Бомбер"))
        langList.add(LangListModel("Button-Down","Рубашка"))
        langList.add(LangListModel("Cardigan","Кардиган"))
        langList.add(LangListModel("Flannel","Фланель"))
        langList.add(LangListModel("Halter","Лиф-Халтер"))
        langList.add(LangListModel("Henley","Хенли"))
        langList.add(LangListModel("Hoodie","Толстовка"))
        langList.add(LangListModel("Jacket","Куртка"))
        langList.add(LangListModel("Jersey","Джерси"))
        langList.add(LangListModel("Parka","Парка"))
        langList.add(LangListModel("Poncho","Пончо"))
        langList.add(LangListModel("Sweater","Свитер"))
        langList.add(LangListModel("Tee","Тройка"))
        langList.add(LangListModel("Top","Топ"))
        langList.add(LangListModel("Turtleneck","Водолазка"))
        langList.add(LangListModel("Culottes","Юбка-Брюки"))
        langList.add(LangListModel("Cutoffs","Подрезанные"))
        langList.add(LangListModel("Gauchos","Гаучо"))
        langList.add(LangListModel("Jeans","Джинсы"))
        langList.add(LangListModel("Jeggings","Джегинсы"))
        langList.add(LangListModel("Jodhpurs","Брюки для верховой езды"))
        langList.add(LangListModel("Joggers","Для бега"))
        langList.add(LangListModel("Leggings","Лосины"))
        langList.add(LangListModel("Sarong","Саронг"))
        langList.add(LangListModel("Shorts","Шорты"))
        langList.add(LangListModel("Skirt","Юбка"))
        langList.add(LangListModel("Sweatpants","Спортивные штаны"))
        langList.add(LangListModel("Sweatshorts","Джемпер"))
        langList.add(LangListModel("Trunks","Пляжные шорты"))
        langList.add(LangListModel("Caftan","Кардиган"))
        langList.add(LangListModel("Cape","Пончо"))
        langList.add(LangListModel("Coat","Пальто"))
        langList.add(LangListModel("Coverup","Прикрыть"))
        langList.add(LangListModel("Dress","Платье"))
        langList.add(LangListModel("Jumpsuit","Комбинезон"))
        langList.add(LangListModel("Kaftan","Кардиган"))
        langList.add(LangListModel("Kimono","Кимоно"))
        langList.add(LangListModel("Nightdress","Ночннушка"))
        langList.add(LangListModel("Onesie","Onesie"))
        langList.add(LangListModel("Robe","Халат"))
        langList.add(LangListModel("Romper","Ползунки"))
        langList.add(LangListModel("Shirtdress","Рубашка-Платье"))
    }

    fun getList() = langList

    fun getRus(eng: String): String {
        langList.forEach {
            if(it.eng.toUpperCase() == eng.toUpperCase()) return it.rus
        }
        return eng
    }

    fun isset(eng: String): Boolean {
        langList.forEach {
            if(it.eng.toUpperCase() == eng.toUpperCase()) return true
        }
        return false
    }
}