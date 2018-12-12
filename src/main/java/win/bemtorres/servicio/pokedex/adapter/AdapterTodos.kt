package win.bemtorres.servicio.pokedex.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import win.bemtorres.servicio.pokedex.R
import win.bemtorres.servicio.pokedex.model.Result

class AdapterTodos(val miContexto: Context, val miLista:ArrayList<Result>?) : RecyclerView.Adapter<AdapterTodos.ViewHolder>() , View.OnClickListener{

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

        fun bindItems(r: Result) {
            var vista = itemView
            var num: TextView = vista.findViewById(R.id.lblNum)
            val nombre: TextView = vista.findViewById(R.id.lblNombre)
            val foto: ImageButton = vista.findViewById(R.id.img_poke)
            val cons : ConstraintLayout = vista.findViewById(R.id.cons_poke)

            nombre.text = r.name

            cons.setOnClickListener {
                Toast.makeText(vista.context,"Has seleccionado ${r.name}",Toast.LENGTH_SHORT).show()
            }
        }
    }
}