package hu.bme.aut.pred2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface TeamDao {

    @Query("SELECT * FROM team")
    fun getAll(): List<Team>

    //@Query("SELECT * FROM team WHERE teamname = 'Alaves'")
    //fun findAlaves(): Team

    @Insert
    fun insert(teams: Team): Long

    @Update
    fun update(teams: Team)

    @Delete
    fun deleteItem(teams: Team)

    @Query("DELETE FROM team WHERE teamname = :name")
    fun deleteByUserId(name: String)

    @Query("DELETE FROM team")
    fun deleteDb()
}