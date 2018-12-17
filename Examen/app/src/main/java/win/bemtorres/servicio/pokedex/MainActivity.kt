package win.bemtorres.servicio.pokedex

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var fm = supportFragmentManager
        var ft = fm.beginTransaction()

        when (item.itemId) {
            R.id.nav_home -> {
                var miFrag = Fr_home()
                miFrag.miContexto = this
                ft.replace(R.id.fl_content,miFrag)
                ft.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_todos -> {
                var miFrag = Fr_todos()
                miFrag.miContexto = this
                ft.replace(R.id.fl_content,miFrag)
                ft.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_favorito -> {
                var miFrag = Fr_favorito()
                miFrag.miContexto = this
                ft.replace(R.id.fl_content,miFrag)
                ft.commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.itemIconTintList = null

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        //dejar de inicio el Fragment
        var fm = supportFragmentManager
        var ft = fm.beginTransaction()
        var miFrag = Fr_home()
        miFrag.miContexto = this
        ft.replace(R.id.fl_content,miFrag)
        ft.commit()
    }


}
