package com.nifty.cloud.mb.kotlinformapp2

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.WindowManager

import com.nifty.cloud.mb.core.NCMB
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NCMB.initialize(this.applicationContext,
                "「YOUR_NCMB_APPLICATION_KEY」",
                "「YOUR_NCMB_CLIENT_KEY」")

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        // load demo1 fragment default
        title = getString(R.string.demo1_title)
        nav_view.menu.getItem(0).isChecked = true
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content, Demo1Fragment())
        fragmentTransaction.commit()

        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        val fragment: Fragment = when (id) {
            R.id.nav_demo1 -> Demo1Fragment()
            R.id.nav_demo2 -> Demo2Fragment()
            R.id.nav_demo3_1 -> Demo31Fragment()
            R.id.nav_demo3_2 -> Demo32Fragment()
            else -> Demo1Fragment()
        }

        // Insert the fragment by replacing any existing fragment
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit()

        // Highlight the selected item has been done by NavigationView
        item.isChecked = true
        // Set action bar title
        title = item.title
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
