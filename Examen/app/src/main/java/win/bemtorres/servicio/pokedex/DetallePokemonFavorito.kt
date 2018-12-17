package win.bemtorres.servicio.pokedex

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detalle_pokemon_favorito.*
import win.bemtorres.servicio.pokedex.conexion.ConexionSQL

class DetallePokemonFavorito : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_pokemon_favorito)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        var id = intent.getIntExtra("idF",0)

        var conn = ConexionSQL(this,null,1)
        var poke = conn.buscarPokemonIdP(id)

        lblNumF.text = "#${poke!!.idPokemon.toString()}"
        lblNombreF.text = poke!!.nombre.capitalize().replace("-"," ")
        var l1 : LinearLayout = this.findViewById(R.id.LL_F1)
        var l2 : LinearLayout = this.findViewById(R.id.LL_F2)
        var l3 : LinearLayout = this.findViewById(R.id.LL_F3)
        var l4 : LinearLayout = this.findViewById(R.id.LL_F4)

        var link = "https://pokeapi.co/media/sprites/pokemon/${poke!!.idPokemon}.png"
        Glide.with(this).load(link).into(img_oficialF)

        var link1 = poke!!.imgM
        var link2 = poke!!.imgF
        var link3 = poke!!.imgShinyM
        var link4 = poke!!.imgShinyF

        if (link1.equals("null")){
            l1.visibility = View.GONE
        }else{
            Glide.with(this).load(link1).into(imgF1)
            colorizarLinear(l1,R.color.water)
        }
        if (link2.equals("null")){
            l2.visibility = View.GONE
        }else{
            Glide.with(this).load(link2).into(imgF2)
            colorizarLinear(l2,R.color.fairy)
        }
        if (link3.equals("null")){
            l3.visibility = View.GONE
        }else{
            Glide.with(this).load(link3).into(imgF3)
            colorizarLinear(l3,R.color.electric)
        }
        if (link4.equals("null")){
            l4.visibility = View.GONE
        }else{
            Glide.with(this).load(link4).into(imgF4)
            colorizarLinear(l4,R.color.psychic)
        }

        lblTipoF1.visibility = View.GONE
        lblTipoF2.visibility = View.GONE

        if (!poke!!.tipo1.equals("null")){
            lblTipoF1.visibility = View.VISIBLE
            colorTipo(poke!!.tipo1.toLowerCase(),lblTipoF1)
            lblTipoF1.text = poke!!.tipo1
        }
        if (!poke!!.tipo2.equals("null")){
            lblTipoF2.visibility = View.VISIBLE
            colorTipo(poke!!.tipo2.toLowerCase(),lblTipoF2)
            lblTipoF2.text = poke!!.tipo2
        }


        //Habilidades

        lblHabilidadF1.visibility = View.GONE
        lblHabilidadF2.visibility = View.GONE

        if (!poke!!.habilidad1.equals("null")){
            lblHabilidadF1.visibility = View.VISIBLE
            lblHabilidadF1.text = poke!!.habilidad1.capitalize().replace("-"," ")
        }
        if (!poke!!.habilidad2.equals("null")){
            lblHabilidadF2.visibility = View.VISIBLE
            lblHabilidadF2.text = poke!!.habilidad2.capitalize().replace("-"," ")
        }

        lblVelocidadF.text = poke!!.speed.toString()
        lblDefensaEF.text = poke!!.especialDefense.toString()
        lblAtaqueEF.text = poke!!.especialAttack.toString()
        lblDefensaF.text = poke!!.defense.toString()
        lblAtaqueF.text = poke!!.attack.toString()
        lblHpF.text = poke!!.hp.toString()

        imgEliminar.setOnClickListener {

            val alerta = AlertDialog.Builder(this)
            alerta.setTitle("Eliminar")

            alerta.setMessage("¿Estás seguro que quieres eliminar a ${poke.nombre.capitalize()}?")
            alerta.setPositiveButton("Si", { dialog, which ->
                conn.eliminarProducto(poke!!.id)
                this.onBackPressed()
            })
            alerta.setNegativeButton("No", { dialog, which ->
                dialog.cancel()
            })
            alerta.show()
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
        }
    }
    fun colorizar(texto : TextView, id: Int){
        texto.setBackgroundColor(ContextCompat.getColor(this,id))
    }
    fun colorizarLinear(li : LinearLayout, id: Int){
        li.setBackgroundColor(ContextCompat.getColor(this,id))
    }
}
