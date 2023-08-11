package com.example.melon.utils

import com.example.melon.models.Follow
import com.example.melon.models.FollowModel

fun List<FollowModel>.getIds(): ArrayList<String>
{
    return if(this.isNotEmpty()){
        val tempFollowIdList = ArrayList<String>()
        this.forEach {
            tempFollowIdList.add(it.id)
        }
        tempFollowIdList
    }
    else
        ArrayList()
}

fun List<Follow>.getFollowIds(): ArrayList<String>
{
    return if(this.isNotEmpty()){
        val tempFollowIdList = ArrayList<String>()
        this.forEach {
            tempFollowIdList.add(it.id)
        }
        tempFollowIdList
    }
    else
        ArrayList()
}