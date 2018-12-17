package win.bemtorres.servicio.pokedex

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_detalle_pokemon.*
import okhttp3.*
import win.bemtorres.servicio.pokedex.conexion.ConexionSQL
import win.bemtorres.servicio.pokedex.model.Hablar
import win.bemtorres.servicio.pokedex.model.PokemonCompleto
import win.bemtorres.servicio.pokedex.modelo.PokemonBD
import java.io.IOException

class DetallePokemon : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_pokemon)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        var id = intent.getIntExtra("id",0)

        var l1 : LinearLayout = this.findViewById(R.id.LL_1)
        var l2 : LinearLayout = this.findViewById(R.id.LL_2)
        var l3 : LinearLayout = this.findViewById(R.id.LL_3)
        var l4 : LinearLayout = this.findViewById(R.id.LL_4)

        var pbCargando : ProgressBar = this.findViewById(R.id.progressBar4)


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
                        if (pokemon==null){
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
                                colorizarLinear(l1,R.color.water)
                            }
                            if (link2.equals(null)){
                                l2.visibility = View.GONE
                            }else{
                                Glide.with(this@DetallePokemon).load(link2).into(img2)
                                colorizarLinear(l2,R.color.fairy)
                            }
                            if (link3.equals(null)){
                                l3.visibility = View.GONE
                            }else{
                                Glide.with(this@DetallePokemon).load(link3).into(img3)
                                colorizarLinear(l3,R.color.electric)
                            }
                            if (link4.equals(null)){
                                l4.visibility = View.GONE
                            }else{
                                Glide.with(this@DetallePokemon).load(link4).into(img4)
                                colorizarLinear(l4,R.color.psychic)
                            }
                            lblNombre2.text = pokemon!!.name.capitalize().replace("-"," ")
                            lblNum2.text = "#${pokemon!!.id}"
                            //Tipo de pokemon

                            lblTipo1.visibility = View.GONE
                            lblTipo2.visibility = View.GONE
                            for (cont in 1..pokemon!!.types.size){
                                if (cont==1){
                                    lblTipo1.visibility = View.VISIBLE
                                    colorTipo(pokemon!!.types[0].type.name,lblTipo1)
                                    lblTipo1.text = pokemon!!.types[0].type.name.capitalize()
                                }
                                if(cont==2){
                                    lblTipo2.visibility = View.VISIBLE
                                    colorTipo(pokemon!!.types[1].type.name,lblTipo2)
                                    lblTipo2.text = pokemon!!.types[1].type.name.capitalize()
                                }
                            }
                            //Habilidades

                            lblHabilidad1.visibility = View.GONE
                            lblHabilidad2.visibility = View.GONE
                            for(cont in 1..pokemon!!.abilities.size){
                                if(cont==1){
                                    lblHabilidad1.visibility = View.VISIBLE
                                    lblHabilidad1.text =pokemon!!.abilities[0].ability.name.capitalize().replace("-"," ")
                                }
                                if (cont==2){
                                    lblHabilidad2.visibility = View.VISIBLE
                                    lblHabilidad2.text =pokemon!!.abilities[1].ability.name.capitalize().replace("-"," ")
                                }
                            }

                            lblVelocidad.text = pokemon!!.stats[0].base_stat.toString()
                            lblDefensaE.text = pokemon!!.stats[1].base_stat.toString()
                            lblAtaqueE.text = pokemon!!.stats[2].base_stat.toString()
                            lblDefensa.text = pokemon!!.stats[3].base_stat.toString()
                            lblAtaque.text = pokemon!!.stats[4].base_stat.toString()
                            lblHp.text = pokemon!!.stats[5].base_stat.toString()

                            linear_Cargando.visibility = View.GONE
                            linear_Contenido.visibility = View.VISIBLE
                            pbCargando.visibility = View.GONE
                        }
                    }



                }
                catch(ex : Exception){
                    Log.e("json invalido","${ex.message}")
                }
            }
        } )

        //añadir a favorito
        imgFavorito.setOnClickListener {

            var conn = ConexionSQL(this,null,1)

            var pokemonEncontrado:Boolean = conn.buscarPokemonIdPokemon(pokemon!!.id)

            if (pokemonEncontrado){
                Toast.makeText(this,"Pokemon ya se encuentra como favorito",Toast.LENGTH_SHORT).show()
            }else {
                var pokemonBD = PokemonBD(0,0,"","","","","","","","","",0,0,0,0,0,0)

                pokemonBD.id = 0 //increment de la bd
                pokemonBD.idPokemon = pokemon!!.id
                pokemonBD.nombre = pokemon!!.name
                pokemonBD.imgM = pokemon!!.sprites.front_default.toString()
                pokemonBD.imgF = pokemon!!.sprites.front_female.toString()
                pokemonBD.imgShinyM = pokemon!!.sprites.front_shiny.toString()
                pokemonBD.imgShinyF = pokemon!!.sprites.front_shiny_female.toString()
                pokemonBD.tipo1 = "null"
                pokemonBD.tipo2 = "null"
                for (cont in 1..pokemon!!.types.size){
                    if (cont==1){
                        pokemonBD.tipo1 = pokemon!!.types[0].type.name.capitalize()
                    }
                    if(cont==2){
                        pokemonBD.tipo2= pokemon!!.types[1].type.name.capitalize()
                    }
                }
                pokemonBD.habilidad1 ="null"
                pokemonBD.habilidad2 ="null"
                for(cont in 1..pokemon!!.abilities.size){
                    if(cont==1){
                        pokemonBD.habilidad1 =pokemon!!.abilities[0].ability.name.capitalize().replace("-"," ")
                    }
                    if (cont==2){
                        pokemonBD.habilidad2 =pokemon!!.abilities[1].ability.name.capitalize().replace("-"," ")
                    }
                }

                pokemonBD.speed = pokemon!!.stats[0].base_stat
                pokemonBD.especialDefense = pokemon!!.stats[1].base_stat
                pokemonBD.especialAttack = pokemon!!.stats[2].base_stat
                pokemonBD.defense = pokemon!!.stats[3].base_stat
                pokemonBD.attack = pokemon!!.stats[4].base_stat
                pokemonBD.hp = pokemon!!.stats[5].base_stat

                conn.insertarProkemon(pokemonBD)
            }
        }



        img_reproducir.setOnClickListener {
            var texto = "Pokemon número ${pokemon!!.id}, nombre ${pokemon!!.name} "

            for (cont in 1..pokemon!!.types.size){
                if (cont==1){
                    texto+=" de tipo ${pokemon!!.types[0].type.name} "
                }
                if(cont==2){
                    texto+=" y  de tipo ${pokemon!!.types[1].type.name} "
                }
            }


             Hablar(this, texto)

        }

    }


    fun colorTipo(n: String, texto : TextView){
        when(n){
            "normal" -> colorizar(texto,R.color.normal)
            "fighting" -> colorizar(texto,R.color.fighting)
            "flying" -> colorizar(texto,R.color.flying)
            "poison" -> colorizar(texto,R.color.posion)
            "ground" -> colorizar(texto,R.color.ground)
            "rock" -> colorizar(texto,R.color.rock)
            "bug" ->  colorizar(texto,R.color.bug)
            "ghost" -> colorizar(texto,R.color.ghost)
            "steel" -> colorizar(texto,R.color.steel)
            "fire" ->  colorizar(texto,R.color.fire)
            "water" ->  colorizar(texto,R.color.water)
            "grass" ->  colorizar(texto,R.color.grass)
            "electric" ->  colorizar(texto,R.color.electric)
            "psychic" ->  colorizar(texto,R.color.psychic)
            "ice" ->  colorizar(texto,R.color.ice)
            "fairy" ->  colorizar(texto,R.color.fairy)
            "dark" ->  colorizar(texto,R.color.dark)
            "shadow" ->  colorizar(texto,R.color.dark)
            "unknown" ->  colorizar(texto,R.color.normal)
            else -> colorizar(texto,R.color.normal)
        }
    }
    fun colorizar(texto : TextView, id: Int){
        texto.setBackgroundColor(ContextCompat.getColor(this,id))
    }
    fun colorizarLinear(li : LinearLayout, id: Int){
        li.setBackgroundColor(ContextCompat.getColor(this,id))
    }
}
