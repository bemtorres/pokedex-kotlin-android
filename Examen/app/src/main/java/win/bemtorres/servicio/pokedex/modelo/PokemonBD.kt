package win.bemtorres.servicio.pokedex.modelo

class PokemonBD(var id:Int,
              var idPokemon:Int,
              var nombre: String,
              var imgM: String,
              var imgF: String,
              var imgShinyM: String,
              var imgShinyF:String,
              var tipo1:String,
              var tipo2:String,
              var habilidad1 : String,
              var habilidad2 : String,
              var speed : Int,
              var especialDefense: Int,
              var especialAttack: Int,
              var defense: Int,
              var attack : Int,
              var hp:Int) {
}