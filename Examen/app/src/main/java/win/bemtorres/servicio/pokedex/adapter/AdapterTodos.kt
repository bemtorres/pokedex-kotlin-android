package win.bemtorres.servicio.pokedex.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import win.bemtorres.servicio.pokedex.DetallePokemon
import win.bemtorres.servicio.pokedex.R
import win.bemtorres.servicio.pokedex.model.Result

class AdapterTodos(val miContexto: Context, var miLista:ArrayList<Result>?) : RecyclerView.Adapter<AdapterTodos.ViewHolder>(), View.OnClickListener{

    override fun onClick(p0: View?) {
        Toast.makeText(p0!!.context,"clic", Toast.LENGTH_LONG).show()
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterTodos.ViewHolder {
        val v = LayoutInflater.from(miContexto).inflate(R.layout.item_listar, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: AdapterTodos.ViewHolder, position: Int) {
        holder.bindItems(miLista!![position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return miLista!!.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(p: Result) {
            var vista = itemView
            var num: TextView = vista.findViewById(R.id.lblNum)
            val nombre: TextView = vista.findViewById(R.id.lblNombre)
            val foto: ImageView = vista.findViewById(R.id.img_oficial)
            val cons : LinearLayout = vista.findViewById(R.id.cons_poke)

            nombre.text = p.name.capitalize().replace("-"," ")
            num.text = "#${p.id}"

            var link = "https://pokeapi.co/media/sprites/pokemon/${p.id}.png"

            Glide.with(vista.context).load(link).into(foto)

            cons.setOnClickListener {
                Toast.makeText(vista.context,"Has seleccionado ${p.name.capitalize().replace("-"," ")}",Toast.LENGTH_SHORT).show()

                var intento: Intent = Intent(vista.context,DetallePokemon::class.java)
                intento.putExtra("id",p.id)
                startActivity(vista.context, intento,null)
            }
        }
    }




}