package win.bemtorres.servicio.pokedex.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import win.bemtorres.servicio.pokedex.DetallePokemon
import win.bemtorres.servicio.pokedex.DetallePokemonFavorito
import win.bemtorres.servicio.pokedex.R
import win.bemtorres.servicio.pokedex.modelo.PokemonBD

class AdapterFavorito (val miContexto: Context, val miLista:ArrayList<PokemonBD>?) : RecyclerView.Adapter<AdapterFavorito.ViewHolder>() , View.OnClickListener{

    override fun onClick(p0: View?) {
        Toast.makeText(p0!!.context,"clic", Toast.LENGTH_LONG).show()
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterFavorito.ViewHolder {
        val v = LayoutInflater.from(miContexto).inflate(R.layout.item_listar_favorito, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: AdapterFavorito.ViewHolder, position: Int) {
        holder.bindItems(miLista!![position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return miLista!!.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(p: PokemonBD) {
            var vista = itemView
            var num: TextView = vista.findViewById(R.id.lblNum1)
            val nombre: TextView = vista.findViewById(R.id.lblNombre1)
            val foto: ImageView = vista.findViewById(R.id.img_favorito)
            val cons : LinearLayout = vista.findViewById(R.id.linear_favorito)

            nombre.text = p.nombre.capitalize()
            num.text = "#${p.idPokemon}"


            var link = "https://pokeapi.co/media/sprites/pokemon/${p.idPokemon}.png"

            Glide.with(vista.context).load(link).into(foto)



            cons.setOnClickListener {
                Toast.makeText(vista.context,"Has seleccionado ${p.nombre.capitalize()}", Toast.LENGTH_SHORT).show()

                var intento: Intent = Intent(vista.context,DetallePokemonFavorito::class.java)
                intento.putExtra("idF",p.idPokemon)
                ContextCompat.startActivity(vista.context, intento, null)
            }
        }
    }
}