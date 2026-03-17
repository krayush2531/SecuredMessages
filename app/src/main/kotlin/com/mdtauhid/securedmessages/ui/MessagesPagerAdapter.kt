package com.mdtauhid.securedmessages.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mdtauhid.securedmessages.parser.SmsCategory

class MessagesPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    data class TabInfo(val title: String, val category: SmsCategory?)

    val tabs = listOf(
        TabInfo("All", null),
        TabInfo("Promotional", SmsCategory.PROMOTIONAL),
        TabInfo("Service", SmsCategory.SERVICE),
        TabInfo("Government", SmsCategory.GOVERNMENT),
        TabInfo("Transactions", SmsCategory.TRANSACTIONAL),
        TabInfo("Implicit", SmsCategory.IMPLICIT),
        TabInfo("Spam", SmsCategory.SPAM)
    )

    override fun getItemCount(): Int = tabs.size

    override fun createFragment(position: Int): Fragment =
        MessageFragment.newInstance(tabs[position].category)
}
