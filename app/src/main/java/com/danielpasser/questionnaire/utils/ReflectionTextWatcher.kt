package com.danielpasser.questionnaire.utils

import android.text.TextWatcher

import android.widget.EditText
import java.lang.Exception
import java.lang.reflect.Field


class ReflectionTextWatcher {
  companion object {
      fun removeAll(editText: EditText) {
          try {
              val field: Field? = findField("mListeners", editText.javaClass)
              if (field != null) {
                  field.isAccessible = true
                  val list = field.get(editText) as ArrayList<TextWatcher> //IllegalAccessException
                  list?.clear()
              }
          } catch (e: Exception) {
              e.printStackTrace()
          }
      }
      private fun findField(name: String, type: Class<*>): Field? {
          for (declaredField in type.declaredFields) {
              if (declaredField.name.equals(name)) {
                  return declaredField
              }
          }
          return if (type.superclass != null) {
              findField(name, type.superclass)
          } else null
      }
  }

}