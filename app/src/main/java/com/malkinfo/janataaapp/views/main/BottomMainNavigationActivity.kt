package com.malkinfo.janataaapp.views.main

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.janataaapp.adapters.TabRecyclerAdapter
import com.malkinfo.janataaapp.models.BottomTabItem
import com.malkinfo.janataaapp.views.base.MyAppCompatActivity
import java.util.ArrayList

//Classes that extend this should call initBottomNavigation(recyclerView)
abstract class BottomNavigationActivity : MyAppCompatActivity(), TabRecyclerAdapter.OnTabButtonSelector {

    protected var mTabRecyclerAdapter: TabRecyclerAdapter? = null
    private var mTabItems: ArrayList<BottomTabItem>? = null
    private val mTabRV: RecyclerView? = null
    var selectedPos = 0

    //Initialize bottom tab
    protected fun initBottomNavigation(mTabRV: RecyclerView, tabItems: ArrayList<BottomTabItem>) {
        this.mTabItems = tabItems
        val gridLayoutManager = GridLayoutManager(this, tabItems.size)
        mTabRV.setHasFixedSize(true)
        mTabRV.layoutManager = gridLayoutManager
        mTabRecyclerAdapter = TabRecyclerAdapter(this, tabItems, this, selectedPos)
        mTabRV.adapter = mTabRecyclerAdapter
    }

    override fun onTabSelected(position: Int) {
        tabItemSelected(position)
    }

    //Abstract method for tab item selected
    protected abstract fun tabItemSelected(position: Int)
}
