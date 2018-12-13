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
import win.bemtorres.servicio.pokedex.model.Pokemon
import win.bemtorres.servicio.pokedex.model.Result
import java.io.IOException
import kotlin.concurrent.thread


class Fr_todos : Fragment() {

    var miContexto : Context?= null
    var listaPokemones: ArrayList<Result> = ArrayList()
    var listaPokemonesCompleta : ArrayList<Pokemon> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_fr_todos, container, false)

        var boton : Button = view.findViewById(R.id.btnActulizarTodos)
        var lista : RecyclerView = view.findViewById(R.id.rv_todos)



        //Metodo 1
        /*
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
                        /*
                        var cont=0

                        for (r in listaPokemones){
                            cont += 1

                            val url2 = r.url
                            val request2 = Request.Builder().url(url2).build()
                            val poke2 = OkHttpClient()

                            poke2.newCall(request2).enqueue(object:Callback{
                                override fun onFailure(call: Call?, e: IOException?) {
                                    Log.e("Request error", "interior de pokemones ${r.name}")
                                }

                                override fun onResponse(call: Call?, response: Response?) {
                                    val respuesta2 = response?.body()?.string()
                                    val gson2 = GsonBuilder().create()
                                    val poke2: Pokemon = gson2.fromJson(respuesta2,Pokemon::class.java)
                                    Log.e("Encontrado", " Numero ${poke2.abilities[0].ability.name}")
                                    listaPoke.add(poke2)
                                    Log.e("Request 2 ", "$cont | listapoke = ${listaPoke.size}")
                                }

                            })
                        }

                        */
                        cargado()
                    }
                    catch(ex : Exception){
                        Log.e("json invalido","${ex.message}")
                    }
                }
            } )
        }
         */

        //Metodo 2
        listaPokemonesCompleta.removeAll(listaPokemonesCompleta)
        for(num in 1..127){
            val url = "https://pokeapi.co/api/v2/pokemon/${num}/"
            val request = Request.Builder().url(url).build()
            val poke = OkHttpClient()

            poke.newCall(request).enqueue(object:Callback{
                override fun onFailure(call: Call?, e: IOException?) {
                    //Toast.makeText(miContexto,"Error no pudo obtener Pokemones",Toast.LENGTH_LONG).show()
                    Log.e("Request poke", "no se puedo obtener Pokemones $num" )
                }

                override fun onResponse(call: Call?, response: Response?) {
                    val respuesta = response?.body()?.string()
                    val gson = GsonBuilder().create()

                    try {
                        val poke = gson.fromJson(respuesta,Pokemon::class.java)
                        listaPokemonesCompleta.add(poke)
                    }
                    catch(ex : Exception){
                        Log.e("json invalido","${ex.message}")
                    }
                }
            } )
        }

        boton.setOnClickListener {

            cargado()
        }
        return view
    }

    fun cargado(){

        activity!!.runOnUiThread{
            listaPokemonesCompleta = ordenar(listaPokemonesCompleta)//ordenada

            val adapterTodos = AdapterTodos(miContexto!!,listaPokemonesCompleta)
            rv_todos.layoutManager = LinearLayoutManager(miContexto, LinearLayout.VERTICAL,false)
            rv_todos.adapter = adapterTodos
            Toast.makeText(miContexto,"${listaPokemonesCompleta.size}",Toast.LENGTH_SHORT).show()
        }

    }

    fun ordenar(lista : ArrayList<Pokemon>): ArrayList<Pokemon>{

        val ordenada = ArrayList(lista.sortedBy { it.id })

        return  ordenada
    }

    override fun onResume() {
        listaPokemonesCompleta = ordenar(listaPokemonesCompleta) //Ordenada

        val adapterTodos = AdapterTodos(miContexto!!,listaPokemonesCompleta)
        rv_todos.layoutManager = LinearLayoutManager(miContexto, LinearLayout.VERTICAL,false)
        rv_todos.adapter = adapterTodos
        Toast.makeText(miContexto,"${listaPokemonesCompleta.size}",Toast.LENGTH_SHORT).show()
        super.onResume()
    }

    /*
    fun cargado(lista: ArrayList<Pokemon>){

        activity!!.runOnUiThread{
            val adapterTodos = AdapterTodos(miContexto!!,lista)
            rv_todos.layoutManager = LinearLayoutManager(miContexto, LinearLayout.VERTICAL,false)
            rv_todos.adapter = adapterTodos
            Toast.makeText(miContexto,"${lista.size}",Toast.LENGTH_SHORT).show()
        }

    } */


    //VERSION 1 SOLO CON ELEMENTOS 1

    /*
    fun cargado(){

        activity!!.runOnUiThread{
            val adapterTodos = AdapterTodos(miContexto!!,listaPokemones)
            rv_todos.layoutManager = LinearLayoutManager(miContexto, LinearLayout.VERTICAL,false)
            rv_todos.adapter = adapterTodos
            Toast.makeText(miContexto,"${listaPokemones.size}",Toast.LENGTH_SHORT).show()
        }

    } */
}


