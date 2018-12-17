package win.bemtorres.servicio.pokedex.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import win.bemtorres.servicio.pokedex.DetallePokemon
import win.bemtorres.servicio.pokedex.DetallePokemonFavorito
import win.bemtorres.servicio.pokedex.R
import win.bemtorres.servicio.pokedex.model.Result

class AdapterBuscar: RecyclerView.Adapter<AdapterBuscar.Companion.Holder>, Filterable {


    lateinit var listaPoke : MutableList<Result>
    lateinit var context : Context
    lateinit var rv: View
    lateinit var listaFiltrada : MutableList<Result>

    constructor(list: MutableList<Result>, conn : Context): super(){
        this.listaPoke = list
        this.listaFiltrada = listaPoke
        this.context = conn
    }

    companion object {
        class Holder : RecyclerView.ViewHolder {
            lateinit var lblNumero : TextView
            lateinit var lblNombre : TextView
            lateinit var img : ImageView
            lateinit var linear :  LinearLayout

            constructor(rv : View) : super(rv){
                lblNumero = rv.findViewById(R.id.lblNum)
                lblNombre = rv.findViewById(R.id.lblNombre)
                img = rv.findViewById(R.id.img_oficial)
                linear = rv.findViewById(R.id.cons_poke)
            }


        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var poke : Result
        poke = listaFiltrada.get(position)
        holder!!.lblNombre.text = poke.name.replace("-"," ").capitalize()
        holder!!.lblNumero.text = "#${poke.id}"
        var link = "https://pokeapi.co/media/sprites/pokemon/${poke.id}.png"
        Glide.with(context).load(link).into(holder!!.img)

      /*  rv.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {

            }

        }) */

        holder!!.linear.setOnClickListener {
            Toast.makeText(context,"Has seleccionado ${poke.name.capitalize().replace("-"," ")}", Toast.LENGTH_SHORT).show()
            var intento: Intent = Intent(context, DetallePokemon::class.java)
            intento.putExtra("id",poke.id)
            ContextCompat.startActivity(context, intento, null)
        }


    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var holder : Holder
        rv = LayoutInflater.from(parent!!.context).inflate(R.layout.item_listar,parent,false)
        holder = Holder(rv)
        return holder
    }

    override fun getItemCount(): Int {
            return  listaFiltrada.size
    }
    override fun getFilter(): Filter {
       return object  : Filter(){

           override fun performFiltering(p0: CharSequence?): FilterResults {
               /*var texto : String = p0.toString()
               if (texto.isEmpty()){
                   listaFiltrada = listaPoke
               }else{
                   var filtroLista : MutableList<Result> = mutableListOf()
                   for (r : Result in listaPoke){
                       if (r.name.toLowerCase().contains(texto.toLowerCase())){
                           filtroLista.add(r)
                       }
                   }
                   listaFiltrada = filtroLista
               }*/
               listaFiltrada = ArrayList(listaPoke.filter{poke -> poke.name.contains(p0!!,false)})
               var filterResult : FilterResults = FilterResults()
               filterResult.values = listaFiltrada
               return filterResult
           }
           override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                listaFiltrada = p1!!.values as MutableList<Result>
                notifyDataSetChanged()
           }

       }
    }

}