package win.bemtorres.servicio.pokedex


import android.app.DownloadManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_fr_todos.*
import okhttp3.*
import win.bemtorres.servicio.pokedex.adapter.AdapterTodos
import win.bemtorres.servicio.pokedex.model.Pokedex
import win.bemtorres.servicio.pokedex.model.Result
import java.io.IOException
import kotlin.concurrent.thread


class Fr_todos : Fragment() {

    var miContexto : Context?= null
    var listaPokemones: ArrayList<Result> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_fr_todos, container, false)

        var boton : Button = view.findViewById(R.id.btnActulizarTodos)
        var lista : RecyclerView = view.findViewById(R.id.rv_todos)

        boton.setOnClickListener {
            val url = "https://pokeapi.co/api/v2/pokemon/"
            val request = Request.Builder().url(url).build()
            val poke = OkHttpClient()

            poke.newCall(request).enqueue(object:Callback{
                override fun onFailure(call: Call?, e: IOException?) {
                    //Toast.makeText(miContexto,"Error no pudo obtener Pokemones",Toast.LENGTH_LONG).show()
                    Log.e("Request", "no se puedo obtener Pokemones")
                }

                override fun onResponse(call: Call?, response: Response?) {
                    val respuesta = response?.body()?.string()
                    val gson = GsonBuilder().create()

                    try {
                        val poke = gson.fromJson(respuesta,Pokedex::class.java)
                        listaPokemones = poke.results

                        //llamar a cada URL
                        //Guardar en un lista nueva
                        //Adapter de los pokemones


                        cargado()
                    }
                    catch(ex : Exception){
                        Log.e("json invalido","${ex.message}")
                    }
                }
            } )
        }


        return view
    }
    fun cargado(){

        activity!!.runOnUiThread{
            val adapterTodos = AdapterTodos(miContexto!!,listaPokemones)
            rv_todos.layoutManager = LinearLayoutManager(miContexto, LinearLayout.VERTICAL,false)
            rv_todos.adapter = adapterTodos
            Toast.makeText(miContexto,"${listaPokemones.size}",Toast.LENGTH_SHORT).show()
        }

    }
}

