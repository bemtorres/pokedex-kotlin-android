package win.bemtorres.servicio.pokedex


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.fragment_fr_favorito.*
import kotlinx.android.synthetic.main.fragment_fr_todos.*
import win.bemtorres.servicio.pokedex.adapter.AdapterFavorito
import win.bemtorres.servicio.pokedex.adapter.AdapterTodos
import win.bemtorres.servicio.pokedex.conexion.ConexionSQL
import win.bemtorres.servicio.pokedex.modelo.PokemonBD


class Fr_favorito : Fragment() {
    var listaPokemones: ArrayList<PokemonBD> = ArrayList()
    var miContexto : Context?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_fr_favorito, container, false)
        var rv_lista : RecyclerView = view.findViewById(R.id.rv_favorito)
        var imgF : ImageView = view.findViewById(R.id.imgFavorito2)
        var lblF : TextView = view.findViewById(R.id.lblFavorito2)

        var conn = ConexionSQL(miContexto!!,null,1)

        listaPokemones = conn.listarPokemon()

        if (listaPokemones.size>0){
            val adapter = AdapterFavorito(miContexto!!,listaPokemones)
            //rv_lista.layoutManager = LinearLayoutManager(miContexto, LinearLayout.VERTICAL,false)

            rv_lista.layoutManager = GridLayoutManager(miContexto, 3)
            rv_lista.adapter = adapter
            imgF.visibility = View.GONE
            lblF.visibility = View.GONE
        }else{
            Toast.makeText(miContexto,"No hay favoritos",Toast.LENGTH_SHORT).show()
            imgF.visibility = View.VISIBLE
            lblF.visibility = View.VISIBLE
        }


        return view
    }


    fun ordenar(lista : ArrayList<PokemonBD>): ArrayList<PokemonBD>{
        val ordenada = ArrayList(lista.sortedBy { it.idPokemon })
        return  ordenada
    }

    override fun onResume() {
        var conn = ConexionSQL(miContexto!!,null,1)

        listaPokemones = conn.listarPokemon()


        if (listaPokemones.size>0){
            listaPokemones = ordenar(listaPokemones)

            rv_favorito.visibility = View.VISIBLE
            val adapter = AdapterFavorito(miContexto!!,listaPokemones)
            //rv_favorito.layoutManager = LinearLayoutManager(miContexto, LinearLayout.VERTICAL,false)
            rv_favorito.layoutManager = GridLayoutManager(miContexto, 3)
            rv_favorito.adapter = adapter

            imgFavorito2.visibility = View.GONE
            lblFavorito2.visibility = View.GONE
        }else{
            rv_favorito.visibility = View.GONE
            imgFavorito2.visibility = View.VISIBLE
            lblFavorito2.visibility = View.VISIBLE
        }


        super.onResume()
    }

}
