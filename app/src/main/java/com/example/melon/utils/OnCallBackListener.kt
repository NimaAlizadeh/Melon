package com.example.melon.utils

import javax.inject.Inject

interface OnCallBackListener{
    fun onClickListener()
    fun setOnRefreshClickListener(callBack: OnCallBackListener)
}