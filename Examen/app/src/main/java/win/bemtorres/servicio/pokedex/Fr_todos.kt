package win.bemtorres.servicio.pokedex


import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.*
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_fr_todos.*
import okhttp3.*
import win.bemtorres.servicio.pokedex.adapter.AdapterTodos
import win.bemtorres.servicio.pokedex.model.Pokedex
import win.bemtorres.servicio.pokedex.model.Pokemon
import win.bemtorres.servicio.pokedex.model.Result
import java.io.IOException
import kotlin.concurrent.thread


class Fr_todos : Fragment() {

    //Cantidad de pokemones a mostrar
    val cantPokemones = 948

    var miContexto : Context?= null
    var listaPokemones: Pokedex? = null
    var listaPoke: ArrayList<Result> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_fr_todos, container, false)

        var boton : ImageView = view.findViewById(R.id.imgSincronizar)
        var imagen1 : ImageView =view.findViewById(R.id.imgCargando)
        var texto1 : TextView = view.findViewById(R.id.lblCargado1)
        var texto2 : TextView = view.findViewById(R.id.lblCargado2)
        var pbCargando : ProgressBar = view.findViewById(R.id.progressBar)
        var btnBuscar : ImageView = view.findViewById(R.id.imgBuscar)

        listaPokemones = null
        //listaPoke.removeAll(listaPoke)
        listaPoke.clear()

        obtenerJson()

        if (!isConnected(miContexto!!)){
            invocarSnackBar(miContexto!!,view)
        }else{
            imagen1.visibility = View.VISIBLE
            texto1.visibility = View.VISIBLE
            texto2.visibility = View.GONE
            pbCargando.visibility = View.VISIBLE
        }

        boton.setOnClickListener {
            obtenerJson()
            if (!isConnected(miContexto!!)){
                invocarSnackBar(miContexto!!,view)
            }else{
                imagen1.visibility = View.VISIBLE
                texto1.visibility = View.VISIBLE
                texto2.visibility = View.VISIBLE
                pbCargando.visibility = View.VISIBLE
            }
            cargado()
        }

        btnBuscar.setOnClickListener {
            var intento: Intent = Intent(miContexto,BuscarPokemon::class.java)

            ContextCompat.startActivity(miContexto!!, intento, null)        }

        return view
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

    private fun cargado(){
        try{
            activity!!.runOnUiThread{
                listaPoke = ordenar(listaPoke)//ordenada
                var adapterTodos = AdapterTodos(miContexto!!,listaPoke)
                rv_todos.layoutManager = GridLayoutManager(miContexto, 3)
                rv_todos.adapter = adapterTodos
                if(listaPoke.size>0){
                    progressBar.visibility = View.GONE
                    imgCargando.visibility = View.GONE
                    lblCargado1.visibility = View.GONE
                    lblCargado2.visibility = View.GONE
                    imgBuscar.visibility = View.VISIBLE
                }
            }
        }catch (ex : Exception){
            Log.e("Cargado", "Error fun cargado")
        }



    }

    private fun ordenar(lista : ArrayList<Result>): ArrayList<Result>{
        val ordenada = ArrayList(lista.sortedBy { it.id })
        return  ordenada
    }

    private fun isConnected(context: Context):Boolean{
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork !=null && activeNetwork.isConnectedOrConnecting
    }

    private fun invocarSnackBar(conext: Context, view : View){
        var snackbar = Snackbar.make(view.findViewById(R.id.Frame_todo),"Sin Internet",Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("Reintentar"){
            if(!isConnected(miContexto!!)){
                invocarSnackBar(conext,view)
            }
        }
        snackbar.setActionTextColor(Color.WHITE)
        snackbar.show()
    }

    override fun onResume() {
        listaPoke = ordenar(listaPoke)//ordenada
        var adapterTodos = AdapterTodos(miContexto!!,listaPoke)
        //rv_todos.layoutManager = LinearLayoutManager(miContexto, LinearLayout.VERTICAL,false)
        rv_todos.layoutManager = GridLayoutManager(miContexto, 3)
        rv_todos.adapter = adapterTodos
        super.onResume()
        //Log.e("on Resume","aca")
    }
}


