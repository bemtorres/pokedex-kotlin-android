package win.bemtorres.servicio.pokedex.model

import java.sql.Types

class PokemonCompleto(var id: Int, var order:Int, var name: String,var abilities : ArrayList<Habilidad>, var sprites:Sprite) {
}
//class PokemonCompleto(var id: Int, var order:Int, var name: String,var abilities : ArrayList<Habilidad>, var sprites:Sprite, var stats: ArrayList<Status>, var types: ArrayList<Tipo>) {
//}