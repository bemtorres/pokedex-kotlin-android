package win.bemtorres.servicio.pokedex

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.view.View
import android.widget.EditText
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_buscar_pokemon.*
import okhttp3.*
import win.bemtorres.servicio.pokedex.adapter.AdapterBuscar
import win.bemtorres.servicio.pokedex.adapter.AdapterTodos
import win.bemtorres.servicio.pokedex.model.Pokedex
import win.bemtorres.servicio.pokedex.model.Pokemon
import win.bemtorres.servicio.pokedex.model.Result
import java.io.IOException

class BuscarPokemon : AppCompatActivity(){
    val cantPokemones = 948
    var listaPokemones: Pokedex? = null

    var listaPoke: ArrayList<Result> = ArrayList()

    var lista: MutableList<Result> = mutableListOf()
    lateinit var toolbar : Toolbar
    lateinit var recyclerView : RecyclerView
    lateinit var layoutManager : RecyclerView.LayoutManager

    lateinit var adapterBuscar : AdapterBuscar

    lateinit var searchView: SearchView
    var busca : SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_pokemon)
       // listaPoke = intent.getIntExtra("lista",0) as ArrayList<Result>

       // listaPoke =  this.intent.getParcelableArrayListExtra<Parcelable>("extraextra") as ArrayList<Result>

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.visibility = View.INVISIBLE

        recyclerView = findViewById(R.id.rv_Buscar)

        layoutManager =  GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager


        recyclerView.setHasFixedSize(true)
        obtenerJson()



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.buscar,menu)
        var item : MenuItem = menu!!.findItem(R.id.barra_buscador)

        searchView = item.actionView as SearchView

        //searchView = MenuItemCompat.getActionView(item) as SearchView
        MenuItemCompat.setOnActionExpandListener(item,object : MenuItemCompat.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                toolbar.setBackgroundColor(Color.WHITE)
                (searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as EditText).setHintTextColor(Color.BLACK)
                return true
            }


            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                toolbar.setBackgroundColor(ContextCompat.getColor(this@BuscarPokemon,R.color.colorPokedexRojo))
                searchView.setQuery("",false)
                return true
            }

        })
        searchView.maxWidth = Int.MAX_VALUE
        buscarNombre(searchView)
        return true
    }

    private fun buscarNombre(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
               return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterBuscar.filter.filter(newText)
                return true
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId==R.id.barra_buscador){
            return  true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!searchView.isIconified){
            searchView.isIconified = true
            return
        }
        super.onBackPressed()
    }

    private fun obtenerJson(){
        val url = "https://pokeapi.co/api/v2/pokemon/"
        val request = Request.Builder().url(url).build()
        val poke = OkHttpClient()

        poke.newCall(request).enqueue(object:Callback{
            override fun onFailure(call: Call?, e: IOException?) {
                Log.e("Request poke", "no se puedo obtener Pokemones" )
            }

            override fun onResponse(call: Call?, response: Response?) {
                val respuesta = response?.body()?.string()
                val gson = GsonBuilder().create()

                try {
                    val pokem = gson.fromJson(respuesta,Pokedex::class.java)
                    listaPokemones = pokem
                    listaPoke.clear()
                    for (n in 1..cantPokemones){
                        var i = n-1
                        var id = listaPokemones!!.results[i].url.replace("https://pokeapi.co/api/v2/pokemon/","")
                        id = id.replace("/","")
                        listaPoke.add(Result(id.toInt(),listaPokemones!!.results[i].name,"" ))
                    }
                }
                catch(ex : Exception){
                    Log.e("json invalido","${ex.message}")
                }
                cargado()

            }
        } )

    }
    private fun ordenar(lista : ArrayList<Result>): ArrayList<Result>{
        val ordenada = ArrayList(lista.sortedBy { it.id })
        return  ordenada
    }


    private fun cargado(){
        this.runOnUiThread{
            listaPoke = ordenar(listaPoke)

            var cont : Int = 0
            for(poke in listaPoke){
                var p : Result = listaPoke[cont]
                lista.add(p)
                cont+=1
            }
            Log.e("cant poke", "${listaPoke.size}" )
            Log.e("cant lista", "${lista.size}" )

            adapterBuscar = AdapterBuscar(lista,this)

            recyclerView.adapter = adapterBuscar
            pb_2.visibility = View.GONE
            toolbar.visibility = View.VISIBLE
        }
    }
}




