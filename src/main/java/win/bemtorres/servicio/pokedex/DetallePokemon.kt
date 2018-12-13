package win.bemtorres.servicio.pokedex

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_detalle_pokemon.*
import okhttp3.*
import win.bemtorres.servicio.pokedex.model.PokemonCompleto
import java.io.IOException

class DetallePokemon : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_pokemon)

        var id = intent.getIntExtra("id",0)

        var l1 : LinearLayout = this.findViewById(R.id.LL_1)
        var l2 : LinearLayout = this.findViewById(R.id.LL_2)
        var l3 : LinearLayout = this.findViewById(R.id.LL_3)
        var l4 : LinearLayout = this.findViewById(R.id.LL_4)

        var pokemon: PokemonCompleto? = null
        val url = "https://pokeapi.co/api/v2/pokemon/${id}/"
        val request = Request.Builder().url(url).build()
        val poke = OkHttpClient()

        poke.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                Log.e("Detalle Poke", "no se puedo obtener datos de $id" )
            }

            override fun onResponse(call: Call?, response: Response?) {
                val respuesta = response?.body()?.string()
                val gson = GsonBuilder().create()

                try {
                    pokemon = gson.fromJson(respuesta, PokemonCompleto::class.java)


                    runOnUiThread {
                        if (pokemon!! ==null){
                            Toast.makeText(this@DetallePokemon,"Error" , Toast.LENGTH_SHORT).show()
                        }else{
                            var link = "https://pokeapi.co/media/sprites/pokemon/${pokemon!!.id}.png"
                            Glide.with(this@DetallePokemon).load(link).into(img_oficial3)

                            var link1 = pokemon!!.sprites.front_default
                            var link2 = pokemon!!.sprites.front_female
                            var link3 = pokemon!!.sprites.front_shiny
                            var link4 = pokemon!!.sprites.front_shiny_female

                            if (link1.equals(null)){
                                l1.visibility = View.GONE
                            }else{
                                Glide.with(this@DetallePokemon).load(link1).into(img1)
                            }
                            if (link2.equals(null)){
                                l2.visibility = View.GONE
                            }else{
                                Glide.with(this@DetallePokemon).load(link2).into(img2)
                            }
                            if (link3.equals(null)){
                                l3.visibility = View.GONE
                            }else{
                                Glide.with(this@DetallePokemon).load(link3).into(img3)
                            }
                            if (link4.equals(null)){
                                l4.visibility = View.GONE
                            }else{
                                Glide.with(this@DetallePokemon).load(link4).into(img4)
                            }
                            lblNombre2.text = pokemon!!.name.capitalize()
                            lblNum2.text = "#${pokemon!!.id}"


                        }
                    }



                }
                catch(ex : Exception){
                    Log.e("json invalido","${ex.message}")
                }
            }
        } )



    }
}
