package win.bemtorres.servicio.pokedex.conexion

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import win.bemtorres.servicio.pokedex.modelo.PokemonBD
import java.lang.Exception
import java.sql.SQLException

class ConexionSQL (val miContexto: Context,
                   val factory: SQLiteDatabase.CursorFactory?,
                   val version: Int): SQLiteOpenHelper(miContexto,"PokedexBD",factory,version){

    override fun onCreate(db: SQLiteDatabase?) {
        val query1 = "CREATE TABLE pokemon(id INTEGER PRIMARY KEY AUTOINCREMENT, idPokemon INTEGER, nombre TEXT, imgM TEXT, imgF TEXT, imgShinyM TEXT, imgShinyF Text,  tipo1 TEXT, tipo2 TEXT, habilidad1 TEXT, habilidad2 TEXT, speed INTEGER, especialDefense INTEGER, especialAttack INTEGER, defense INTEGER, attack INTEGER, hp INTEGER )"
        db?.execSQL(query1)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val query1 = "DROP TABLE IF EXISTS pokemon;"
        db?.execSQL(query1)
        onCreate(db)
    }

    fun insertarProkemon(po: PokemonBD){
        try {
            val db = this.writableDatabase
            var cv = ContentValues()

            cv.put("idPokemon" , po.idPokemon)
            cv.put("nombre" , po.nombre)
            cv.put("imgM" , po.imgM)
            cv.put("imgF" , po.imgF)
            cv.put("imgShinyM" , po.imgShinyM)
            cv.put("imgShinyF" , po.imgShinyF)
            cv.put("tipo1" , po.tipo1)
            cv.put("tipo2" , po.tipo2)
            cv.put("habilidad1" , po.habilidad1)
            cv.put("habilidad2" , po.habilidad2)
            cv.put("speed" , po.speed)
            cv.put("especialDefense" , po.especialDefense)
            cv.put("especialAttack" , po.especialAttack)
            cv.put("defense" , po.defense)
            cv.put("attack" , po.attack)
            cv.put("hp" , po.hp)

            val result = db.insert("pokemon", null, cv)
            db.close()
            if (result == -1L){
                Toast.makeText(miContexto, "Pokemón NO Agregado", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(miContexto, "Pokemon Añadido a favorito", Toast.LENGTH_SHORT).show()
            }
        }catch (e: Exception){
            Toast.makeText(miContexto,  "Error ${e.message}", Toast.LENGTH_SHORT).show()
        }

    }

    //
    // LISTAR
    //


    fun listarPokemon() : ArrayList<PokemonBD> {
        var lista = ArrayList<PokemonBD>()
        try {
            val db = this.writableDatabase
            var cursor: Cursor? = null

            cursor = db.rawQuery("SELECT * FROM pokemon", null)
            if (cursor?.moveToFirst() == true) {
                do {
                    val id = cursor.getInt(0)
                    val idP = cursor.getInt(1)
                    val nombre = cursor.getString(2)
                    val imgM = cursor.getString(3)
                    val imgF = cursor.getString(4)
                    val imgShinyM = cursor.getString(5)
                    val imgShinyF = cursor.getString(6)
                    val tipo1 = cursor.getString(7)
                    val tipo2 = cursor.getString(8)
                    val h1 = cursor.getString(9)
                    val h2 = cursor.getString(10)
                    val speed = cursor.getInt(11)
                    val especialD = cursor.getInt(12)
                    val especialA = cursor.getInt(13)
                    val defense = cursor.getInt(14)
                    val attack = cursor.getInt(15)
                    val hp = cursor.getInt(16)

                    val pro = PokemonBD(
                        id,
                        idP,
                        nombre,
                        imgM,
                        imgF,
                        imgShinyM,
                        imgShinyF,
                        tipo1,
                        tipo2,
                        h1,
                        h2,
                        speed,
                        especialD,
                        especialA,
                        defense,
                        attack,
                        hp
                    )

                    lista.add(pro)
                } while (cursor.moveToNext())
            }
            return lista
        } catch (ex: SQLException) {
            Toast.makeText(miContexto, "Error ${ex.message}", Toast.LENGTH_SHORT).show()
            Log.e("sq lListar", ex.message)
            return lista
        }
    }

    //
    //  ELIMINAR
    //

    fun eliminarProducto(id:Int){
        try {
            val db = this.writableDatabase
            val args = arrayOf(id.toString())
            val result = db.delete("pokemon","id=?",args)
            if (result ==0){
                Toast.makeText(miContexto,"Pokemon No Eliminado- $args", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(miContexto,"Eliminado de favorito", Toast.LENGTH_SHORT).show()
            }
        }catch (ex: SQLException){
            Toast.makeText(miContexto,"error ${ex.message}", Toast.LENGTH_SHORT).show()
            Log.e("sql Eliminar", ex.message)
        }
    }

    //
    // ACTUALIZAR
    //

    fun actualizarPokemon(po : PokemonBD){
        try{
            val db = this.writableDatabase
            var cv = ContentValues()

            val args = arrayOf(po.id.toString())

            cv.put("idPokemon" , po.idPokemon)
            cv.put("nombre" , po.nombre)
            cv.put("imgM" , po.imgM)
            cv.put("imgF" , po.imgF)
            cv.put("imgShinyM" , po.imgShinyM)
            cv.put("imgShinyF" , po.imgShinyF)
            cv.put("tipo1" , po.tipo1)
            cv.put("tipo2" , po.tipo2)
            cv.put("habilidad1" , po.habilidad1)
            cv.put("habilidad2" , po.habilidad2)
            cv.put("speed" , po.speed)
            cv.put("especialDefense" , po.especialDefense)
            cv.put("especialAttack" , po.especialAttack)
            cv.put("defense" , po.defense)
            cv.put("attack" , po.attack)
            cv.put("hp" , po.hp)

            val result = db.update("pokemon",cv,"id=?",args)
            db.close()
            if (result==0){
                Toast.makeText(miContexto,"Pokemon NO Actualizado", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(miContexto,"Producto Actualizado", Toast.LENGTH_SHORT).show()
            }
        }catch (ex: Exception){
            Toast.makeText(miContexto,"error ${ex.message}", Toast.LENGTH_SHORT).show()
            Log.e("sql Actualizar",ex.message)
        }
    }

    //
    // BUSCAR
    //
    fun buscarPokemonIdP(id: Int) : PokemonBD?{
        var pro : PokemonBD? = null
        try{
            val db = this.writableDatabase
            var cursor: Cursor? = null

            cursor = db.rawQuery("SELECT * FROM pokemon", null)
            if (cursor?.moveToFirst() == true) {
                do {
                    if(id == cursor.getInt(1)){

                        val id = cursor.getInt(0)
                        val idP = cursor.getInt(1)
                        val nombre = cursor.getString(2)
                        val imgM = cursor.getString(3)
                        val imgF = cursor.getString(4)
                        val imgShinyM = cursor.getString(5)
                        val imgShinyF = cursor.getString(6)
                        val tipo1 = cursor.getString(7)
                        val tipo2 = cursor.getString(8)
                        val h1 = cursor.getString(9)
                        val h2 = cursor.getString(10)
                        val speed = cursor.getInt(11)
                        val especialD = cursor.getInt(12)
                        val especialA = cursor.getInt(13)
                        val defense = cursor.getInt(14)
                        val attack = cursor.getInt(15)
                        val hp = cursor.getInt(16)

                        pro = PokemonBD(
                            id,
                            idP,
                            nombre,
                            imgM,
                            imgF,
                            imgShinyM,
                            imgShinyF,
                            tipo1,
                            tipo2,
                            h1,
                            h2,
                            speed,
                            especialD,
                            especialA,
                            defense,
                            attack,
                            hp
                        )


                        break

                    }
                } while (cursor.moveToNext())
            }
            return pro
        }catch (ex: Exception){
            Toast.makeText(miContexto,"error ${ex.message}", Toast.LENGTH_SHORT).show()
            Log.e("sql Buscar Pokemon",ex.message)
            return  null
        }
    }

    fun buscarPokemonIdPokemon(id: Int) : Boolean{
        var estado : Boolean = false
        try{
            val db = this.writableDatabase
            var cursor: Cursor? = null

            cursor = db.rawQuery("SELECT * FROM pokemon", null)
            if (cursor?.moveToFirst() == true) {
                do {
                    if(id == cursor.getInt(1)){
                        estado = true
                        break
                    }
                } while (cursor.moveToNext())
            }
            return estado
        }catch (ex: Exception){
            Toast.makeText(miContexto,"error ${ex.message}", Toast.LENGTH_SHORT).show()
            Log.e("sql Buscar Pokemon",ex.message)
            return  false
        }
    }

    fun buscarPokemonId(id: Int) : PokemonBD?{
        var pro : PokemonBD? = null
        try{
            val db = this.writableDatabase
            var cursor: Cursor? = null

            cursor = db.rawQuery("SELECT * FROM pokemon", null)
            if (cursor?.moveToFirst() == true) {
                do {
                    if(id == cursor.getInt(0)){

                        val id = cursor.getInt(0)
                        val idP = cursor.getInt(1)
                        val nombre = cursor.getString(2)
                        val imgM = cursor.getString(3)
                        val imgF = cursor.getString(4)
                        val imgShinyM = cursor.getString(5)
                        val imgShinyF = cursor.getString(6)
                        val tipo1 = cursor.getString(7)
                        val tipo2 = cursor.getString(8)
                        val h1 = cursor.getString(9)
                        val h2 = cursor.getString(10)
                        val speed = cursor.getInt(11)
                        val especialD = cursor.getInt(12)
                        val especialA = cursor.getInt(13)
                        val defense = cursor.getInt(14)
                        val attack = cursor.getInt(15)
                        val hp = cursor.getInt(16)

                        pro = PokemonBD(
                            id,
                            idP,
                            nombre,
                            imgM,
                            imgF,
                            imgShinyM,
                            imgShinyF,
                            tipo1,
                            tipo2,
                            h1,
                            h2,
                            speed,
                            especialD,
                            especialA,
                            defense,
                            attack,
                            hp
                        )
                        break
                    }
                } while (cursor.moveToNext())
            }
            return pro
        }catch (ex: Exception){
            Toast.makeText(miContexto,"error ${ex.message}", Toast.LENGTH_SHORT).show()
            Log.e("sql Buscar Pokemon",ex.message)
            return  null
        }
    }
}
